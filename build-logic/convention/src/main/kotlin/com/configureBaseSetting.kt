package com

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureBaseSetting(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {

  pluginManager.apply("org.jetbrains.kotlin.android")
  pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

  commonExtension.apply {
    when (this) {
      is ApplicationExtension -> {
        defaultConfig {
          vectorDrawables {
            useSupportLibrary = true
          }
        }
        packaging {
          resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
      }
    }
    dependencies {
      add("implementation", libs.findLibrary("org-jetbrains-kotlinx-coroutines-core").get())
      add("implementation", libs.findLibrary("androidx-core-ktx").get())
      add("implementation", libs.findLibrary("org-jetbrains-kotlinx-serialization-json").get())
      add("implementation", libs.findLibrary("com-google-code-gson").get())
    }
  }
}