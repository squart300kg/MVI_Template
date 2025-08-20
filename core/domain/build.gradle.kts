plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
}

android {
  namespace = "kr.co.architecture.core.domain"

  defaultConfig { }

  dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:repository"))

    implementation(libs.com.github.skydoves.compose.stable.marker)
  }
}