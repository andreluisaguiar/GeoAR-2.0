package com.geoar.app.education

import com.geoar.app.geometry.GeometryType

/**
 * Conteúdo educativo para cada forma geométrica
 */
data class EducationalContent(
    val geometryType: GeometryType,
    val title: String,
    val description: String,
    val properties: List<Property>,
    val formulas: List<Formula>,
    val examples: List<Example>
)

data class Property(val name: String, val value: String)
data class Formula(val name: String, val formula: String, val explanation: String)
data class Example(val description: String, val solution: String)

object EducationalContentProvider {
    
    fun getContentFor(geometryType: GeometryType): EducationalContent {
        return when (geometryType) {
            GeometryType.CUBE -> EducationalContent(
                geometryType = GeometryType.CUBE,
                title = "Cubo",
                description = "Um cubo é um sólido geométrico com 6 faces quadradas idênticas, 12 arestas e 8 vértices.",
                properties = listOf(
                    Property("Faces", "6 quadradas"),
                    Property("Arestas", "12"),
                    Property("Vértices", "8")
                ),
                formulas = listOf(
                    Formula(
                        "Volume",
                        "V = a³",
                        "O volume de um cubo é igual ao comprimento da aresta ao cubo."
                    ),
                    Formula(
                        "Área Total",
                        "A = 6a²",
                        "A área total é a soma das áreas de todas as seis faces."
                    )
                ),
                examples = listOf(
                    Example(
                        "Cubo com aresta de 5cm",
                        "Volume: 5³ = 125 cm³"
                    )
                )
            )
            
            GeometryType.SPHERE -> EducationalContent(
                geometryType = GeometryType.SPHERE,
                title = "Esfera",
                description = "Uma esfera é uma forma tridimensional perfeitamente redonda onde todos os pontos estão equidistantes do centro.",
                properties = listOf(
                    Property("Raio", "r"),
                    Property("Superfície", "Curva contínua"),
                    Property("Dimensões", "3D")
                ),
                formulas = listOf(
                    Formula(
                        "Volume",
                        "V = ⁴/₃πr³",
                        "O volume de uma esfera é calculado usando o raio elevado ao cubo."
                    ),
                    Formula(
                        "Área da Superfície",
                        "A = 4πr²",
                        "A área da superfície de uma esfera é quatro vezes o produto de π pelo raio ao quadrado."
                    )
                ),
                examples = listOf(
                    Example(
                        "Esfera com raio de 3cm",
                        "Volume: ⁴/₃π(3)³ = 36π cm³"
                    )
                )
            )
            
            GeometryType.CYLINDER -> EducationalContent(
                geometryType = GeometryType.CYLINDER,
                title = "Cilindro",
                description = "Um cilindro é uma forma com duas bases circulares paralelas conectadas por uma superfície lateral curva.",
                properties = listOf(
                    Property("Bases", "2 círculos"),
                    Property("Altura", "h"),
                    Property("Raio", "r")
                ),
                formulas = listOf(
                    Formula(
                        "Volume",
                        "V = πr²h",
                        "O volume é igual à área da base (πr²) multiplicada pela altura."
                    ),
                    Formula(
                        "Área Lateral",
                        "A = 2πrh",
                        "A área lateral é o perímetro da base multiplicado pela altura."
                    )
                ),
                examples = listOf(
                    Example(
                        "Cilindro com r=4cm, h=10cm",
                        "Volume: π(4)²(10) = 160π cm³"
                    )
                )
            )
            
            GeometryType.CONE -> EducationalContent(
                geometryType = GeometryType.CONE,
                title = "Cone",
                description = "Um cone é uma forma que se estreita de uma base circular a um ponto chamado vértice.",
                properties = listOf(
                    Property("Vértice", "1 ponto"),
                    Property("Base", "1 círculo"),
                    Property("Altura", "h")
                ),
                formulas = listOf(
                    Formula(
                        "Volume",
                        "V = ⅓πr²h",
                        "O volume de um cone é um terço do volume de um cilindro com as mesmas dimensões."
                    ),
                    Formula(
                        "Área Lateral",
                        "A = πrl",
                        "Onde l é a geratriz do cone."
                    )
                ),
                examples = listOf(
                    Example(
                        "Cone com r=6cm, h=8cm",
                        "Volume: ⅓π(6)²(8) = 96π cm³"
                    )
                )
            )
            
            GeometryType.PYRAMID -> EducationalContent(
                geometryType = GeometryType.PYRAMID,
                title = "Pirâmide",
                description = "Uma pirâmide é uma forma com uma base poligonal e faces triangulares que convergem para um vértice.",
                properties = listOf(
                    Property("Vértice", "1"),
                    Property("Faces Laterais", "Triangulares"),
                    Property("Base", "Poligonal")
                ),
                formulas = listOf(
                    Formula(
                        "Volume",
                        "V = ⅓Bh",
                        "Onde B é a área da base e h é a altura."
                    ),
                    Formula(
                        "Área Total",
                        "A = B + AL",
                        "Soma da área da base com a área lateral."
                    )
                ),
                examples = listOf(
                    Example(
                        "Pirâmide quadrada com base 5cm, h=12cm",
                        "Volume: ⅓(5²)(12) = 100 cm³"
                    )
                )
            )
            
            GeometryType.SQUARE -> EducationalContent(
                geometryType = GeometryType.SQUARE,
                title = "Quadrado",
                description = "Um quadrado é uma forma com 4 lados iguais e 4 ângulos retos.",
                properties = listOf(
                    Property("Lados", "4 iguais"),
                    Property("Ângulos", "4 retos (90°)"),
                    Property("Simetria", "4 eixos")
                ),
                formulas = listOf(
                    Formula(
                        "Área",
                        "A = a²",
                        "O lado ao quadrado."
                    ),
                    Formula(
                        "Perímetro",
                        "P = 4a",
                        "Quatro vezes o comprimento do lado."
                    )
                ),
                examples = listOf(
                    Example(
                        "Quadrado com lado de 7cm",
                        "Área: 7² = 49 cm²"
                    )
                )
            )
            
            GeometryType.CIRCLE -> EducationalContent(
                geometryType = GeometryType.CIRCLE,
                title = "Círculo",
                description = "Um círculo é uma forma redonda onde todos os pontos estão à mesma distância do centro.",
                properties = listOf(
                    Property("Raio", "r"),
                    Property("Diâmetro", "2r"),
                    Property("Circunferência", "2πr")
                ),
                formulas = listOf(
                    Formula(
                        "Área",
                        "A = πr²",
                        "Pi vezes o raio ao quadrado."
                    ),
                    Formula(
                        "Perímetro",
                        "P = 2πr",
                        "Também chamado de circunferência."
                    )
                ),
                examples = listOf(
                    Example(
                        "Círculo com raio de 4cm",
                        "Área: π(4)² = 16π cm²"
                    )
                )
            )
            
            GeometryType.TRIANGLE -> EducationalContent(
                geometryType = GeometryType.TRIANGLE,
                title = "Triângulo",
                description = "Um triângulo é uma forma com 3 lados e 3 ângulos.",
                properties = listOf(
                    Property("Lados", "3"),
                    Property("Ângulos", "3 (soma = 180°)"),
                    Property("Vértices", "3")
                ),
                formulas = listOf(
                    Formula(
                        "Área",
                        "A = ½bh",
                        "Base vezes altura dividido por 2."
                    ),
                    Formula(
                        "Perímetro",
                        "P = a + b + c",
                        "Soma dos três lados."
                    )
                ),
                examples = listOf(
                    Example(
                        "Triângulo com base 8cm e altura 6cm",
                        "Área: ½(8)(6) = 24 cm²"
                    )
                )
            )
        }
    }
}

