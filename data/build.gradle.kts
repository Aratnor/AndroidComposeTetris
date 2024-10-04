import game.fabric.buildsrc.Config
import game.fabric.buildsrc.Dependencies
import game.fabric.buildsrc.NameSpaces

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = NameSpaces.DATA
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(Dependencies.ANDROID_CORE)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.LIFECYCLE_RUNTIME)
    implementation(Dependencies.LIFECYCLE_COMPOSE)
    implementation(Dependencies.ACTIVITY)
    implementation(Dependencies.NAVIGATION_COMPOSE)
    implementation(Dependencies.MATERIAL)
}