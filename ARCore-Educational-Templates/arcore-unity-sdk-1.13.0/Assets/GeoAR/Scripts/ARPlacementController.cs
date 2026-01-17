using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;
using UnityEngine.XR.ARSubsystems;

namespace GeoAR
{
    [RequireComponent(typeof(ARRaycastManager))]
    public class ARPlacementController : MonoBehaviour
    {
        public ARRaycastManager raycastManager;
        public ARPlaneManager planeManager;
        public Camera arCamera;

        private GameObject currentPrefab;
        private GameObject placedObject;
        private Action onShapeTapped;
        private static List<ARRaycastHit> hits = new List<ARRaycastHit>();

        private void Reset()
        {
            raycastManager = GetComponent<ARRaycastManager>();
            arCamera = Camera.main;
        }

        public void PrepareForPlacement(GameObject prefab, Action onTapped)
        {
            if (placedObject != null)
            {
                Destroy(placedObject);
                placedObject = null;
            }
            currentPrefab = prefab;
            onShapeTapped = onTapped;

            if (planeManager != null)
                planeManager.requestedDetectionMode = PlaneDetectionMode.Horizontal;
        }

        public void DisablePlacement()
        {
            currentPrefab = null;
        }

        private void Update()
        {
            if (currentPrefab == null)
                return;

            if (placedObject == null)
            {
                HandlePlacement();
            }
            else
            {
                HandleTapOnPlaced();
            }
        }

        private void HandlePlacement()
        {
            if (Input.touchCount == 0) return;
            var touch = Input.GetTouch(0);
            if (touch.phase != TouchPhase.Began) return;

            if (raycastManager.Raycast(touch.position, hits, TrackableType.PlaneWithinPolygon))
            {
                var hitPose = hits[0].pose;
                placedObject = Instantiate(currentPrefab, hitPose.position, hitPose.rotation);
                EnsureCollider(placedObject);
            }
        }

        private void HandleTapOnPlaced()
        {
            if (Input.touchCount == 0) return;
            var touch = Input.GetTouch(0);
            if (touch.phase != TouchPhase.Began) return;

            var ray = arCamera != null ? arCamera.ScreenPointToRay(touch.position) : Camera.main.ScreenPointToRay(touch.position);
            if (Physics.Raycast(ray, out var hit))
            {
                if (hit.transform != null && hit.transform.gameObject == placedObject)
                {
                    onShapeTapped?.Invoke();
                }
            }
        }

        private void EnsureCollider(GameObject obj)
        {
            if (obj.GetComponent<Collider>() == null)
            {
                var rend = obj.GetComponentInChildren<Renderer>();
                var collider = obj.AddComponent<BoxCollider>();
                if (rend != null)
                {
                    collider.center = rend.bounds.center - obj.transform.position;
                    collider.size = rend.bounds.size;
                }
            }
        }
    }
}
