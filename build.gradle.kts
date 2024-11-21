buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}")
        classpath("com.android.tools.build:gradle:${Version.androidGradle}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${Version.buildKonfig}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Version.sqldelight}")
    }
}
group = "com.jdamcd.runlog"
version = AppVersion.name

plugins {
    id("com.diffplug.spotless") version Version.spotless
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
            ktlint(Version.ktlint)
        }
        kotlinGradle {
            target("*.gradle.kts", "additionalScripts/*.gradle.kts")
            ktlint(Version.ktlint)
        }
    }
}
