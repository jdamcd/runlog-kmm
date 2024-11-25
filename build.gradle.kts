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

subprojects {
    apply {
        plugin(rootProject.libs.plugins.spotless.get().pluginId)
    }
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(libs.versions.ktlint.get())
                .setEditorConfigPath(rootProject.file(".editorconfig").path)
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.versions.ktlint.get())
                .setEditorConfigPath(rootProject.file(".editorconfig").path)
        }
    }
}
