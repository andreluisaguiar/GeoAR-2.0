package com.geoar.app.geometry

/**
 * Tipos de formas geométricas suportadas
 */
enum class GeometryType(
    val displayName: String,
    val module: ModuleType,
    val description: String,
    val formula: String
) {
    CUBE(
        "Cubo",
        ModuleType.BASIC,
        "Um cubo é uma forma tridimensional com 6 faces quadradas idênticas",
        "Volume: a³"
    ),
    SPHERE(
        "Esfera",
        ModuleType.BASIC,
        "Uma esfera é uma forma tridimensional perfeitamente redonda",
        "Volume: ⁴/₃πr³"
    ),
    CYLINDER(
        "Cilindro",
        ModuleType.BASIC,
        "Um cilindro é uma forma com duas bases circulares paralelas",
        "Volume: πr²h"
    ),
    CONE(
        "Cone",
        ModuleType.INTERMEDIATE,
        "Um cone é uma forma que se estreita de uma base circular a um ponto",
        "Volume: ⅓πr²h"
    ),
    PYRAMID(
        "Pirâmide",
        ModuleType.INTERMEDIATE,
        "Uma pirâmide é uma forma com uma base poligonal e faces triangulares",
        "Volume: ⅓lwh"
    ),
    SQUARE(
        "Quadrado",
        ModuleType.BASIC,
        "Um quadrado é uma forma com 4 lados iguais",
        "Área: a²"
    ),
    CIRCLE(
        "Círculo",
        ModuleType.BASIC,
        "Um círculo é uma forma redonda perfeita",
        "Área: πr²"
    ),
    TRIANGLE(
        "Triângulo",
        ModuleType.BASIC,
        "Um triângulo é uma forma com 3 lados",
        "Área: ½bh"
    );
    
    companion object {
        fun getByModule(module: ModuleType): List<GeometryType> {
            return values().filter { it.module == module }
        }
    }
}

enum class ModuleType(val displayName: String) {
    BASIC("Básico"),
    INTERMEDIATE("Intermediário"),
    ADVANCED("Avançado")
}

