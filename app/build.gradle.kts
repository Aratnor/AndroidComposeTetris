import game.fabric.buildsrc.Dependencies
import game.fabric.buildsrc.Config
import game.fabric.buildsrc.Modules

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = Config.applicationId
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

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
    implementation(Dependencies.androidCore)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.lifecycleKtx)
    implementation(Dependencies.lifecycleCompose)
    implementation(project(Modules.GAME_LOGIC))
    implementation(Dependencies.viewModelExtension)
    implementation(Dependencies.activityKtx)
    implementation(Dependencies.navigation)
    implementation(Dependencies.material)
    implementation(Dependencies.COMPOSE_UI)
    implementation(Dependencies.COMPOSE_TOOL)
    implementation(Dependencies.COMPOSE_PREVIEW)
    kapt(Dependencies.HILT_COMPILER)
    implementation(Dependencies.HILT_ANDROID)
    implementation(Dependencies.HILT_COMPOSE)
}