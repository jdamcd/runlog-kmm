plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
group = "com.jdamcd.runlog"
version = AppVersion.name

dependencies {
    implementation(project(":shared:main"))
    implementation(Dependency.Ktx.core)
    implementation(Dependency.Ktx.activity)
    implementation(Dependency.Lifecycle.viewModel)
    implementation(Dependency.material)
    implementation(Dependency.splash)
    implementation(Dependency.Hilt.core)
    implementation(Dependency.Hilt.compose)
    implementation(Dependency.coil)
    implementation(Dependency.Koin.android)
    kapt(Dependency.Hilt.compiler)
    kapt(Dependency.Lifecycle.compiler)

    // Compose
    implementation(platform(Dependency.Compose.bom))
    implementation(Dependency.Compose.ui)
    implementation(Dependency.Compose.activity)
    implementation(Dependency.Compose.tooling)
    implementation(Dependency.Compose.foundation)
    implementation(Dependency.Compose.material)
    implementation(Dependency.Compose.icons)
    implementation(Dependency.Compose.iconsExtended)
    implementation(Dependency.Compose.navigation)
    implementation(Dependency.Compose.constraintLayout)

    // Tests
    testImplementation(Dependency.Mockito.core)
    testImplementation(Dependency.Mockito.kotlin)
    testImplementation(Dependency.kotestAssert)
    testImplementation(Dependency.coroutinesTest)
    testImplementation(Dependency.archTest)
    testImplementation(Dependency.turbine)
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }
    lint {
        abortOnError = false
    }
    namespace = "com.jdamcd.runlog.android"
}
