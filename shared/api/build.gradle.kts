import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvmToolchain(21)
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:utils"))
            implementation(libs.koin.core)
            implementation(libs.bundles.ktor.common)
            implementation(libs.coroutines)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotest)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    compileSdk = AndroidVersion.target
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidVersion.minimum
    }
    namespace = "com.jdamcd.runlog.shared.api"
}

buildkonfig {
    packageName = "com.jdamcd.runlog.shared.api"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "CLIENT_ID",
            gradleLocalProperties(rootDir, providers).getProperty("com.jdamcd.runlog.client_id", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "CLIENT_SECRET",
            gradleLocalProperties(rootDir, providers).getProperty("com.jdamcd.runlog.client_secret", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "MAPBOX_TOKEN",
            gradleLocalProperties(rootDir, providers).getProperty("com.jdamcd.runlog.mapbox_token", "")
        )
    }
}
