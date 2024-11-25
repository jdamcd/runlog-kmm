plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
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
