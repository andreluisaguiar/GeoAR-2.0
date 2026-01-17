package com.geoar.app.ui.ar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geoar.app.ar.ARSessionManager
import com.geoar.app.data.ProgressRepository

class ARViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ARViewModel::class.java)) {
            val sessionManager = ARSessionManager(context)
            val progressRepository = ProgressRepository(context)
            @Suppress("UNCHECKED_CAST")
            return ARViewModel(sessionManager, progressRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

