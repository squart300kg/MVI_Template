plugins {
    alias(libs.plugins.architecture.sample.application.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.architecture.sample.application.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    kotlin("kapt")
}

android {
    namespace = "kr.co.architecture.app"

    defaultConfig {
        applicationId = "kr.co.architecture.ssy"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(project(":feature:home"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:repository"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.org.jetbrains.kotlinx.serialization.json)
    implementation(libs.junit)
    implementation(libs.com.google.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)
}