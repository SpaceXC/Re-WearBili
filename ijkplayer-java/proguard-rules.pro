# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /opt/android/ADK/tools/proguard/proguard-android.txt
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

-keep class tv.danmaku.ijk.media.player.* { }
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{ }
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{ }

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
-keep class tv.danmaku.ijk.media.player.** {
    *;
}

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-keep class tv.**
-keep class tv.*
-keep class tv.* { *; }
-keep class tv.* { *; }
-keep class tv.danmaku.ijk.media.player.IMediaPlayer$OnCompletionListener
-keep class tv.danmaku.ijk.media.player.IMediaPlayer$OnInfoListener
-keep class tv.danmaku.ijk.media.player.IMediaPlayer$OnPreparedListener
-keep class tv.danmaku.ijk.media.player.IMediaPlayer
-keep class tv.danmaku.ijk.media.player.IjkLibLoader
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer