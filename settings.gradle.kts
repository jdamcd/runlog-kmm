pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
rootProject.name = "RunLog"

include(":android")
include(":shared:main")
include(":shared:api")
include(":shared:database")
include(":shared:utils")
