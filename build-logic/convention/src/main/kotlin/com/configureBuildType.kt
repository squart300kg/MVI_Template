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

        storeFile = this@configureBuildType.file("./keystore/ssyssy.jks")
        storePassword = System.getenv("RELEASE_PASSWD") ?: RELEASE_KEYSTORE_PASSWORD
        keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: RELEASE_KEY_ALIAS
        keyPassword = System.getenv("RELEASE_KEY_PASSWD") ?: RELEASE_KEY_PASSWORD
      }
    }
    (this as? ApplicationExtension)?.apply {
      buildTypes {
        getByName("release") {
//          isDebuggable = true
          signingConfig = signingConfigs.getByName("release")
        }
      }
    }
    buildTypes {
      getByName("release") {
        // AGP버전 8.4.0부터 라이브러리 모듈 자체적으로 코드축소 실행하여
        // 'R8: Type a.a is defined multiple times'에러 발생.
        // 따라서 앱 모듈에만 코드축소 적용
        isMinifyEnabled = commonExtension is ApplicationExtension
        proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          "proguard-rules.pro"
        )
      }
    }

    dependencies { }
  }
}