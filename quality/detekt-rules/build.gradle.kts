plugins {
  id("org.jetbrains.kotlin.jvm")
}

dependencies {
  compileOnly(libs.detekt.api)

  testImplementation(libs.detekt.test)
  testImplementation(libs.junit)
}
