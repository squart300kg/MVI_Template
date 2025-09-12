plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
}

android {
  namespace = "kr.co.architecture.yeo.feature.search"

  defaultConfig { }

  dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:router"))
    implementation(project(":core:domain"))
    implementation(project(":testing"))
  }
}