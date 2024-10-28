plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.architecture.sample.library.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "kr.co.architecture.ui"

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
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:repository"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.com.google.code.gson)
    implementation(libs.org.jetbrains.kotlinx.serialization.json)
    implementation(libs.io.coil.kt)
    implementation(libs.androidx.work.runtime)
    implementation(libs.org.jetbrains.kotlinx.collections.immutable)

    testImplementation(libs.kotlinx.coroutines.test)
    
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}