package com

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

private const val TARGET_SDK = 35
private const val MIN_SDK = 26
private const val COMPILE_SDK = 35
private const val VERSION_CODE = 1
private const val VERSION_NAME = "1.0"
private const val PACKAGE_NAME = "kr.co.architecture.ssy"

internal fun Project.configureBaseAppVersion(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {
    when (this) {
      is ApplicationExtension -> {
        defaultConfig {
          targetSdk = TARGET_SDK
          minSdk = MIN_SDK
          applicationId = PACKAGE_NAME
          versionCode = VERSION_CODE
          versionName = VERSION_NAME
        }
      }
      is TestExtension -> {
        defaultConfig.targetSdk = TARGET_SDK
        defaultConfig.minSdk = MIN_SDK
      }
      is LibraryExtension -> {
        defaultConfig.minSdk = MIN_SDK
      }
    }
    compileSdk = COMPILE_SDK
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
  }
}