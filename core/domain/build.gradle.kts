plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.dagger.hilt)
    kotlin("kapt")
}

android {
    namespace = "kr.co.architecture.domain"

    defaultConfig {
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:repository"))

    implementation(libs.org.jetbrains.kotlinx.coroutines.core)
    implementation(libs.com.google.dagger.hilt.android)

    kapt(libs.com.google.dagger.hilt.compiler)

    testImplementation(libs.androidx.test.ext.junit)
}