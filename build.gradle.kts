plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.org.jetbrains.kotlin.atomicfu) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.google.protobuf) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.baselineprofile) apply false
}