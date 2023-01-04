import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.codingfeline.buildkonfig")
}
group = AppConfig.group
version = AppVersion.name

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:utils"))
                implementation(Dependency.Ktor.core)
                implementation(Dependency.Ktor.contentNegotiation)
                implementation(Dependency.Ktor.auth)
                implementation(Dependency.Ktor.log)
                implementation(Dependency.Ktor.serialize)
                implementation(Dependency.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
                implementation(Dependency.junit)
                implementation(Dependency.kotestAssert)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependency.Ktor.android)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Dependency.Ktor.ios)
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
        targetSdk = AndroidVersion.target
    }
    namespace = "com.jdamcd.runlog.shared.api"
}

buildkonfig {
    packageName = "com.jdamcd.runlog.shared.api"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "CLIENT_ID",
            gradleLocalProperties(rootDir).getProperty("com.jdamcd.runlog.client_id", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "CLIENT_SECRET",
            gradleLocalProperties(rootDir).getProperty("com.jdamcd.runlog.client_secret", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "MAPBOX_TOKEN",
            gradleLocalProperties(rootDir).getProperty("com.jdamcd.runlog.mapbox_token", "")
        )
    }
}
