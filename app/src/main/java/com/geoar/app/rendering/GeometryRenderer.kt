package com.geoar.app.rendering

import android.opengl.GLES20
import android.opengl.Matrix
import com.geoar.app.geometry.GeometryType
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Renderizador de formas geométricas 3D usando OpenGL ES
 */
class GeometryRenderer {
    
    private var mProgram: Int = 0
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)
    
    private val vertexShaderCode = """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        uniform vec3 uColor;
        varying vec3 fragColor;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
            fragColor = uColor;
        }
    """.trimIndent()
    
    private val fragmentShaderCode = """
        precision mediump float;
        varying vec3 fragColor;
        void main() {
            gl_FragColor = vec4(fragColor, 1.0);
        }
    """.trimIndent()
    
    init {
        loadShaders()
    }
    
    /**
     * Carrega os shaders OpenGL
     */
    private fun loadShaders() {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        
        mProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram, vertexShader)
        GLES20.glAttachShader(mProgram, fragmentShader)
        GLES20.glLinkProgram(mProgram)
        
        val status = IntArray(1)
        GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, status, 0)
        if (status[0] == 0) {
            GLES20.glDeleteProgram(mProgram)
            throw RuntimeException("Error creating program")
        }
    }
    
    /**
     * Carrega um shader individual
     */
    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader)
            throw RuntimeException("Error compiling shader: " + GLES20.glGetShaderInfoLog(shader))
        }
        return shader
    }
    
    /**
     * Renderiza uma forma geométrica
     */
    fun renderGeometry(
        type: GeometryType,
        position: FloatArray,
        scale: Float,
        color: FloatArray
    ) {
        val vertices = when (type) {
            GeometryType.CUBE -> cubeVertices
            GeometryType.SPHERE -> sphereVertices
            GeometryType.CYLINDER -> cylinderVertices
            GeometryType.CONE -> coneVertices
            GeometryType.PYRAMID -> pyramidVertices
            GeometryType.SQUARE -> squareVertices
            GeometryType.CIRCLE -> circleVertices
            GeometryType.TRIANGLE -> triangleVertices
        }
        
        val indices = when (type) {
            GeometryType.CUBE -> cubeIndices
            GeometryType.CYLINDER -> cylinderIndices
            GeometryType.CONE -> coneIndices
            GeometryType.PYRAMID -> pyramidIndices
            else -> shortArrayOf() // Sem índices para formas 2D simples
        }
        
        val vertexBuffer = createFloatBuffer(vertices)
        val indexBuffer = indices.takeIf { it.isNotEmpty() }?.let { createShortBuffer(it) }
        
        GLES20.glUseProgram(mProgram)
        
        // Aplicar transformações
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, position[0], position[1], position[2])
        Matrix.scaleM(mModelMatrix, 0, scale, scale, scale)
        
        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, mProjectionMatrix, 0, mvpMatrix, 0)
        
        // Configurar atributos
        val positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        
        val colorHandle = GLES20.glGetUniformLocation(mProgram, "uColor")
        GLES20.glUniform3fv(colorHandle, 1, color, 0)
        
        val mvpMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        
        // Renderizar
        if (indexBuffer != null) {
            indexBuffer.position(0)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)
        } else {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.size / 3)
        }
        
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
    
    /**
     * Define a matriz de projeção
     */
    fun setProjectionMatrix(projectionMatrix: FloatArray) {
        System.arraycopy(projectionMatrix, 0, mProjectionMatrix, 0, 16)
    }
    
    /**
     * Define a matriz de visualização
     */
    fun setViewMatrix(viewMatrix: FloatArray) {
        System.arraycopy(viewMatrix, 0, mViewMatrix, 0, 16)
    }
    
    // Funções auxiliares para criar buffers
    private fun createFloatBuffer(array: FloatArray): FloatBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(array.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        val buffer = byteBuffer.asFloatBuffer()
        buffer.put(array)
        buffer.position(0)
        return buffer
    }
    
    private fun createShortBuffer(array: ShortArray): ShortBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(array.size * 2)
        byteBuffer.order(ByteOrder.nativeOrder())
        val buffer = byteBuffer.asShortBuffer()
        buffer.put(array)
        buffer.position(0)
        return buffer
    }
    
    // Datos de vértices para diferentes formas
    private val cubeVertices = floatArrayOf(
        // Front face
        -1f, -1f, 1f, 1f, -1f, 1f, 1f, 1f, 1f, -1f, 1f, 1f,
        // Back face
        -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f,
        // Left face
        -1f, -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f,
        // Right face
        1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f, 1f, 1f, -1f, 1f,
        // Top face
        -1f, 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, -1f,
        // Bottom face
        -1f, -1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, -1f, -1f, 1f
    )
    
    private val cubeIndices = shortArrayOf(
        0, 1, 2, 0, 2, 3,  // front
        4, 5, 6, 4, 6, 7,  // back
        8, 9, 10, 8, 10, 11, // left
        12, 13, 14, 12, 14, 15, // right
        16, 17, 18, 16, 18, 19, // top
        20, 21, 22, 20, 22, 23  // bottom
    )
    
    private val sphereVertices = generateSphereVertices(20, 20, 1.0f)
    private val cylinderVertices = generateCylinderVertices(20, 1.0f, 2.0f)
    private val coneVertices = generateConeVertices(20, 1.0f, 2.0f)
    private val pyramidVertices = floatArrayOf(
        0f, 1f, 0f, -1f, -1f, 1f, 1f, -1f, 1f,
        0f, 1f, 0f, 1f, -1f, 1f, 1f, -1f, -1f,
        0f, 1f, 0f, 1f, -1f, -1f, -1f, -1f, -1f,
        0f, 1f, 0f, -1f, -1f, -1f, -1f, -1f, 1f
    )
    
    private val pyramidIndices = shortArrayOf(
        0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 1
    )
    
    private val squareVertices = floatArrayOf(
        -1f, -1f, 0f, 1f, -1f, 0f, 1f, 1f, 0f, -1f, 1f, 0f
    )
    
    private val circleVertices = generateCircleVertices(32, 1.0f)
    
    private val triangleVertices = floatArrayOf(
        0f, 1f, 0f, -1f, -1f, 0f, 1f, -1f, 0f
    )
    
    private val cylinderIndices = shortArrayOf()
    private val coneIndices = shortArrayOf()
    
    private fun generateSphereVertices(radius: Int, stacks: Int, slice: Float): FloatArray {
        val vertices = mutableListOf<Float>()
        for (i in 0..stacks) {
            val phi = Math.PI * i / stacks
            for (j in 0..radius) {
                val theta = 2.0 * Math.PI * j / radius
                val x = (Math.cos(phi) * Math.sin(theta) * slice).toFloat()
                val y = (Math.sin(phi) * slice).toFloat()
                val z = (Math.cos(phi) * Math.cos(theta) * slice).toFloat()
                vertices.add(x)
                vertices.add(y)
                vertices.add(z)
            }
        }
        return vertices.toFloatArray()
    }
    
    private fun generateCylinderVertices(sectors: Int, radius: Float, height: Float): FloatArray {
        val vertices = mutableListOf<Float>()
        for (i in 0..sectors) {
            val angle = 2.0 * Math.PI * i / sectors
            val x = (Math.cos(angle) * radius).toFloat()
            val y = height / 2
            val z = (Math.sin(angle) * radius).toFloat()
            vertices.add(x)
            vertices.add(y)
            vertices.add(z)
            
            vertices.add(x)
            vertices.add(-y)
            vertices.add(z)
        }
        return vertices.toFloatArray()
    }
    
    private fun generateConeVertices(sectors: Int, radius: Float, height: Float): FloatArray {
        val vertices = mutableListOf<Float>()
        vertices.add(0f)
        vertices.add(height / 2)
        vertices.add(0f)
        
        for (i in 0..sectors) {
            val angle = 2.0 * Math.PI * i / sectors
            val x = (Math.cos(angle) * radius).toFloat()
            val y = -height / 2
            val z = (Math.sin(angle) * radius).toFloat()
            vertices.add(x)
            vertices.add(y)
            vertices.add(z)
        }
        return vertices.toFloatArray()
    }
    
    private fun generateCircleVertices(sectors: Int, radius: Float): FloatArray {
        val vertices = mutableListOf<Float>()
        vertices.add(0f)
        vertices.add(0f)
        vertices.add(0f)
        
        for (i in 0..sectors) {
            val angle = 2.0 * Math.PI * i / sectors
            val x = (Math.cos(angle) * radius).toFloat()
            val y = (Math.sin(angle) * radius).toFloat()
            val z = 0f
            vertices.add(x)
            vertices.add(y)
            vertices.add(z)
        }
        return vertices.toFloatArray()
    }
}

