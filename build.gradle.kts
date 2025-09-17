import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  dependencies {
    classpath(libs.hilt.android.gradle.plugin)
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.jetbrains.kotlin) apply false
  alias(libs.plugins.dagger.hilt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization) apply false
  alias(libs.plugins.android.test) apply false
}

subprojects {
  tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
      if (project.findProperty("composeCompilerReports") == "true") {
        freeCompilerArgs.addAll(
          listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir}/compose_compiler"
          )
        )
      }
      if (project.findProperty("composeCompilerMetrics") == "true") {
        freeCompilerArgs.addAll(
          listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir}/compose_compiler"
          )
        )
      }
    }
  }
}