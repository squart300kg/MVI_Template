plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.architecture.sample.base.setting)
    alias(libs.plugins.architecture.sample.ui)
}

android {
    namespace = "kr.co.architecture.core.ui"

    defaultConfig { }

    dependencies {
        implementation(project(":core:model"))
        implementation(project(":core:domain"))
        implementation(project(":core:repository"))
        implementation(project(":core:common"))
    }
}