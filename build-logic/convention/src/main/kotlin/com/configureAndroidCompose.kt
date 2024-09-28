package com

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures { compose = true }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.11"
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("androidx-paging-compose").get())

            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())

            add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-testManifest").get())
        }
    }
}