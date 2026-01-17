using System.Linq;
using UnityEditor;
using UnityEditor.Build.Reporting;
using UnityEngine;

public static class GeoARBuild
{
    [MenuItem("GeoAR/Build/Android APK (Debug)")]
    public static void BuildAndroidApk()
    {
        var scenes = EditorBuildSettings.scenes.Where(s => s.enabled).Select(s => s.path).ToArray();
        if (scenes.Length == 0)
        {
            Debug.LogError("Nenhuma cena habilitada em Build Settings.");
            throw new System.Exception("Sem cenas para build.");
        }

        EditorUserBuildSettings.androidBuildSystem = AndroidBuildSystem.Gradle;
        EditorUserBuildSettings.development = true;
        EditorUserBuildSettings.exportAsGoogleAndroidProject = false;
        EditorUserBuildSettings.androidBuildType = AndroidBuildType.Debug;

#if UNITY_2021_2_OR_NEWER
        PlayerSettings.SetApplicationIdentifier(BuildTargetGroup.Android, "com.geoar.app");
#else
        PlayerSettings.applicationIdentifier = "com.geoar.app";
#endif
        PlayerSettings.Android.minSdkVersion = AndroidSdkVersions.AndroidApiLevel24;
#if UNITY_2022_1_OR_NEWER
        PlayerSettings.Android.targetSdkVersion = AndroidSdkVersions.AndroidApiLevelAuto;
#endif
        PlayerSettings.Android.targetArchitectures = AndroidArchitecture.ARM64;

        var buildPlayerOptions = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = "Builds/Android/GeoAR-debug.apk",
            target = BuildTarget.Android,
            options = BuildOptions.Development,
            targetGroup = BuildTargetGroup.Android
        };

        var report = BuildPipeline.BuildPlayer(buildPlayerOptions);
        var summary = report.summary;
        if (summary.result == BuildResult.Succeeded)
        {
            Debug.Log($"APK gerado: {buildPlayerOptions.locationPathName} (tamanho: {summary.totalSize} bytes)");
        }
        else
        {
            Debug.LogError($"Falha no build: {summary.result}");
            throw new System.Exception($"Build falhou: {summary.result}");
        }
    }

    [MenuItem("GeoAR/Build/Export Android Project")]
    public static void ExportAndroidProject()
    {
        var scenes = EditorBuildSettings.scenes.Where(s => s.enabled).Select(s => s.path).ToArray();
        if (scenes.Length == 0)
        {
            Debug.LogError("Nenhuma cena habilitada em Build Settings.");
            throw new System.Exception("Sem cenas para export.");
        }

        EditorUserBuildSettings.exportAsGoogleAndroidProject = true;
        EditorUserBuildSettings.androidBuildType = AndroidBuildType.Release;

        var bpo = new BuildPlayerOptions
        {
            scenes = scenes,
            locationPathName = "Builds/AndroidProject",
            target = BuildTarget.Android,
            options = BuildOptions.None,
            targetGroup = BuildTargetGroup.Android
        };

        var report = BuildPipeline.BuildPlayer(bpo);
        var summary = report.summary;
        if (summary.result != BuildResult.Succeeded)
        {
            Debug.LogError($"Export falhou: {summary.result}");
            throw new System.Exception($"Export falhou: {summary.result}");
        }
        Debug.Log("Projeto Android exportado em Builds/AndroidProject");
    }
}
