buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}")
        classpath("com.android.tools.build:gradle:${Version.androidGradle}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${Version.buildKonfig}")
    }
}
group = "com.jdamcd.runlog"
version = AppVersion.name

plugins {
    id("com.diffplug.spotless") version "5.6.1"
}

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            ktlint("0.39.0")
        }
        kotlinGradle {
            target("*.gradle.kts", "additionalScripts/*.gradle.kts")
            ktlint("0.39.0")
        }
    }
}
