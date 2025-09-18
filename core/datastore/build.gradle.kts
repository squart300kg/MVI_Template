plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
  namespace = "kr.co.architecture.core.datastore"

  defaultConfig { }

  dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.datastore.preference)
  }
}