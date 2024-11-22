buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.kotlin.gradle)
        classpath(libs.kotlin.serialization)
        classpath(libs.android.gradle)
        classpath(libs.hilt.gradle)
        classpath(libs.buildkonfig.gradle)
        classpath(libs.sqldelight.gradle)
    }
}
group = "com.jdamcd.runlog"
version = AppVersion.name

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.compose.compiler) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(libs.versions.ktlint.get())
        }
        kotlinGradle {
            target("*.gradle.kts", "additionalScripts/*.gradle.kts")
            ktlint(libs.versions.ktlint.get())
        }
    }
}
