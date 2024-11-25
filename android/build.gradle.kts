plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":shared:main"))
    implementation(libs.ktx.core)
    implementation(libs.ktx.activity)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.material)
    implementation(libs.splash)
    implementation(libs.hilt.core)
    implementation(libs.hilt.compose)
    implementation(libs.coil)
    implementation(libs.koin.android)
    ksp(libs.hilt.compiler)
    ksp(libs.lifecycle.compiler)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)
    implementation(libs.compose.tooling)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.icons)
    implementation(libs.compose.iconsExtended)
    implementation(libs.compose.navigation)
    implementation(libs.compose.constraintLayout)

    // Tests
    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.arch.test)
    testImplementation(libs.turbine)
}
android {
    compileSdk = AndroidVersion.target
    defaultConfig {
        applicationId = "com.jdamcd.runlog.android"
        minSdk = AndroidVersion.minimum
        targetSdk = AndroidVersion.target
        versionCode = AppVersion.code
        versionName = AppVersion.name
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    lint {
        abortOnError = false
    }
    namespace = "com.jdamcd.runlog.android"
}

kotlin {
    jvmToolchain(17)
}
