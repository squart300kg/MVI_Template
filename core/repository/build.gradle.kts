plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.architecture.sample.base.setting)
}

android {
    namespace = "kr.co.architecture.core.repository"

    defaultConfig { }

    dependencies {
        implementation(project(":core:model"))
        implementation(project(":core:common"))
        implementation(project(":core:network"))

        implementation(libs.com.github.skydoves.sandwich)
    }
}

