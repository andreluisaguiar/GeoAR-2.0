using System.Collections.Generic;

namespace GeoAR
{
    public static class DefaultQuestions
    {
        public static List<GeometryQuestion> GetAll()
        {
            return new List<GeometryQuestion>
            {
                new GeometryQuestion
                {
                    shapeName = "Cubo",
                    question = "Quantas faces tem um cubo?",
                    options = new []{ "4", "6", "8" },
                    correctIndex = 1
                },
                new GeometryQuestion
                {
                    shapeName = "Esfera",
                    question = "A esfera possui arestas?",
                    options = new []{ "Sim", "Não", "Depende" },
                    correctIndex = 1
                },
                new GeometryQuestion
                {
                    shapeName = "Cilindro",
                    question = "Qual é a forma da base de um cilindro?",
                    options = new []{ "Quadrado", "Triângulo", "Círculo" },
                    correctIndex = 2
                },
                new GeometryQuestion
                {
                    shapeName = "Cone",
                    question = "Quantos vértices tem um cone?",
                    options = new []{ "0", "1", "2" },
                    correctIndex = 1
                },
                new GeometryQuestion
                {
                    shapeName = "Pirâmide Quadrangular",
                    question = "Quantas faces triangulares tem esta pirâmide?",
                    options = new []{ "2", "3", "4" },
                    correctIndex = 2
                },
                new GeometryQuestion
                {
                    shapeName = "Prisma Triangular",
                    question = "Quantas arestas tem um prisma triangular?",
                    options = new []{ "9", "12", "6" },
                    correctIndex = 0
                },
                new GeometryQuestion
                {
                    shapeName = "Dodecaedro",
                    question = "Quantas faces tem um dodecaedro?",
                    options = new []{ "10", "12", "14" },
                    correctIndex = 1
                },
                new GeometryQuestion
                {
                    shapeName = "Toro",
                    question = "O toro é considerado um poliedro?",
                    options = new []{ "Sim", "Não", "Às vezes" },
                    correctIndex = 1
                },
            };
        }
    }
}
