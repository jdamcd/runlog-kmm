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
                implementation(Dependency.ktorSerialize)
                implementation(Dependency.kotlinCoroutines)
                implementation(Dependency.kotlinSerialize)
                implementation(Dependency.stately)
                implementation(Dependency.statelyConcurrency)
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
    compileSdkVersion(AndroidVersion.target)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(AndroidVersion.minimum)
        targetSdkVersion(AndroidVersion.target)
        versionCode = AppVersion.code
        versionName = AppVersion.name
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

buildkonfig {
    packageName = "com.jdamcd.runlog.shared.api"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "CLIENT_ID",
            com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)
                .getProperty("com.jdamcd.runlog.client_id", "")
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "CLIENT_SECRET",
            com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)
                .getProperty("com.jdamcd.runlog.client_secret", "")
        )
    }
}
