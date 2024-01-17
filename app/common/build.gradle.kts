plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.protobuf)
    kotlin("kapt")
}

android {
    namespace  = "cn.spacexc.wearbili.common"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTool.get()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        lint.targetSdk = libs.versions.targetSdk.get().toInt()

    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }

}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.ui.graphics)

    api(project(":libs"))
    api(libs.hilt.android)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)


    api(libs.protobuf.javalite)
    api(libs.protobuf.kotlin.lite)

    api(libs.gson)

    implementation(libs.zxing.core)

    kapt(libs.hilt.compiler)

}