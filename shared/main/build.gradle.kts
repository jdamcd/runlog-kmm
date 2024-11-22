plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
group = AppConfig.group
version = AppVersion.name

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
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:api"))
                implementation(project(":shared:database"))
                implementation(project(":shared:utils"))
                implementation(libs.koin.core)
                implementation(libs.coroutines)
                implementation(libs.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest)
                implementation(libs.coroutines.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktx.core)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.kotest)
                implementation(libs.mockk)
                implementation(libs.coroutines.test)
                implementation(libs.turbine)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {}
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
            dependencies {
                implementation(libs.kotest)
            }
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
