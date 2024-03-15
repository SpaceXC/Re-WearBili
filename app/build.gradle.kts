plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    kotlin("kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "cn.spacexc.wearbili.remake"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTool.get()

    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true

    val releaseNumber = 5
    defaultConfig {
        applicationId = "cn.spacexc.wearbili.remake"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 37
        versionName = "Glaciaxia 冰封"

        vectorDrawables {
            useSupportLibrary = true
        }

    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources.excludes.apply {
            add("/META-INF/{AL2.0,LGPL2.1}")
            add("META-INF/beans.xml")
        }
    }


    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName =
                "Re-WearBili - $versionName Ver.$releaseNumber Rel.$versionCode.apk"
        }
    }


}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:" + libs.versions.protobuflite.get()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.kotlinx.metadata.jvm)
    implementation(project(":app:common"))
    implementation(project(":ijkplayer-java"))
    implementation(project(":ijkplayer-so"))

    implementation(libs.androidx.datastore.proto)
    implementation(libs.androidx.datastore.core)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

    implementation(libs.hilt.android)
    implementation(libs.hilt.work)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.work.compiler)

    // TODO: 使用fastutil获得更好的性能
    // implementation(libs.fastutil)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.accompanist.placeholder.material)


    implementation(libs.androidx.paging.runtime.ktx)
    //noinspection GradleDependency
    implementation(libs.androidx.paging.compose)


    implementation(libs.appcenter.analytics)
    implementation(libs.appcenter.crashes)


    /*// For media playback using ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    // For DASH playback support with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.dash)
    // For exposing and controlling media sessions
    implementation(libs.androidx.media3.session)
    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.workmanager)
    // Common functionality for media decoders
    implementation(libs.androidx.media3.decoder)
    // Common functionality for loading data
    implementation(libs.androidx.media3.datasource)
    // Common functionality used across multiple media libraries
    implementation(libs.androidx.media3.common)*/
    //implementation(libs.danmaku.flame.master)

    //implementation(libs.crashx)


    // (Java only)
    implementation(libs.androidx.work.runtime)
    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)


    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)

    implementation(libs.okhttp)
    implementation(libs.rxhttp)
    ksp(libs.rxhttp.compiler)


    implementation(libs.photoView)

    implementation(libs.zoomable)

    implementation(libs.jsoup)

    implementation(libs.leancloud.storage)
    implementation(libs.rxjava.rxandroid)
}