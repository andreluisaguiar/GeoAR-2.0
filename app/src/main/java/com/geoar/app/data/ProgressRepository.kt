package com.geoar.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Repositório para gerenciar progresso do usuário
 */
class ProgressRepository(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private const val PREFS_NAME = "geo_ar_prefs"
        private const val KEY_PROGRESS = "user_progress"
    }
    
    fun saveProgress(progress: UserProgress) {
        try {
            val progressJson = json.encodeToString(progress)
            prefs.edit().putString(KEY_PROGRESS, progressJson).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun loadProgress(): UserProgress {
        return try {
            val progressJson = prefs.getString(KEY_PROGRESS, null)
            if (progressJson != null) {
                json.decodeFromString<UserProgress>(progressJson)
            } else {
                UserProgress()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UserProgress()
        }
    }
    
    fun clearProgress() {
        prefs.edit().remove(KEY_PROGRESS).apply()
    }
}

