@file:Suppress("UnstableApiUsage")

include(":baselineprofile")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
        maven("http://4thline.org/m2"){
            isAllowInsecureProtocol = true
        }
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}
rootProject.name = "WearBili"
include(":app")
include(":app:common")
include(":libs")
include(":ijkplayer-java")
include(":ijkplayer-so")