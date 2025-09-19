package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureViewSystemUi(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {
    dependencies {
      add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
      add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
      add("implementation", libs.findLibrary("androidx-constraintlayout").get())
    }
  }
}