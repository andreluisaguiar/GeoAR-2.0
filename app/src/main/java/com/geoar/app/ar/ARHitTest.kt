package com.geoar.app.ar

import android.view.MotionEvent
import com.google.ar.core.*
import com.google.ar.core.exceptions.CameraNotAvailableException

/**
 * Utilitário para testes de hit em ARCore
 */
class ARHitTest(private val frame: Frame) {
    
    /**
     * Realiza hit test no evento de toque
     */
    fun performHitTest(event: MotionEvent, session: Session): List<HitResult>? {
        return try {
            frame.hitTest(event.x, event.y)
        } catch (e: CameraNotAvailableException) {
            null
        }
    }
    
    /**
     * Filtra hit results para apenas planos
     */
    fun filterForPlanes(hitResults: List<HitResult>): List<HitResult> {
        return hitResults.filter { hit ->
            val trackable = hit.trackable
            trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)
        }
    }
    
    /**
     * Obtém o hit result mais próximo do usuário
     */
    fun getClosestHit(hitResults: List<HitResult>): HitResult? {
        return hitResults.minByOrNull { it.distance }
    }
    
    /**
     * Verifica se existe um plano detectado
     */
    fun hasDetectedPlane(): Boolean {
        return frame.getTrackables(Plane::class.java).any { plane ->
            plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING && plane.trackingState == Trackable.TrackingState.TRACKING
        }
    }
}

