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

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(libs.versions.ktlint.get())
            .editorConfigOverride(
                mapOf(
                    "ktlint_code_style" to "intellij_idea",
                    "ktlint_standard_property-naming" to "disabled",
                    "ktlint_standard_value-parameter-comment" to "disabled",
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                    "ij_kotlin_allow_trailing_comma" to "false",
                    "ij_kotlin_allow_trailing_comma_on_call_site" to "false"
                )
            )
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
            .editorConfigOverride(
                mapOf(
                    "ktlint_code_style" to "intellij_idea",
                    "ij_kotlin_allow_trailing_comma" to "false",
                    "ij_kotlin_allow_trailing_comma_on_call_site" to "false"
                )
            )
    }
}
