import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    kotlin("android.extensions")
    id("com.codingfeline.buildkonfig")
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
                implementation(Dependency.ktxCore)
                implementation(Dependency.ktorAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Dependency.junit)
                implementation(Dependency.kotestAssert)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Dependency.ktoriOS)
            }
        }
        val iosTest by getting {
            dependencies {
                implementation(Dependency.kotestAssert)
            }
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

buildkonfig {
    packageName = "com.jdamcd.runlog.shared.internal"

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
    }
}
