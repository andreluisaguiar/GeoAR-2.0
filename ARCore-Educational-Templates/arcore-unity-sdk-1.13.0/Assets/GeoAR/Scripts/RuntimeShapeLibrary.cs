using UnityEngine;

namespace GeoAR
{
    public static class RuntimeShapeLibrary
    {
        static Material CreateMaterial(Color c)
        {
            var m = new Material(Shader.Find("Standard"));
            m.color = c;
            return m;
        }

        static GameObject CreateMeshObject(string name, Mesh mesh, Color color)
        {
            var go = new GameObject(name);
            var mf = go.AddComponent<MeshFilter>();
            var mr = go.AddComponent<MeshRenderer>();
            mf.sharedMesh = mesh;
            mr.sharedMaterial = CreateMaterial(color);

            var mc = go.AddComponent<MeshCollider>();
            mc.sharedMesh = mesh;
            mc.convex = true;

            go.hideFlags = HideFlags.HideAndDontSave;
            go.SetActive(false);
            return go;
        }

        public static GameObject CreateCone(float radius = 0.1f, float height = 0.2f, int segments = 24)
        {
            var mesh = new Mesh();
            var verts = new Vector3[segments + 2 + segments]; // base + apex + side ring
            var normals = new Vector3[verts.Length];
            var uvs = new Vector2[verts.Length];

            // Base center
            verts[0] = new Vector3(0, 0, 0);
            normals[0] = Vector3.down;
            uvs[0] = new Vector2(0.5f, 0.5f);

            // Base ring
            for (int i = 0; i < segments; i++)
            {
                float ang = (Mathf.PI * 2f) * i / segments;
                float x = Mathf.Cos(ang) * radius;
                float z = Mathf.Sin(ang) * radius;
                verts[1 + i] = new Vector3(x, 0, z);
                normals[1 + i] = Vector3.down;
                uvs[1 + i] = new Vector2((x / (radius * 2f)) + 0.5f, (z / (radius * 2f)) + 0.5f);
            }

            // Apex
            int apexIndex = 1 + segments;
            verts[apexIndex] = new Vector3(0, height, 0);
            normals[apexIndex] = Vector3.up;
            uvs[apexIndex] = new Vector2(0.5f, 0.5f);

            // Side ring duplicated for correct normals
            int sideStart = apexIndex + 1;
            for (int i = 0; i < segments; i++)
            {
                float ang = (Mathf.PI * 2f) * i / segments;
                float x = Mathf.Cos(ang) * radius;
                float z = Mathf.Sin(ang) * radius;
                verts[sideStart + i] = new Vector3(x, 0, z);
                Vector3 normal = Vector3.Normalize(new Vector3(x, radius / height, z));
                normals[sideStart + i] = normal;
                uvs[sideStart + i] = new Vector2((float)i / (segments - 1), 0);
            }

            // Triangles
            int triCount = segments * 3 + segments * 3;
            var tris = new int[triCount];
            int t = 0;

            // Base fan
            for (int i = 0; i < segments; i++)
            {
                int a = 0;
                int b = 1 + ((i + 1) % segments);
                int c = 1 + i;
                tris[t++] = a; tris[t++] = b; tris[t++] = c;
            }

            // Sides
            for (int i = 0; i < segments; i++)
            {
                int a = sideStart + i;
                int b = sideStart + ((i + 1) % segments);
                int c = apexIndex;
                tris[t++] = a; tris[t++] = c; tris[t++] = b;
            }

            mesh.vertices = verts;
            mesh.normals = normals;
            mesh.uv = uvs;
            mesh.triangles = tris;
            mesh.RecalculateBounds();
            return CreateMeshObject("Cone", mesh, new Color(1f, 0.6f, 0.2f));
        }

