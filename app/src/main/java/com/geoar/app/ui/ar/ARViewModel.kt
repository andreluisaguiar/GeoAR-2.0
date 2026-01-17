package com.geoar.app.ui.ar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geoar.app.ar.ARSessionManager
import com.geoar.app.data.ProgressRepository
import com.geoar.app.geometry.GeometryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ARViewModel(
    private val sessionManager: ARSessionManager,
    private val progressRepository: ProgressRepository
) : ViewModel() {
    
    private val _selectedGeometry = MutableStateFlow<GeometryType?>(null)
    val selectedGeometry: StateFlow<GeometryType?> = _selectedGeometry
    
    private val _isARReady = MutableStateFlow(false)
    val isARReady: StateFlow<Boolean> = _isARReady
    
    private val _arStatus = MutableStateFlow<ARStatus>(ARStatus.INITIALIZING)
    val arStatus: StateFlow<ARStatus> = _arStatus
    
    private val _placedObjects = MutableStateFlow<List<PlacedObject>>(emptyList())
    val placedObjects: StateFlow<List<PlacedObject>> = _placedObjects
    
    init {
        checkARSupport()
    }
    
    private fun checkARSupport() {
        val availability = sessionManager.checkAvailability()
        _arStatus.value = when (availability) {
            ARSessionManager.ARCoreAvailability.INSTALLED -> {
                _isARReady.value = true
                ARStatus.READY
            }
            ARSessionManager.ARCoreAvailability.NOT_INSTALLED -> ARStatus.NEEDS_INSTALL
            ARSessionManager.ARCoreAvailability.NOT_SUPPORTED -> ARStatus.NOT_SUPPORTED
            ARSessionManager.ARCoreAvailability.NEEDS_UPDATE -> ARStatus.NEEDS_UPDATE
            ARSessionManager.ARCoreAvailability.CHECKING -> ARStatus.CHECKING
            ARSessionManager.ARCoreAvailability.ERROR -> ARStatus.ERROR
        }
    }
    
    fun initializeARSession() {
        viewModelScope.launch {
            try {
                sessionManager.initializeSession()
                _arStatus.value = ARStatus.ACTIVE
            } catch (e: Exception) {
                _arStatus.value = ARStatus.ERROR
                e.printStackTrace()
            }
        }
    }
    
    fun selectGeometry(type: GeometryType) {
        _selectedGeometry.value = type
    }
    
    fun placeObject(pose: FloatArray) {
        val geometry = _selectedGeometry.value ?: return
        val newObject = PlacedObject(
            id = System.currentTimeMillis(),
            geometryType = geometry,
            position = pose,
            scale = 1.0f,
            isSelected = false
        )
        _placedObjects.value = _placedObjects.value + newObject
        incrementShapeCount()
    }
    
    fun deleteSelectedObject() {
        val selectedId = _placedObjects.value.find { it.isSelected }?.id
        if (selectedId != null) {
            _placedObjects.value = _placedObjects.value.filter { it.id != selectedId }
        }
    }
    
    fun selectObject(id: Long) {
        _placedObjects.value = _placedObjects.value.map {
            it.copy(isSelected = it.id == id)
        }
    }
    
    fun deselectAll() {
        _placedObjects.value = _placedObjects.value.map { it.copy(isSelected = false) }
    }
    
    fun clearAll() {
        _placedObjects.value = emptyList()
    }
    
    private fun incrementShapeCount() {
        viewModelScope.launch {
            val currentProgress = progressRepository.loadProgress()
            val updatedProgress = currentProgress.incrementShapesPlaced()
            progressRepository.saveProgress(updatedProgress)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        sessionManager.resetSession()
    }
}

enum class ARStatus {
    INITIALIZING,
    CHECKING,
    READY,
    ACTIVE,
    NEEDS_INSTALL,
    NEEDS_UPDATE,
    NOT_SUPPORTED,
    ERROR
}

data class PlacedObject(
    val id: Long,
    val geometryType: GeometryType,
    val position: FloatArray,
    val scale: Float,
    val isSelected: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as PlacedObject
        
        if (id != other.id) return false
        if (geometryType != other.geometryType) return false
        if (!position.contentEquals(other.position)) return false
        if (scale != other.scale) return false
        if (isSelected != other.isSelected) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + geometryType.hashCode()
        result = 31 * result + position.contentHashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}

