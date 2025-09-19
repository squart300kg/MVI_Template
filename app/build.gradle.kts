plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
  alias(libs.plugins.androidx.baseline.profile)
}

android {
  namespace = "kr.co.architecture.app"

  defaultConfig { }

  buildFeatures {
    dataBinding = true
  }

  dependencies {
    implementation(project(":feature:search"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:detail"))
    implementation(project(":core:ui"))
    implementation(project(":core:router"))
    implementation(project(":core:domain"))
    implementation(project(":core:repository"))
    baselineProfile(project(":benchmarks"))

    implementation(libs.androidx.profile.installer)
  }
}

