import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "kr.co.architecture.build.logic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}
tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_21.toString()
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}


gradlePlugin {
  plugins {
    register("androidUi") {
      id = "architecture.sample.ui"
      implementationClass = "AndroidUiPlugin"
    }
    register("androidBaseSetting") {
      id = "architecture.sample.base.setting"
      implementationClass = "AndroidBaseSettingPlugin"
    }
    register("androidBenchmarks") {
      id = "architecture.sample.benchmarks"
      implementationClass = "AndroidBenchmarksPlugin"
    }
  }
}