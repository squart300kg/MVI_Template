plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
}

android {
  namespace = "kr.co.architecture.core.router"

  defaultConfig { }

  dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
  }
}