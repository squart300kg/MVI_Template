import org.jetbrains.kotlin.konan.properties.Properties

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
  namespace = "kr.co.architecture.core.network"

  defaultConfig {
    buildConfigField("String", "API_URL", "${properties["API_URL"]}")
  }

  dependencies {
    implementation(project(":core:model"))

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson.converter)
    api(libs.com.github.skydoves.sandwich)
    api(libs.com.github.skydoves.sandwich.retrofit)
  }
}

