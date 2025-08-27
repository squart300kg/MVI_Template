plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
  alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
  namespace = "kr.co.architecture.core.database"

  defaultConfig { }

  dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)
  }
}