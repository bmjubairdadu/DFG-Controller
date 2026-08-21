# Aggressive optimization
-optimizationpasses 5
-allowaccessmodification
-overloadaggressively

# General Android rules
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,EnclosingMethod

# Keep entry points
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# Compose rules
-keepclassmembers class * extends androidx.compose.runtime.Composer { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# DataStore rules (keep serializers if any, and generated code if using Proto)
-keepclassmembers class * extends androidx.datastore.core.Serializer { *; }

# libsu rules
-keep class com.topjohnwu.superuser.** { *; }
-keep interface com.topjohnwu.superuser.** { *; }

# Keep our models if used for serialization/reflection
-keep class com.dfgcontroller.ui.models.** { *; }

# Obfuscate everything else
-repackageclasses ''
-flattenpackagehierarchy ''
-adaptresourcefilenames    **.properties,**.gif,**.jpg,**.jpeg,**.png
-adaptresourcefilecontents **.properties,**.xml,META-INF/MANIFEST.MF
