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
include(":feature:alimCenter")
include(":core:ui")
include(":core:model")
include(":core:network")
include(":core:repository")
include(":core:domain")
include(":core:common")
include(":testing")
include(":benchmarks")