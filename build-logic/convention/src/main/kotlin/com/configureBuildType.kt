package com

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate

internal fun Project.configureBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        signingConfigs {
            create("release") {
                val RELEASE_KEYSTORE_PASSWORD: String by this@configureBuildType
                val RELEASE_KEY_ALIAS: String by this@configureBuildType
                val RELEASE_KEY_PASSWORD: String by this@configureBuildType

                storeFile = this@configureBuildType.file("./keystore/wanted.jks")
                storePassword = System.getenv("RELEASE_PASSWD") ?: RELEASE_KEYSTORE_PASSWORD
                keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: RELEASE_KEY_ALIAS
                keyPassword = System.getenv("RELEASE_KEY_PASSWD") ?: RELEASE_KEY_PASSWORD
            }
        }
        (this as? ApplicationExtension)?.apply {
            buildTypes {
                getByName("release") {
                    signingConfig = signingConfigs.getByName("release")
                }
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        dependencies {  }
    }
}