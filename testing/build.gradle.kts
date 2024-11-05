plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.architecture.sample.base.setting)
    alias(libs.plugins.jetbrains.kotlin)
}

android {
    namespace = "kr.co.test.testing"

    defaultConfig { }

    dependencies {
        implementation(project(":core:model"))
        implementation(project(":core:common"))
        implementation(project(":core:repository"))
        implementation(project(":core:network"))

        implementation(libs.androidx.test.rules)
        implementation(libs.kotlinx.coroutines.test)
    }
}