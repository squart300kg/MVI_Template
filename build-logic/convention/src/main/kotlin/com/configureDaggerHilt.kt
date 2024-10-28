package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDaggerHilt(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply("com.google.dagger.hilt.android")
    pluginManager.apply("com.google.devtools.ksp")

    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("com-google-dagger-hilt-android").get())
            add("ksp", libs.findLibrary("com-google-dagger-hilt-compiler").get())
        }
    }
}