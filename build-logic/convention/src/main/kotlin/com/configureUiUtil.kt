package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureUiUtil(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {

    dependencies {
      add("implementation", libs.findLibrary("org-jetbrains-kotlinx-collections-immutable").get())
      add("implementation", libs.findLibrary("io-coil-kt").get())
    }
  }
}