pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "ArchitectureSample"
include(":app")
include(":feature:home")
include(":core:ui")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:repository")
include(":custom-http-client")
include(":custom-image-loader")
include(":benchmarks")