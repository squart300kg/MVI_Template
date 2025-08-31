plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
}

android {
  namespace = "kr.co.architecture.core.repository"

  defaultConfig { }

  dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(libs.com.github.skydoves.sandwich)
  }
}

