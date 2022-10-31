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
    namespace = "com.jdamcd.runlog.shared.utils"
}
