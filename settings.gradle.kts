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
include(":feature:search")
include(":feature:bookmark")
include(":feature:detail")
include(":core:ui")
include(":core:router")
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:repository")
include(":core:database")
include(":core:domain")
include(":testing")
include(":benchmarks")