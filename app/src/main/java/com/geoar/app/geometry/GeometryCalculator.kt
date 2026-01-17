package com.geoar.app.geometry

import kotlin.math.PI
import kotlin.math.pow

/**
 * Calculadora de propriedades geométricas
 */
object GeometryCalculator {
    
    fun calculateVolume(type: GeometryType, vararg dimensions: Float): Float {
        return when (type) {
            GeometryType.CUBE -> dimensions[0].pow(3)
            GeometryType.SPHERE -> (4f / 3f) * PI.toFloat() * dimensions[0].pow(3)
            GeometryType.CYLINDER -> PI.toFloat() * dimensions[0].pow(2) * dimensions[1]
            GeometryType.CONE -> (1f / 3f) * PI.toFloat() * dimensions[0].pow(2) * dimensions[1]
            GeometryType.PYRAMID -> (1f / 3f) * dimensions[0] * dimensions[1] * dimensions[2]
            else -> 0f // Formas 2D não têm volume
        }
    }
    
    fun calculateArea(type: GeometryType, vararg dimensions: Float): Float {
        return when (type) {
            GeometryType.SQUARE -> dimensions[0].pow(2)
            GeometryType.CIRCLE -> PI.toFloat() * dimensions[0].pow(2)
            GeometryType.TRIANGLE -> 0.5f * dimensions[0] * dimensions[1]
            GeometryType.CUBE -> 6f * dimensions[0].pow(2)
            GeometryType.CYLINDER -> 2f * PI.toFloat() * dimensions[0] * dimensions[1]
            GeometryType.SPHERE -> 4f * PI.toFloat() * dimensions[0].pow(2)
            else -> 0f
        }
    }
    
    fun calculatePerimeter(type: GeometryType, vararg dimensions: Float): Float {
        return when (type) {
            GeometryType.SQUARE -> 4f * dimensions[0]
            GeometryType.CIRCLE -> 2f * PI.toFloat() * dimensions[0]
            GeometryType.TRIANGLE -> dimensions[0] + dimensions[1] + dimensions[2]
            else -> 0f
        }
    }
    
    fun getDimensions(type: GeometryType): Int {
        return when (type) {
            GeometryType.CUBE -> 1
            GeometryType.SPHERE -> 1
            GeometryType.CYLINDER -> 2
            GeometryType.CONE -> 2
            GeometryType.PYRAMID -> 3
            GeometryType.SQUARE -> 1
            GeometryType.CIRCLE -> 1
            GeometryType.TRIANGLE -> 3
        }
    }
}

