plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
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
                implementation(Dependency.Koin.core)
                implementation(Dependency.SqlDelight.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
                implementation(Dependency.junit)
                implementation(Dependency.kotestAssert)
                implementation(Dependency.coroutinesTest)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependency.SqlDelight.android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(Dependency.SqlDelight.jvm)
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
            dependencies {
                implementation(Dependency.SqlDelight.native)
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
    namespace = "com.jdamcd.runlog.shared.database"
}

sqldelight {
    database("RunLogDB") {
        packageName = "com.jdamcd.runlog.shared.database"
    }
}
