plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
  alias(libs.plugins.androidx.baseline.profile)
}

android {
  namespace = "kr.co.architecture.app"

  defaultConfig { }

  dependencies {
    implementation(project(":feature:first"))
    implementation(project(":feature:second"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:repository"))
    implementation(project(":core:common"))
    baselineProfile(project(":benchmarks"))

    implementation(libs.androidx.profile.installer)
  }
}

