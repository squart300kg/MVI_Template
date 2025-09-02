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
include(":feature:first")
include(":feature:second")
include(":feature:detail")
include(":core:ui")
include(":core:router")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:repository")
include(":core:domain")
include(":testing")
include(":benchmarks")