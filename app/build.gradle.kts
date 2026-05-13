plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
}

android {
  namespace = "kr.co.architecture.app"

  defaultConfig { }

  dependencies {
    implementation(project(":feature:first"))
    implementation(project(":feature:second"))
    implementation(project(":feature:detail"))
    implementation(project(":core:ui"))
    implementation(project(":core:router"))
    implementation(project(":core:domain"))
    implementation(project(":core:repository"))
  }
}
