import org.jetbrains.kotlin.konan.properties.Properties

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.architecture.sample.base.setting)
}

val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
  properties.load(localPropertiesFile.inputStream())
}

fun propertyOrDefault(
  name: String,
  defaultValue: String
): String {
  return providers
    .environmentVariable(name)
    .orElse(providers.gradleProperty(name))
    .orElse(properties[name]?.toString() ?: defaultValue)
    .get()
}

fun quotedBuildConfigValue(value: String): String {
  return "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""
}

android {
  namespace = "kr.co.architecture.core.network"

  defaultConfig {
    buildConfigField(
      "String",
      "apiKey",
      quotedBuildConfigValue(propertyOrDefault("apiKey", ""))
    )
    buildConfigField(
      "String",
      "apiUrl",
      quotedBuildConfigValue(propertyOrDefault("apiUrl", "https://newsapi.org/"))
    )
  }

  dependencies {
    implementation(project(":core:model"))

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.com.github.skydoves.sandwich)
    implementation(libs.com.github.skydoves.sandwich.retrofit)
  }
}
