package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureUnitTestUtil(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {

    defaultConfig {
      testInstrumentationRunner =
        "androidx.test.runner.AndroidJUnitRunner"
    }
    dependencies {
      add("testImplementation", libs.findLibrary("org-mockito-core").get())
      add("testImplementation", libs.findLibrary("org-mockito-kotlin").get())
      add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
      add("testImplementation", libs.findLibrary("androidx-test-ext-junit").get())
    }
  }
}