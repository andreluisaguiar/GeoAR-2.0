using System;
using UnityEngine;
using UnityEngine.UI;

namespace GeoAR
{
    public class QuizManager : MonoBehaviour
    {
        public CanvasGroup panel;
        public Text questionText;
        public Button optionA;
        public Button optionB;
        public Button optionC;
        public Text feedbackText;
        public Text scoreText;

        private Action<int> onAnswerSelected;

        private void Awake()
        {
            Hide();
        }

        public void ShowQuestion(GeometryQuestion q, Action<int> callback)
        {
            onAnswerSelected = callback;
            Show();

            questionText.text = q.question;

            SetButton(optionA, q.options[0], 0);
            SetButton(optionB, q.options[1], 1);
            SetButton(optionC, q.options[2], 2);

            if (feedbackText != null)
                feedbackText.gameObject.SetActive(false);
        }

        public void ShowFeedback(bool correct)
        {
            if (feedbackText == null) return;
            feedbackText.gameObject.SetActive(true);
            feedbackText.text = correct ? "Correto!" : "Incorreto!";
            feedbackText.color = correct ? Color.green : Color.red;
        }

        public void ShowEnd(int score, int total)
        {
            Show();
            if (questionText != null)
                questionText.text = $"Fim! Pontuação: {score}/{total}";
            if (optionA != null) optionA.gameObject.SetActive(false);
            if (optionB != null) optionB.gameObject.SetActive(false);
            if (optionC != null) optionC.gameObject.SetActive(false);
        }

        public void Hide()
        {
            if (panel != null)
            {
                panel.alpha = 0f;
                panel.interactable = false;
                panel.blocksRaycasts = false;
            }
        }

        private void Show()
        {
            if (panel != null)
            {
                panel.alpha = 1f;
                panel.interactable = true;
                panel.blocksRaycasts = true;
            }

            if (optionA != null) optionA.gameObject.SetActive(true);
            if (optionB != null) optionB.gameObject.SetActive(true);
            if (optionC != null) optionC.gameObject.SetActive(true);
        }

        private void SetButton(Button button, string label, int index)
        {
            if (button == null) return;
            var text = button.GetComponentInChildren<Text>();
            if (text != null) text.text = label;

            button.onClick.RemoveAllListeners();
            button.onClick.AddListener(() => onAnswerSelected?.Invoke(index));
        }
    }
}
