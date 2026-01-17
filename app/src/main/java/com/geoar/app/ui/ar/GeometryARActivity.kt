package com.geoar.app.ui.ar

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.geoar.app.data.ProgressRepository
import com.geoar.app.databinding.ActivityGeometryArBinding
import com.geoar.app.education.EducationalContentProvider
import com.geoar.app.geometry.GeometryType
import com.geoar.app.rendering.GeometryRenderer
import com.geoar.app.ar.ARSessionManager
import com.google.ar.core.*
import kotlinx.coroutines.launch
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GeometryARActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGeometryArBinding
    private lateinit var viewModel: ARViewModel
    
    private lateinit var sessionManager: ARSessionManager
    private lateinit var progressRepository: ProgressRepository
    private lateinit var geometryRenderer: GeometryRenderer
    
    private var arSession: Session? = null
    private var isPlacing = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeometryArBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Inicializar dependências
        sessionManager = ARSessionManager(this)
        progressRepository = ProgressRepository(this)
        
        // Configurar ViewModel
        viewModel = ViewModelProvider(this, ARViewModelFactory(this))[ARViewModel::class.java]
        
        lifecycleScope.launch {
            viewModel.initializeARSession()
        }
        
        setupUI()
        setupGLSurface()
        observeViewModel()
        
        val selectedGeometry = intent.getStringExtra("GEOMETRY_TYPE")
        if (selectedGeometry != null) {
            GeometryType.valueOf(selectedGeometry).also { type ->
                viewModel.selectGeometry(type)
                showGeometryInfo(type)
            }
        }
    }
    
    private fun setupUI() {
        binding.deleteButton.setOnClickListener {
            viewModel.deleteSelectedObject()
        }
        
        binding.clearButton.setOnClickListener {
            showClearConfirmation()
        }
        
        binding.infoButton.setOnClickListener {
            val selected = viewModel.selectedGeometry.value
            if (selected != null) {
                showEducationalDialog(selected)
            }
        }
        
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    
    private fun setupGLSurface() {
        binding.arSurfaceView.setEGLContextClientVersion(2)
        geometryRenderer = GeometryRenderer()
        
        binding.arSurfaceView.setRenderer(object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
                GLES20.glEnable(GLES20.GL_DEPTH_TEST)
                GLES20.glEnable(GLES20.GL_BLEND)
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            }
            
            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }
            
            override fun onDrawFrame(gl: GL10?) {
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
                
                arSession?.let { session ->
                    try {
                        session.setCameraTextureName(0)
                        val frame = session.update()
                        
                        if (frame.hasDisplayRotationChanged()) {
                            session.setDisplayGeometry(
                                frame.camera.imageIntrinsics.width,
                                frame.camera.imageIntrinsics.height,
                                0
                            )
                        }
                        
                        // Renderizar objetos colocados
                        viewModel.placedObjects.value.forEach { obj ->
                            val color = when (obj.geometryType) {
                                GeometryType.CUBE -> floatArrayOf(1.0f, 0.0f, 0.0f)
                                GeometryType.SPHERE -> floatArrayOf(0.0f, 1.0f, 0.0f)
                                GeometryType.CYLINDER -> floatArrayOf(0.0f, 0.0f, 1.0f)
                                else -> floatArrayOf(1.0f, 1.0f, 0.0f)
                            }
                            geometryRenderer.renderGeometry(
                                obj.geometryType,
                                obj.position,
                                obj.scale,
                                if (obj.isSelected) color.map { it * 0.5f }.toFloatArray() else color
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
        
        binding.arSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.arStatus.collect { status ->
                when (status) {
                    ARStatus.READY -> {
                        arSession = sessionManager.getSession()
                        binding.arSurfaceView.onResume()
                        sessionManager.resumeSession()
                    }
                    ARStatus.ACTIVE -> {
                        binding.scanningOverlay.visibility = View.GONE
                    }
                    ARStatus.ERROR -> {
                        Toast.makeText(this@GeometryARActivity, "Erro ao inicializar AR", Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.selectedGeometry.collect { geometry ->
                if (geometry != null) {
                    binding.geometryName.text = geometry.displayName
                    binding.instructionText.text = "Toque na superfície detectada para posicionar"
                    binding.instructionText.visibility = View.VISIBLE
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.placedObjects.collect { objects ->
                // Atualizar UI conforme necessário
            }
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !isPlacing) {
            placeObjectOnSurface(event)
        }
        return super.onTouchEvent(event)
    }
    
    private fun placeObjectOnSurface(event: MotionEvent) {
        val geometry = viewModel.selectedGeometry.value ?: return
        val session = arSession ?: return
        
        try {
            val frame = session.update()
            val hitResults = frame.hitTest(event.x, event.y)
            
            val plane = hitResults.firstOrNull { hit ->
                val trackable = hit.trackable
                trackable is Plane && trackable.isPoseInPolygon(hit.hitPose) &&
                        trackable.trackingState == Trackable.TrackingState.TRACKING
            }
            
            plane?.let { hit ->
                val anchor = hit.createAnchor()
                val pose = anchor.pose
                
                val poseData = floatArrayOf(
                    pose.tx(),
                    pose.ty(),
                    pose.tz()
                )
                
                viewModel.placeObject(poseData)
                Toast.makeText(this, "${geometry.displayName} posicionado!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun showGeometryInfo(type: GeometryType) {
        val info = EducationalContentProvider.getContentFor(type)
        binding.geometryDescription.text = info.description
    }
    
    private fun showEducationalDialog(type: GeometryType) {
        val content = EducationalContentProvider.getContentFor(type)
        
        val message = buildString {
            append(content.description)
            append("\n\n")
            content.formulas.forEach { formula ->
                append("${formula.name}: ${formula.formula}\n")
                append("  ${formula.explanation}\n\n")
            }
        }
        
        AlertDialog.Builder(this)
            .setTitle(content.title)
            .setMessage(message)
            .setPositiveButton("Fechar", null)
            .show()
    }
    
    private fun showClearConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Limpar Tudo")
            .setMessage("Deseja remover todos os objetos posicionados?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.clearAll()
            }
            .setNegativeButton("Não", null)
            .show()
    }
    
    override fun onResume() {
        super.onResume()
        arSession?.let {
            sessionManager.resumeSession()
            binding.arSurfaceView.onResume()
        }
    }
    
    override fun onPause() {
        super.onPause()
        sessionManager.pauseSession()
        binding.arSurfaceView.onPause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        binding.arSurfaceView.onPause()
        sessionManager.resetSession()
    }
}
