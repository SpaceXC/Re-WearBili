plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "cn.spacexc.wearbili.remake.livestreamplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "cn.spacexc.wearbili.remake.livestreamplayer"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    viewBinding {
        enable = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    val activityVersion = "1.7.2"
    // Kotlin
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation(project(path = ":Bilibili-Kotlin-SDK"))

    val media3Version = "1.1.0"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    // For DASH playback support with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")
    // For HLS playback support with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version")
    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3Version")
    // For exposing and controlling media sessions
    implementation("androidx.media3:media3-session:$media3Version")
    // Common functionality for media database components
    implementation("androidx.media3:media3-database:$media3Version")
    // Common functionality for media decoders
    implementation("androidx.media3:media3-decoder:$media3Version")
    // Common functionality for loading data
    implementation("androidx.media3:media3-datasource:$media3Version")
    // Common functionality used across multiple media libraries
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-datasource-okhttp:$media3Version")
}