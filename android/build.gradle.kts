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
    implementation(Dependency.ktxCore)
    implementation(Dependency.ktxActvitiy)
    implementation(Dependency.viewModel)
    implementation(Dependency.material)
    implementation(Dependency.appCompat)
    implementation(Dependency.hilt)
    implementation(Dependency.hiltCompose)
    implementation(Dependency.coil)
    kapt(Dependency.hiltCompiler)
    kapt(Dependency.lifecycleCompiler)

    // Compose
    implementation(platform(Dependency.composeBom))
    implementation(Dependency.composeUi)
    implementation(Dependency.composeActivity)
    implementation(Dependency.composeTooling)
    implementation(Dependency.composeFoundation)
    implementation(Dependency.composeMaterial)
    implementation(Dependency.composeIcons)
    implementation(Dependency.composeIconsExtended)
    implementation(Dependency.composeNavigation)
    implementation(Dependency.constraintLayout)

    // Tests
    testImplementation(Dependency.mockito)
    testImplementation(Dependency.mockitoKotlin)
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
