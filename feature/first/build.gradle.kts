plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
}

android {
  namespace = "kr.co.architecture.feature.first"

  defaultConfig { }

  dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:router"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
    implementation(project(":testing"))
  }
}