plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    jvmToolchain(17)
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "RunLogShared"
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:api"))
            implementation(project(":shared:database"))
            implementation(project(":shared:utils"))
            implementation(libs.koin.core)
            implementation(libs.coroutines)
            implementation(libs.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotest)
            implementation(libs.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.ktx.core)
        }
        androidUnitTest.dependencies {
            implementation(libs.kotest)
            implementation(libs.mockk)
            implementation(libs.coroutines.test)
            implementation(libs.turbine)
        }
        iosMain.dependencies {
        }
        iosTest.dependencies {
            implementation(libs.kotest)
        }
    }
}

android {
    compileSdk = AndroidVersion.target
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidVersion.minimum
    }
    namespace = "com.jdamcd.runlog.shared"
}
