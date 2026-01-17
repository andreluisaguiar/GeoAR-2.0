using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using UnityEngine.XR.ARFoundation;

namespace GeoAR
{
    public static class BootstrapARScene
    {
        [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.AfterSceneLoad)]
        private static void Init()
        {
            // AR Session
            var arSessionGO = new GameObject("AR Session");
            arSessionGO.AddComponent<ARSession>();

            // AR Session Origin
            var originGO = new GameObject("AR Session Origin");
            var origin = originGO.AddComponent<ARSessionOrigin>();

            // AR Camera
            var cameraGO = new GameObject("AR Camera");
            var cam = cameraGO.AddComponent<Camera>();
            cameraGO.tag = "MainCamera";
            cameraGO.transform.SetParent(originGO.transform);
            cam.clearFlags = CameraClearFlags.SolidColor;
            cam.backgroundColor = Color.black;
            cameraGO.AddComponent<ARCameraManager>();
            cameraGO.AddComponent<ARCameraBackground>();

            // Managers
            var raycast = originGO.AddComponent<ARRaycastManager>();
            var planes = originGO.AddComponent<ARPlaneManager>();
            planes.requestedDetectionMode = UnityEngine.XR.ARSubsystems.PlaneDetectionMode.Horizontal;

            // Placement Controller
            var placement = originGO.AddComponent<ARPlacementController>();
            placement.raycastManager = raycast;
            placement.planeManager = planes;
            placement.arCamera = cam;

            // UI Canvas
            var canvasGO = new GameObject("GeoAR Canvas");
            var canvas = canvasGO.AddComponent<Canvas>();
            canvas.renderMode = RenderMode.ScreenSpaceOverlay;
            canvasGO.AddComponent<CanvasScaler>();
            canvasGO.AddComponent<GraphicRaycaster>();

            // EventSystem
            if (Object.FindObjectOfType<EventSystem>() == null)
            {
                var es = new GameObject("EventSystem");
                es.AddComponent<EventSystem>();
                es.AddComponent<StandaloneInputModule>();
            }

            // Panel
            var panelGO = new GameObject("Panel");
            panelGO.transform.SetParent(canvasGO.transform, false);
            var panelRect = panelGO.AddComponent<RectTransform>();
            panelRect.anchorMin = new Vector2(0.1f, 0.1f);
            panelRect.anchorMax = new Vector2(0.9f, 0.4f);
            panelRect.offsetMin = Vector2.zero;
            panelRect.offsetMax = Vector2.zero;
            var panelImage = panelGO.AddComponent<Image>();
            panelImage.color = new Color(0f, 0f, 0f, 0.5f);
            var panelGroup = panelGO.AddComponent<CanvasGroup>();

            // Question Text
            var questionGO = new GameObject("QuestionText");
            questionGO.transform.SetParent(panelGO.transform, false);
            var qRect = questionGO.AddComponent<RectTransform>();
            qRect.anchorMin = new Vector2(0.05f, 0.65f);
            qRect.anchorMax = new Vector2(0.95f, 0.95f);
            var qText = questionGO.AddComponent<Text>();
            qText.text = "Pergunta";
            qText.color = Color.white;
            qText.alignment = TextAnchor.MiddleCenter;
            qText.font = Resources.GetBuiltinResource<Font>("Arial.ttf");
            qText.resizeTextForBestFit = true;

            // Feedback Text
            var feedbackGO = new GameObject("FeedbackText");
            feedbackGO.transform.SetParent(panelGO.transform, false);
            var fRect = feedbackGO.AddComponent<RectTransform>();
            fRect.anchorMin = new Vector2(0.05f, 0.05f);
            fRect.anchorMax = new Vector2(0.95f, 0.25f);
            var fText = feedbackGO.AddComponent<Text>();
            fText.text = "";
            fText.color = Color.white;
            fText.alignment = TextAnchor.MiddleCenter;
            fText.font = Resources.GetBuiltinResource<Font>("Arial.ttf");
            fText.resizeTextForBestFit = true;

            // Buttons helper
            Button MakeButton(string name, Vector2 min, Vector2 max)
            {
                var bGO = new GameObject(name);
                bGO.transform.SetParent(panelGO.transform, false);
                var rect = bGO.AddComponent<RectTransform>();
                rect.anchorMin = min;
                rect.anchorMax = max;
                var img = bGO.AddComponent<Image>();
                img.color = new Color(1f, 1f, 1f, 0.2f);
                var btn = bGO.AddComponent<Button>();

                var tGO = new GameObject("Text");
                tGO.transform.SetParent(bGO.transform, false);
                var tRect = tGO.AddComponent<RectTransform>();
                tRect.anchorMin = new Vector2(0.1f, 0.1f);
                tRect.anchorMax = new Vector2(0.9f, 0.9f);
                var t = tGO.AddComponent<Text>();
                t.text = "Opção";
                t.color = Color.white;
                t.alignment = TextAnchor.MiddleCenter;
                t.font = Resources.GetBuiltinResource<Font>("Arial.ttf");
                t.resizeTextForBestFit = true;
                return btn;
            }

            var optionA = MakeButton("OptionA", new Vector2(0.05f, 0.55f), new Vector2(0.95f, 0.6f));
            var optionB = MakeButton("OptionB", new Vector2(0.05f, 0.45f), new Vector2(0.95f, 0.5f));
            var optionC = MakeButton("OptionC", new Vector2(0.05f, 0.35f), new Vector2(0.95f, 0.4f));

            // QuizManager
            var quizGO = new GameObject("QuizManager");
            var quiz = quizGO.AddComponent<QuizManager>();
            quiz.panel = panelGroup;
            quiz.questionText = qText;
            quiz.optionA = optionA;
            quiz.optionB = optionB;
            quiz.optionC = optionC;
            quiz.feedbackText = fText;

            // GameManager
            var gmGO = new GameObject("GameManager");
            var gm = gmGO.AddComponent<GameManager>();
            gm.quizManager = quiz;
            gm.placementController = placement;
        }
    }
}
