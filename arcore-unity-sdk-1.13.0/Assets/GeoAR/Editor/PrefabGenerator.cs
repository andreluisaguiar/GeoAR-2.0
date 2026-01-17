#if UNITY_EDITOR
using UnityEditor;
using UnityEngine;

namespace GeoAR.EditorTools
{
    public static class PrefabGenerator
    {
        private const string PrefabsDir = "Assets/GeoAR/Prefabs";
        private const string ResourcesPrefabsDir = "Assets/GeoAR/Resources/Prefabs";

        [MenuItem("GeoAR/Gerar Prefabs Básicos")]
        public static void GenerateBasicPrefabs()
        {
            EnsureDir(PrefabsDir);
            EnsureDir(ResourcesPrefabsDir);

            SavePrefab(PrefabsDir, "Cubo.prefab", GameObject.CreatePrimitive(PrimitiveType.Cube));
            SavePrefab(PrefabsDir, "Esfera.prefab", GameObject.CreatePrimitive(PrimitiveType.Sphere));
            SavePrefab(PrefabsDir, "Cilindro.prefab", GameObject.CreatePrimitive(PrimitiveType.Cylinder));

            SavePrefab(PrefabsDir, "Cone.prefab", RuntimeShapeLibrary.CreateCone());
            SavePrefab(PrefabsDir, "PiramideQuadrangular.prefab", RuntimeShapeLibrary.CreateSquarePyramid());
            SavePrefab(PrefabsDir, "PrismaTriangular.prefab", RuntimeShapeLibrary.CreateTriangularPrism());
            SavePrefab(PrefabsDir, "Toro.prefab", RuntimeShapeLibrary.CreateTorus());

            // Dodecaedro placeholder com esfera; troque depois por modelo
            SavePrefab(PrefabsDir, "Dodecaedro.prefab", GameObject.CreatePrimitive(PrimitiveType.Sphere));

            // Também salve duplicatas em Resources para carregamento em runtime
            SavePrefab(ResourcesPrefabsDir, "Cubo.prefab", GameObject.CreatePrimitive(PrimitiveType.Cube));
            SavePrefab(ResourcesPrefabsDir, "Esfera.prefab", GameObject.CreatePrimitive(PrimitiveType.Sphere));
            SavePrefab(ResourcesPrefabsDir, "Cilindro.prefab", GameObject.CreatePrimitive(PrimitiveType.Cylinder));
            SavePrefab(ResourcesPrefabsDir, "Cone.prefab", RuntimeShapeLibrary.CreateCone());
            SavePrefab(ResourcesPrefabsDir, "PiramideQuadrangular.prefab", RuntimeShapeLibrary.CreateSquarePyramid());
            SavePrefab(ResourcesPrefabsDir, "PrismaTriangular.prefab", RuntimeShapeLibrary.CreateTriangularPrism());
            SavePrefab(ResourcesPrefabsDir, "Toro.prefab", RuntimeShapeLibrary.CreateTorus());
            SavePrefab(ResourcesPrefabsDir, "Dodecaedro.prefab", GameObject.CreatePrimitive(PrimitiveType.Sphere));

            AssetDatabase.SaveAssets();
            AssetDatabase.Refresh();
            EditorUtility.DisplayDialog("GeoAR", "Prefabs básicos gerados em Assets/GeoAR/Prefabs.", "OK");
        }

        static void EnsureDir(string path)
        {
            if (!AssetDatabase.IsValidFolder(path))
            {
                var parts = path.Split('/');
                string current = parts[0];
                for (int i = 1; i < parts.Length; i++)
                {
                    string next = current + "/" + parts[i];
                    if (!AssetDatabase.IsValidFolder(next))
                        AssetDatabase.CreateFolder(current, parts[i]);
                    current = next;
                }
            }
        }

        static void SavePrefab(string dir, string fileName, GameObject go)
        {
            go.SetActive(true);
            string fullPath = System.IO.Path.Combine(dir, fileName).Replace("\\", "/");
            var prefab = PrefabUtility.SaveAsPrefabAsset(go, fullPath);
            Object.DestroyImmediate(go);
            if (prefab == null)
                Debug.LogError("Falha ao criar prefab: " + fileName);
        }
    }
}
#endif
