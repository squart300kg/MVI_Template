plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
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
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:repository"))

    implementation(libs.org.jetbrains.kotlinx.coroutines.core)

    testImplementation(libs.androidx.test.ext.junit)
}