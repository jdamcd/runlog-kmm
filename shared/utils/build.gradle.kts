plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
group = "com.jdamcd.runlog"
version = AppVersion.name

kotlin {
    android()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {}
        }
        val commonTest by getting {
            dependencies {}
        }
        val androidMain by getting {
            dependencies {}
        }
        val androidTest by getting {
            dependencies {}
        }
        val iosMain by getting {
            dependencies {}
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
