# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep ARCore classes
-keep class com.google.ar.core.** { *; }

# Keep OpenGL classes
-keep class javax.microedition.khronos.** { *; }

# Keep data classes for serialization
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
}

# Keep user progress classes
-keep class com.geoar.app.data.** { *; }
