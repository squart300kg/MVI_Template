package com

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

private const val TARGET_SDK = 36
private const val MIN_SDK = 26
private const val COMPILE_SDK = 36
private const val VERSION_CODE = 1
private const val VERSION_NAME = "1.0"
private const val PACKAGE_NAME = "kr.co.architecture.ssy"

internal fun Project.configureBaseAppVersion(
  commonExtension: CommonExtension,
) {
  commonExtension.apply {
    when (this) {
      is ApplicationExtension -> {
        defaultConfig.targetSdk = TARGET_SDK
        defaultConfig.minSdk = MIN_SDK
        defaultConfig.applicationId = PACKAGE_NAME
        defaultConfig.versionCode = VERSION_CODE
        defaultConfig.versionName = VERSION_NAME
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
    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17
  }
}
