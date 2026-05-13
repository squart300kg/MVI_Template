import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.tasks.Exec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  alias(libs.plugins.detekt)
}

dependencies {
  detektPlugins(project(":quality:detekt-rules"))
  detektPlugins(libs.detekt.formatting)
}

detekt {
  buildUponDefaultConfig = false
  parallel = true
  config.setFrom(files("$rootDir/detekt.yml"))
  source.setFrom(
    files(
      "app/src/main/kotlin",
      "core",
      "feature",
      "testing/src/main/kotlin",
    )
  )
}

tasks.withType<Detekt>().configureEach {
  exclude("**/build/**")
  reports {
    html.required.set(true)
    xml.required.set(false)
    txt.required.set(true)
    sarif.required.set(false)
    md.required.set(false)
  }
}

tasks.register<Exec>("verifyHarnessConsistency") {
  group = "verification"
  description = "Verifies AGENTS/CLAUDE and .ai-skills mirror consistency."
  commandLine("bash", "./scripts/verify-harness-consistency.sh")
}

tasks.register<Exec>("verifyArchitectureRules") {
  group = "verification"
  description = "Verifies project architecture rules for feature navigation and global UI."
  commandLine("bash", "./scripts/verify-architecture-rules.sh")
}

tasks.register("qualityGateFast") {
  group = "verification"
  description = "Runs the fast local quality gate for Android work."
  dependsOn("verifyHarnessConsistency")
  dependsOn("verifyArchitectureRules")
  dependsOn("detekt")
  dependsOn(":quality:detekt-rules:test")
  dependsOn(":app:compileDebugKotlin")
}

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      val composeCompilerDir = project.layout.buildDirectory.dir("compose_compiler").get().asFile.absolutePath
      jvmTarget.set(JvmTarget.JVM_17)
      if (project.findProperty("composeCompilerReports") == "true") {
        freeCompilerArgs.addAll(
          listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$composeCompilerDir"
          )
        )
      }
      if (project.findProperty("composeCompilerMetrics") == "true") {
        freeCompilerArgs.addAll(
          listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$composeCompilerDir"
          )
        )
      }
    }
  }
}

gradle.projectsEvaluated {
  tasks.named("qualityGateFast").configure {
    subprojects.forEach { subproject ->
      subproject.tasks.findByName("testDebugUnitTest")?.let { dependsOn(it) }
      subproject.tasks.findByName("lintDebug")?.let { dependsOn(it) }
    }
  }
}
