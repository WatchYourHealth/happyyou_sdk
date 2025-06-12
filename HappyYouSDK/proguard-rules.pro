# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

#from here rules started////////////////////

#Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

#Febric
-dontwarn com.google.appengine.api.urlfetch.**
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes SourceFile,LineNumberTable

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep public class com.google.gson

-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-ignorewarnings

# Preserve all annotations.
-keepattributes *Annotation*

# Preserve all .class method names.
-keepclassmembernames class * {
   java.lang.Class class$(java.lang.String);
   java.lang.Class class$(java.lang.String, boolean);
}
# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
   native <methods>;
}
# Preserve the special static methods that are required in all enumeration
# classes.
-keepclassmembers class * extends java.lang.Enum {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your library doesn't use serialization.
# If your code contains serializable classes that have to be backward
# compatible, please refer to the manual.
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   static final java.io.ObjectStreamField[] serialPersistentFields;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.preference.Preference
-keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
}

-keepclassmembers class **.R$* {
  public static <fields>;
}

-keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
  public void *ButtonClicked(android.view.View);
}


#creashlytics
-keepattributes *Annotation*
-keep public class * extends java.lang.Exception
-printmapping build/outputs/mapping/release/mapping.txt


#keep classes
-keep class com.wyh.happyyousdk.model.request.* { *; }
-keep class com.wyh.happyyousdk.model.request.** { *; }
-keep class com.wyh.happyyousdk.model.request.*.* { *; }
-keep class com.wyh.happyyousdk.model.request.*.** { *; }
-keep class com.wyh.happyyousdk.model.request.** { *; }
-keep class com.wyh.happyyousdk.model.response.* { *; }
-keep class com.wyh.happyyousdk.model.response.*.*.* { *; }
-keep class com.wyh.happyyousdk.model.response.*.*.** { *; }
-keep class com.wyh.happyyousdk.model.response.*.* { *; }
-keep class com.wyh.happyyousdk.model.response.*.** { *; }
-keep class com.wyh.happyyousdk.model.response.** { *; }

-keep class com.wyh.happyyousdk.** {
    public *;
}
-dontobfuscate


-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-dontwarn org.apache.http.**
-dontwarn android.net.**
-keep class org.apache.** {*;}


-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-optimizations !method/inlining/*

-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.* { *; }
-keep class com.wyh.happyyousdk.utils.pdfviewer.**


#SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }


#For Contact Picker
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#For carousel
-keep class com.synnapps.carouselview.** { *; }

#For face scan
-keep class com.google.mediapipe.solutioncore.** {*;}
-keep class com.google.protobuf.** {*;}
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
*;
}


#For Google Fit
-keepclassmembers class com.google.android.gms.dynamite.DynamiteModule {
    ** MODULE_ID;
    ** MODULE_VERSION;
    ** sClassLoader;
}
-keepclassmembers class com.google.android.gms.internal.in {
    ** mOrigin;
    ** mCreationTimestamp;
    ** mName;
    ** mValue;
    ** mTriggerEventName;
    ** mTimedOutEventName;
    ** mTimedOutEventParams;
    ** mTriggerTimeout;
    ** mTriggeredEventName;
    ** mTriggeredEventParams;
    ** mTimeToLive;
    ** mExpiredEventName;
    ** mExpiredEventParams;
}
-keepclassmembers class com.google.devtools.build.android.desugar.runtime.ThrowableExtension {
    ** SDK_INT;
}
-keep class com.google.android.gms.dynamic.IObjectWrapper
-keep class com.google.android.gms.tasks.Task
-keep class com.google.android.gms.tasks.TaskCompletionSource
-keep class com.google.android.gms.tasks.OnSuccessListener
-keep class com.google.android.gms.tasks.OnFailureListener
-keep class com.google.android.gms.tasks.OnCompleteListener
-keep class com.google.android.gms.tasks.Continuation
-keep class com.google.android.gms.measurement.AppMeasurement$EventInterceptor
-keep class com.google.android.gms.measurement.AppMeasurement$OnEventListener
-keep class com.google.android.gms.measurement.AppMeasurement$zza
-keep class com.google.android.gms.internal.*
-keep class com.google.android.gms.common.api.Result
-keep class com.google.android.gms.common.zza

-dontnote com.google.android.gms.internal.ql
-dontnote com.google.android.gms.internal.zzcem
-dontnote com.google.android.gms.internal.zzchl

# Firebase notes
-dontnote com.google.firebase.messaging.zza

# Protobuf notes
-dontnote com.google.protobuf.zzc
-dontnote com.google.protobuf.zzd
-dontnote com.google.protobuf.zze
-dontwarn com.google.android.gms.auth.GoogleAuthUtil
-dontwarn com.squareup.picasso.OkHttpLoader

-keep class com.trackier.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
     int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
     com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
     java.lang.String getId();
     boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }