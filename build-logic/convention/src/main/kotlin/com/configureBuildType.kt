package com

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureBuildType(
  commonExtension: CommonExtension,
) {
  val releaseStoreFile = file("./keystore/ssyssy.jks")
  val releaseStorePassword = providers
    .environmentVariable("RELEASE_PASSWD")
    .orElse(providers.gradleProperty("RELEASE_KEYSTORE_PASSWORD"))
    .orNull
  val releaseKeyAlias = providers
    .environmentVariable("RELEASE_KEY_ALIAS")
    .orElse(providers.gradleProperty("RELEASE_KEY_ALIAS"))
    .orNull
  val releaseKeyPassword = providers
    .environmentVariable("RELEASE_KEY_PASSWD")
    .orElse(providers.gradleProperty("RELEASE_KEY_PASSWORD"))
    .orNull
  val hasReleaseSigning = releaseStoreFile.exists() &&
    !releaseStorePassword.isNullOrBlank() &&
    !releaseKeyAlias.isNullOrBlank() &&
    !releaseKeyPassword.isNullOrBlank()

  (commonExtension as? ApplicationExtension)?.apply {
    signingConfigs {
      if (hasReleaseSigning) {
        create("release") {
          storeFile = releaseStoreFile
          storePassword = releaseStorePassword
          keyAlias = releaseKeyAlias
          keyPassword = releaseKeyPassword
        }
      }
    }
    buildTypes {
      getByName("release") {
//        isDebuggable = true
        if (hasReleaseSigning) {
          signingConfig = signingConfigs.getByName("release")
        }
      }
    }
  }

  commonExtension.buildTypes.getByName("release") {
    // AGP버전 8.4.0부터 라이브러리 모듈 자체적으로 코드축소 실행하여
    // 'R8: Type a.a is defined multiple times'에러 발생.
    // 따라서 앱 모듈에만 코드축소 적용
    isMinifyEnabled = commonExtension is ApplicationExtension
    proguardFiles(
      commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
      "proguard-rules.pro"
    )
  }
}
