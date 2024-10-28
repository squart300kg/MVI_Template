plugins {
    alias(libs.plugins.architecture.sample.library.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
}

android {
    namespace = "kr.co.architecture.repository"

    defaultConfig {
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))

    implementation(libs.com.github.skydoves.sandwich)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.junit)
}