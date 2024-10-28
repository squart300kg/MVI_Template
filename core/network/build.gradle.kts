import org.jetbrains.kotlin.konan.properties.Properties
plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "kr.co.architecture.network"

    defaultConfig {
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "apiKey", "${properties["apiKey"]}")
        buildConfigField("String", "apiUrl", "${properties["apiUrl"]}")

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
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
    implementation(project(":core:common"))

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.com.github.skydoves.sandwich)

    testImplementation(libs.androidx.test.ext.junit)
}