        public static GameObject CreateSquarePyramid(float baseSize = 0.2f, float height = 0.2f)
        {
            var mesh = new Mesh();
            float s = baseSize * 0.5f;
            Vector3[] v = new Vector3[]
            {
                new Vector3(-s,0,-s), // 0
                new Vector3(s,0,-s),  // 1
                new Vector3(s,0,s),   // 2
                new Vector3(-s,0,s),  // 3
                new Vector3(0,height,0), // apex 4
            };

            int[] t = new int[]
            {
                // Base
                0,1,2,
                0,2,3,
                // Sides
                0,4,1,
                1,4,2,
                2,4,3,
                3,4,0,
            };

            mesh.vertices = v;
            mesh.triangles = t;
            mesh.RecalculateNormals();
            mesh.RecalculateBounds();
            return CreateMeshObject("PiramideQuadrangular", mesh, new Color(0.9f, 0.3f, 0.3f));
        }

        public static GameObject CreateTriangularPrism(float baseSize = 0.2f, float length = 0.2f)
        {
            var mesh = new Mesh();
            float s = baseSize * 0.5f;
            float L = length;
            // Triangle in XZ, extruded on +Y
            Vector3 A = new Vector3(-s, 0, -s);
            Vector3 B = new Vector3(s, 0, -s);
            Vector3 C = new Vector3(0, 0, s);
            Vector3 Ay = A + Vector3.up * L;
            Vector3 By = B + Vector3.up * L;
            Vector3 Cy = C + Vector3.up * L;

            Vector3[] v = new Vector3[] { A, B, C, Ay, By, Cy };
            int[] t = new int[]
            {
                // Bottom triangle
                0,1,2,
                // Top triangle
                3,5,4,
                // Sides
                0,3,1,
                1,3,4,
                1,4,2,
                2,4,5,
                2,5,0,
                0,5,3,
            };

            mesh.vertices = v;
            mesh.triangles = t;
            mesh.RecalculateNormals();
            mesh.RecalculateBounds();
            return CreateMeshObject("PrismaTriangular", mesh, new Color(0.3f, 0.6f, 1f));
        }

        public static GameObject CreateTorus(float R = 0.15f, float r = 0.05f, int segments = 24, int tubeSegments = 16)
        {
            var mesh = new Mesh();
            int vCount = segments * tubeSegments;
            Vector3[] verts = new Vector3[vCount];
            Vector3[] normals = new Vector3[vCount];
            Vector2[] uvs = new Vector2[vCount];
            int[] tris = new int[segments * tubeSegments * 6];

            int vi = 0;
            for (int i = 0; i < segments; i++)
            {
                float u = (i / (float)segments) * Mathf.PI * 2f;
                Vector3 center = new Vector3(Mathf.Cos(u) * R, 0, Mathf.Sin(u) * R);
                Vector3 du = new Vector3(-Mathf.Sin(u), 0, Mathf.Cos(u));
                for (int j = 0; j < tubeSegments; j++)
                {
                    float v = (j / (float)tubeSegments) * Mathf.PI * 2f;
                    Vector3 offset = (Mathf.Cos(v) * du + Mathf.Sin(v) * Vector3.up) * r;
                    verts[vi] = center + offset;
                    normals[vi] = offset.normalized;
                    uvs[vi] = new Vector2(i / (float)segments, j / (float)tubeSegments);
                    vi++;
                }
            }

            int ti = 0;
            for (int i = 0; i < segments; i++)
            {
                int inext = (i + 1) % segments;
                for (int j = 0; j < tubeSegments; j++)
                {
                    int jnext = (j + 1) % tubeSegments;
                    int a = i * tubeSegments + j;
                    int b = inext * tubeSegments + j;
                    int c = inext * tubeSegments + jnext;
                    int d = i * tubeSegments + jnext;
                    tris[ti++] = a; tris[ti++] = b; tris[ti++] = c;
                    tris[ti++] = a; tris[ti++] = c; tris[ti++] = d;
                }
            }

            mesh.vertices = verts;
            mesh.normals = normals;
            mesh.uv = uvs;
            mesh.triangles = tris;
            mesh.RecalculateBounds();
            return CreateMeshObject("Toro", mesh, new Color(0.9f, 0.8f, 0.2f));
        }
    }
}
