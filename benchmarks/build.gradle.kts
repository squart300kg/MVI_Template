plugins {
  alias(libs.plugins.android.test)
  alias(libs.plugins.architecture.sample.benchmarks)
}

android {
  namespace = "kr.co.architecture.benchmarks"

  defaultConfig { }
}

dependencies {
  implementation(project(":testing"))
}
