plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
  kotlin("kapt")
}

android {
  namespace = "kr.co.architecture.feature.detail"

  defaultConfig { }

  buildFeatures {
    dataBinding = true
    compose = false
  }
  dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:router"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
    implementation(project(":testing"))
  }
}