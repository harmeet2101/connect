# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/azfar/Android/adt/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-flattenpackagehierarchy
-repackageclasses
-overloadaggressively
-allowaccessmodification
-optimizations code/allocation/variable,code/simplification/cast,code/simplification/string,code/simplification/variable

-dontwarn com.twotoasters.android.**
-dontwarn com.androauth.oauth.**
-dontwarn org.joda.**
-dontwarn com.artisan.**
-dontwarn com.flurry.**

# For Artisan
-keepattributes Signature,*Annotation*
-libraryjars libs # if you are already including everything in libs as library jars you can omit this line or you can specify libs/artisan_library.jar
-keep class com.artisan.** { *; }
-keep interface com.artisan.** { *; }

-keep class com.mboconnect.entities.** { *; }
-keepclassmembers class com.mboconnect.entities.** { *; }
-keep class android.support.v7.widget.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------