plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.architecture.sample.library.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    kotlin("kapt")
}

android {
    namespace = "kr.co.architecture.home"

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

    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
    implementation(project(":core:common"))
    implementation(project(":testing"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.com.google.code.gson)
    implementation(libs.org.jetbrains.kotlinx.serialization.json)
    implementation(libs.io.coil.kt)
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.androidx.paging.runtimne)
    implementation(libs.androidx.work.runtime)
    implementation(libs.org.jetbrains.kotlinx.collections.immutable)

    kapt(libs.com.google.dagger.hilt.compiler)

    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}