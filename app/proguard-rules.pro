# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-printmapping mapping.txt

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn javax.enterprise.context.ApplicationScoped
-dontwarn javax.enterprise.inject.Alternative

-keep class io.ktor.http.Cookie
-keep class com.google.gson.internal.LinkedTreeMap
-keep class cn.spacexc.wearbili** {*;}
-keep class cn.spacexc.bilibilisdk.** { *; }
-keep class com.arialyy.** { *; }
-keep class com.arialyy.*
-keep class com.arialyy.**
-keep class com.arialyy**
-keep class com.microsoft.** { *; }
-keep class com.microsoft.*
-keep class com.microsoft.**
-keep class com.microsoft**
-keep class io.ktor.** { *; }
-keep class io.ktor.*
-keep class io.ktor.**
-keep class io.ktor**
-keep class cn.spacexc**
-keep class com.google.* {*;}

-keep class cn.spacexc.*
-keep class cn.spacexc.**
-keep class com.google.* {*;}
-keep class com.google.gson.** {*;}
-keep class com.google.gson.reflect.** {*;}
-keep class com.google.gson.stream.** {*;}
-keep class com.google.gson.internal.** {*;}
-keep class com.google.gson.internal.bind.** {*;}
-keep class com.google.gson.internal.bind.util.** {*;}
-keep class com.google.gson.internal.bind.reflectiveTypeAdapterFactory.** {*;}
-keep class com.google.gson.internal.bind.reflectiveTypeAdapterFactory$Adapter** {*;}
-keep class com.google.gson.internal.bind.reflectiveTypeAdapterFactory$Adapter$$** {*;}
-keep class com.google.gson.internal.bind.reflectiveTypeAdapterFactory$BoundField** {*;}

-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

-keep class android.** { *; }

# Gson uses generic type information stored in a class file when working with
# fields. Proguard removes such information by default, keep it.
-keepattributes Signature

# This is also needed for R8 in compat mode since multiple
# optimizations will remove the generic signature such as class
# merging and argument removal. See:
# https://r8.googlesource.com/r8/+/refs/heads/main/compatibility-faq.md#troubleshooting-gson-gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Optional. For using GSON @Expose annotation
#-keepattributes AnnotationDefault,RuntimeVisibleAnnotations

# 指定代码的压缩级别
-optimizationpasses 5

# 不忽略库中的非public的类成员
-dontskipnonpubliclibraryclassmembers

# google推荐算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# 避免混淆Annotation、内部类、泛型、匿名类
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 保持四大组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

# 保持support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保持自定义控件
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes Signature
-dontwarn com.jcraft.jzlib.**
-keep class com.jcraft.jzlib.**  { *;}
-dontwarn sun.misc.**
-keep class sun.misc.** { *;}
-dontwarn retrofit2.**
-keep class retrofit2.** { *;}
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *; }
-dontwarn com.google.**
-keep class com.google.** { *;}
-dontwarn cn.leancloud.**
-keep class cn.leancloud.** { *;}
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontwarn android.support.**
-dontwarn org.apache.**
-keep class org.apache.** { *;}
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *;}
-keepattributes *Annotation*