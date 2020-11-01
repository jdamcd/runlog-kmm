object Version {
    // Build
    const val kotlin = "1.4.10"
    const val androidGradle = "4.2.0-alpha15"
    const val buildKonfig = "0.7.0"
    // Native
    const val ktor = "1.4.1"
    const val coroutines = "1.3.9-native-mt-2"
    const val kotlinSerialize = "1.0.0-RC2"
    const val stately = "1.1.0"
    // Android
    const val hilt = "2.28-alpha"
    const val hiltAndroid = "1.0.0-alpha02"
    const val compose = "1.0.0-alpha05"
    const val appCompat = "1.2.0"
    const val material = "1.2.1"
    const val ktxCore = "1.3.2"
    const val ktxActivity = "1.1.0"
    const val lifecycle = "2.2.0"
    // Test
    const val junit = "4.12"
    const val kotest = "4.2.5"
    const val mockito = "3.0.0"
    const val mockitoKotlin = "2.1.0"
    const val archTest = "2.1.0"
    const val coroutinesTest = "1.4.0"
}

object Dependency {
    // Native
    const val ktorCore = "io.ktor:ktor-client-core:${Version.ktor}"
    const val ktorSerialize = "io.ktor:ktor-client-serialization:${Version.ktor}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val kotlinSerialize = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.kotlinSerialize}"
    const val stately = "co.touchlab:stately-common:${Version.stately}"
    const val statelyConcurrency = "co.touchlab:stately-concurrency:${Version.stately}"
    // Android
    const val ktorAndroid = "io.ktor:ktor-client-android:${Version.ktor}"
    const val ktxCore = "androidx.core:core-ktx:${Version.ktxCore}"
    const val ktxActvitiy = "androidx.activity:activity-ktx:${Version.ktxActivity}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Version.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycle}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Version.hiltAndroid}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val hiltAndroidCompiler = "androidx.hilt:hilt-compiler:${Version.hiltAndroid}"
    const val composeUi = "androidx.compose.ui:ui:${Version.compose}"
    const val composeTooling = "androidx.ui:ui-tooling:${Version.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Version.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
    const val composeIcons = "androidx.compose.material:material-icons-core:${Version.compose}"
    const val composeIconsExtended = "androidx.compose.material:material-icons-extended:${Version.compose}"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${Version.compose}"
    const val composeTest = "androidx.ui:ui-test:${Version.compose}"
    // iOS
    const val ktoriOS = "io.ktor:ktor-client-ios:${Version.ktor}"
    // Test
    const val junit = "junit:junit:${Version.junit}"
    const val kotestAssert = "io.kotest:kotest-assertions-core:${Version.kotest}"
    const val mockito = "org.mockito:mockito-inline:${Version.mockito}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Version.mockitoKotlin}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}"
    const val archTest = "androidx.arch.core:core-testing:${Version.archTest}"
}
