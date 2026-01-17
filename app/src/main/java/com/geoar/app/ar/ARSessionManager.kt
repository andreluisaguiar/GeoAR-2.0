package com.geoar.app.ar

import android.content.Context
import com.google.ar.core.*
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException

/**
 * Gerenciador de sessão ARCore
 */
class ARSessionManager(private val context: Context) {
    
    private var session: Session? = null
    var isSessionActive = false
        private set
    
    /**
     * Verifica se o dispositivo é compatível com ARCore
     */
    fun checkAvailability(): ARCoreAvailability {
        return when (ArCoreApk.getInstance().checkAvailability(context)) {
            Availability.UNKNOWN_ERROR -> ARCoreAvailability.ERROR
            Availability.UNKNOWN_CHECKING -> ARCoreAvailability.CHECKING
            Availability.UNKNOWN_TIMED_OUT -> ARCoreAvailability.ERROR
            Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> ARCoreAvailability.NOT_SUPPORTED
            Availability.SUPPORTED_NOT_INSTALLED -> ARCoreAvailability.NOT_INSTALLED
            Availability.SUPPORTED_APK_TOO_OLD -> ARCoreAvailability.NEEDS_UPDATE
            Availability.SUPPORTED_INSTALLED -> ARCoreAvailability.INSTALLED
            else -> ARCoreAvailability.ERROR
        }
    }
    
    /**
     * Inicializa a sessão ARCore
     */
    @Throws(UnavailableDeviceNotCompatibleException::class,
            UnavailableUserDeclinedInstallationException::class)
    suspend fun initializeSession(): Session {
        val session = Session(context)
        val config = Config(session)
        
        // Configurar detecção de planos horizontais
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
        
        // Configurar outros parâmetros de ARCore
        config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        config.focusMode = Config.FocusMode.AUTO
        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        
        session.configure(config)
        this.session = session
        isSessionActive = true
        
        return session
    }
    
    /**
     * Obtém a sessão ARCore atual
     */
    fun getSession(): Session? = session
    
    /**
     * Reseta a sessão
     */
    fun resetSession() {
        session?.close()
        session = null
        isSessionActive = false
    }
    
    /**
     * Resume a sessão
     */
    fun resumeSession() {
        session?.resume()
    }
    
    /**
     * Pause a sessão
     */
    fun pauseSession() {
        session?.pause()
    }
    
    /**
     * Retorna informações sobre os planos detectados
     */
    fun getDetectedPlanes(): List<Plane> {
        return session?.getAllTrackables(Plane::class.java)?.toList() ?: emptyList()
    }
    
    /**
     * Cria um anchor na posição especificada
     */
    fun createAnchor(pose: Pose): Anchor? {
        return session?.createAnchor(pose)
    }
}

enum class ARCoreAvailability {
    CHECKING,
    INSTALLED,
    NOT_INSTALLED,
    NEEDS_UPDATE,
    NOT_SUPPORTED,
    ERROR
}

