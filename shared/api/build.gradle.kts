import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.codingfeline.buildkonfig")
}
group = "com.jdamcd.runlog"
version = AppVersion.name

kotlin {
    android()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:utils"))
                implementation(Dependency.ktorCore)
                implementation(Dependency.ktorContentNegotiation)
                implementation(Dependency.ktorAuth)
                implementation(Dependency.ktorLog)
                implementation(Dependency.ktorSerialize)
                implementation(Dependency.kotlinCoroutines)
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
                implementation(Dependency.ktorAndroid)
            }
        }
        val androidTest by getting {
            dependencies {}
        }
        val iosMain by getting {
            dependencies {
                implementation(Dependency.ktoriOS)
            }
        }
        val iosTest by getting {
            dependencies {}
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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
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
