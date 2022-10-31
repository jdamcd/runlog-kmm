import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
group = "com.jdamcd.runlog"
version = AppVersion.name

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "RunLogShared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:api"))
                implementation(project(":shared:utils"))
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
                implementation(Dependency.ktxCore)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependency.kotestAssert)
            }
        }
        val iosMain by getting {
            dependencies {}
        }
        val iosTest by getting {
            dependencies {
                implementation(Dependency.kotestAssert)
            }
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
    namespace = "com.jdamcd.runlog.shared"
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
