object Version {
    // Build
    const val kotlin = "1.7.20"
    const val androidGradle = "7.3.1"
    const val buildKonfig = "0.13.3"

    // Native
    const val ktor = "2.1.2"
    const val coroutines = "1.6.4-native-mt"

    // Android
    const val hilt = "2.44"
    const val hiltCompose = "1.0.0"
    const val composeBom = "2022.10.00"
    const val composeCompiler = "1.3.2"
    const val appCompat = "1.5.1"
    const val material = "1.7.0"
    const val ktxCore = "1.9.0"
    const val ktxActivity = "1.6.0"
    const val lifecycle = "2.5.1"
    const val navigationCompose = "2.5.3"

    // Test
    const val junit = "4.13.2"
    const val kotest = "5.5.2"
    const val mockito = "4.8.1"
    const val mockitoKotlin = "4.0.0"
    const val turbine = "0.12.1"
    const val archTest = "2.1.0"
    const val coroutinesTest = "1.6.4"
}

object Dependency {
    // Native
    const val ktorCore = "io.ktor:ktor-client-core:${Version.ktor}"
    const val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
    const val ktorAuth = "io.ktor:ktor-client-auth:${Version.ktor}"
    const val ktorLog = "io.ktor:ktor-client-logging:${Version.ktor}"
    const val ktorSerialize = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"

    // Android
    const val ktorAndroid = "io.ktor:ktor-client-android:${Version.ktor}"
    const val ktxCore = "androidx.core:core-ktx:${Version.ktxCore}"
    const val ktxActvitiy = "androidx.activity:activity-ktx:${Version.ktxActivity}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycle}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val hiltCompose = "androidx.hilt:hilt-navigation-compose:${Version.hiltCompose}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Version.navigationCompose}"
    const val composeBom = "androidx.compose:compose-bom:${Version.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeFoundation = "androidx.compose.foundation:foundation"
    const val composeActivity = "androidx.activity:activity-compose"
    const val composeTooling = "androidx.compose.ui:ui-tooling"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeIcons = "androidx.compose.material:material-icons-core"
    const val composeIconsExtended = "androidx.compose.material:material-icons-extended"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata"

    // iOS
    const val ktoriOS = "io.ktor:ktor-client-ios:${Version.ktor}"

    // Test
    const val junit = "junit:junit:${Version.junit}"
    const val kotestAssert = "io.kotest:kotest-assertions-core:${Version.kotest}"
    const val mockito = "org.mockito:mockito-inline:${Version.mockito}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockitoKotlin}"
    const val turbine = "app.cash.turbine:turbine:${Version.turbine}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}"
    const val archTest = "androidx.arch.core:core-testing:${Version.archTest}"
}
