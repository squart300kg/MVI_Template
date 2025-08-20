package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidTestUtil(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {

    dependencies {
      add("androidTestImplementation", libs.findLibrary("junit").get())
      add("androidTestImplementation", libs.findLibrary("androidx-test-ext-junit").get())
    }
  }
}