using System;
using System.Collections.Generic;
using UnityEngine;

namespace GeoAR
{
    public class GameManager : MonoBehaviour
    {
        [Serializable]
        public struct ShapePrefab
        {
            public string shapeName;
            public GameObject prefab;
        }

        public QuizManager quizManager;
        public ARPlacementController placementController;
        public List<ShapePrefab> shapePrefabs = new List<ShapePrefab>();

        private List<GeometryQuestion> questions;
        private int currentIndex = 0;
        private int score = 0;

        private void Awake()
        {
            questions = DefaultQuestions.GetAll();
            TryAutoPopulatePrefabsFromResources();
        }

        private void Start()
        {
            StartLevel(currentIndex);
        }

        private void StartLevel(int index)
        {
            var q = questions[index];
            var prefab = GetPrefabFor(q.shapeName);
            placementController.PrepareForPlacement(prefab, OnShapeTapped);
            quizManager.Hide();
        }

        private GameObject GetPrefabFor(string shapeName)
        {
            foreach (var sp in shapePrefabs)
            {
                if (string.Equals(sp.shapeName, shapeName, StringComparison.OrdinalIgnoreCase))
                    return sp.prefab;
            }

            // Fallback: cria um "prefab" runtime com base no nome da forma
            GameObject template;
            switch (shapeName.Trim())
            {
                case "Cubo":
                    template = GameObject.CreatePrimitive(PrimitiveType.Cube);
                    break;
                case "Esfera":
                    template = GameObject.CreatePrimitive(PrimitiveType.Sphere);
                    break;
                case "Cilindro":
                    template = GameObject.CreatePrimitive(PrimitiveType.Cylinder);
                    break;
                case "Cone":
                    template = RuntimeShapeLibrary.CreateCone();
                    break;
                case "Pirâmide Quadrangular":
                    template = RuntimeShapeLibrary.CreateSquarePyramid();
                    break;
                case "Prisma Triangular":
                    template = RuntimeShapeLibrary.CreateTriangularPrism();
                    break;
                case "Dodecaedro":
                    // Placeholder: esfera até importarmos um modelo.
                    template = GameObject.CreatePrimitive(PrimitiveType.Sphere);
                    break;
                case "Toro":
                    template = RuntimeShapeLibrary.CreateTorus();
                    break;
                default:
                    template = GameObject.CreatePrimitive(PrimitiveType.Cube);
                    break;
            }

            template.name = $"Template_{shapeName}";
            template.hideFlags = HideFlags.HideAndDontSave;
            template.SetActive(false);
            Debug.LogWarning($"Prefab não encontrado para forma: {shapeName}. Usando forma gerada em runtime.");
            return template;
        }

        private void TryAutoPopulatePrefabsFromResources()
        {
            if (questions == null) return;

            // Se já houver mapeamentos, não sobrescreve
            var existing = new HashSet<string>(StringComparer.OrdinalIgnoreCase);
            foreach (var sp in shapePrefabs)
                if (!string.IsNullOrEmpty(sp.shapeName)) existing.Add(sp.shapeName);

            foreach (var q in questions)
            {
                if (existing.Contains(q.shapeName)) continue;
                var resourceName = MapResourceName(q.shapeName);
                var go = Resources.Load<GameObject>("Prefabs/" + resourceName);
                if (go != null)
                {
                    shapePrefabs.Add(new ShapePrefab { shapeName = q.shapeName, prefab = go });
                    existing.Add(q.shapeName);
                    Debug.Log($"GeoAR: Prefab auto-carregado de Resources para '{q.shapeName}' → '{resourceName}'.");
                }
            }
        }

        private string MapResourceName(string shapeName)
        {
            // Normaliza nomes com acentos/espaços para os nomes dos prefabs gerados
            switch (shapeName.Trim())
            {
                case "Cubo": return "Cubo";
                case "Esfera": return "Esfera";
                case "Cilindro": return "Cilindro";
                case "Cone": return "Cone";
                case "Pirâmide Quadrangular": return "PiramideQuadrangular";
                case "Prisma Triangular": return "PrismaTriangular";
                case "Dodecaedro": return "Dodecaedro";
                case "Toro": return "Toro";
                default:
                    return shapeName.Replace(" ", "");
            }
        }

        private void OnShapeTapped()
        {
            quizManager.ShowQuestion(questions[currentIndex], OnAnswerSelected);
        }

        private void OnAnswerSelected(int selectedIndex)
        {
            var q = questions[currentIndex];
            bool correct = selectedIndex == q.correctIndex;
            quizManager.ShowFeedback(correct);

            if (correct)
            {
                score++;
                currentIndex++;

                if (currentIndex < questions.Count)
                {
                    StartLevel(currentIndex);
                }
                else
                {
                    quizManager.ShowEnd(score, questions.Count);
                    placementController.DisablePlacement();
                }
            }
        }
    }
}
