package com.geoar.app.data

import kotlinx.serialization.Serializable

/**
 * Modelo de dados para progresso do usuário
 */
@Serializable
data class UserProgress(
    val currentLevel: Int = 1,
    val completedModules: Set<String> = emptySet(),
    val scores: Map<String, Int> = emptyMap(),
    val unlockedAchievements: List<String> = emptyList(),
    val totalPlayTime: Long = 0,
    val shapesPlaced: Int = 0,
    val interactionsCount: Int = 0
) {
    fun addCompletedModule(moduleId: String): UserProgress {
        return copy(completedModules = completedModules + moduleId)
    }
    
    fun addScore(key: String, score: Int): UserProgress {
        return copy(scores = scores + (key to score))
    }
    
    fun addAchievement(achievement: String): UserProgress {
        return copy(unlockedAchievements = unlockedAchievements + achievement)
    }
    
    fun incrementShapesPlaced(): UserProgress {
        return copy(shapesPlaced = shapesPlaced + 1)
    }
    
    fun incrementInteractions(): UserProgress {
        return copy(interactionsCount = interactionsCount + 1)
    }
}

