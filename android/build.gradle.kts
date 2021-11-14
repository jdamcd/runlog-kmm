plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("dagger.hilt.android.plugin")
}
group = "com.jdamcd.runlog"
version = AppVersion.name

dependencies {
    implementation(project(":shared:main"))
    implementation(Dependency.ktxCore)
    implementation(Dependency.ktxActvitiy)
    implementation(Dependency.viewModel)
    implementation(Dependency.liveData)
    implementation(Dependency.lifecycleExtensions)
    implementation(Dependency.material)
    implementation(Dependency.appCompat)
    implementation(Dependency.hilt)
    implementation(Dependency.hiltViewModel)
    kapt(Dependency.hiltCompiler)
    kapt(Dependency.hiltAndroidCompiler)
    kapt(Dependency.lifecycleCompiler)

    // Compose
    implementation(Dependency.composeUi)
    implementation(Dependency.composeActivity)
    implementation(Dependency.composeTooling)
    implementation(Dependency.composeFoundation)
    implementation(Dependency.composeMaterial)
    implementation(Dependency.composeIcons)
    implementation(Dependency.composeIconsExtended)
    implementation(Dependency.composeLiveData)

    // Tests
    testImplementation(Dependency.mockito)
    testImplementation(Dependency.mockitoKotlin)
    testImplementation(Dependency.coroutinesTest)
    testImplementation(Dependency.archTest)
}
android {
    compileSdkVersion(AndroidVersion.target)
    defaultConfig {
        applicationId = "com.jdamcd.runlog.android"
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        useIR = true
    }
    composeOptions {
        kotlinCompilerVersion = Version.kotlin
        kotlinCompilerExtensionVersion = Version.compose
    }
    lintOptions {
        isAbortOnError = false
    }
}
