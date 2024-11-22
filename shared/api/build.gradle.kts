import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvmToolchain(17)
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:utils"))
                implementation(libs.koin.core)
                implementation(libs.bundles.ktor.common)
                implementation(libs.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
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
