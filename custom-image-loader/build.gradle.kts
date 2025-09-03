plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.architecture.sample.ui)
}

android {
  namespace = "kr.co.architecture.core.custom.image.loader"

  defaultConfig { }

  dependencies {
    implementation(project(":custom-http-client"))
  }
}