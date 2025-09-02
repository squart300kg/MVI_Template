plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
}

android {
  namespace = "kr.co.test.testing"

  defaultConfig { }

  dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:repository"))

    implementation(libs.androidx.test.rules)
    implementation(libs.kotlinx.coroutines.test)
  }
}