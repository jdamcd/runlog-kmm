object Version {
    // Build
    const val kotlin = "1.8.0"
    const val androidGradle = "7.4.0"
    const val buildKonfig = "0.13.3"

    // Native
    const val ktor = "2.1.2"
    const val coroutines = "1.6.4-native-mt"
    const val koin = "3.2.2"
    const val sqldelight = "1.5.5"

    // Android
    const val hilt = "2.44"
    const val hiltCompose = "1.0.0"
    const val composeBom = "2022.10.00"
    const val composeCompiler = "1.4.0"
    const val constraintLayout = "1.0.1"
    const val material = "1.7.0"
    const val splash = "1.0.0"
    const val ktxCore = "1.9.0"
    const val ktxActivity = "1.6.0"
    const val lifecycle = "2.5.1"
    const val navigationCompose = "2.5.3"
    const val coil = "2.2.2"

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
    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Version.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
        const val auth = "io.ktor:ktor-client-auth:${Version.ktor}"
        const val log = "io.ktor:ktor-client-logging:${Version.ktor}"
        const val serialize = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
        const val ios = "io.ktor:ktor-client-ios:${Version.ktor}"
        const val android = "io.ktor:ktor-client-android:${Version.ktor}"
    }
    object Koin {
        const val core = "io.insert-koin:koin-core:${Version.koin}"
        const val android = "io.insert-koin:koin-android:${Version.koin}"
    }
    object SqlDelight {
        const val android = "com.squareup.sqldelight:android-driver:${Version.sqldelight}"
        const val native = "com.squareup.sqldelight:native-driver:${Version.sqldelight}"
        const val jvm = "com.squareup.sqldelight:sqlite-driver:${Version.sqldelight}"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}"
    }
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"

    // Android
    object Ktx {
        const val core = "androidx.core:core-ktx:${Version.ktxCore}"
        const val activity = "androidx.activity:activity-ktx:${Version.ktxActivity}"
    }
    object Lifecycle {
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
        const val compiler = "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycle}"
    }
    object Hilt {
        const val core = "com.google.dagger:hilt-android:${Version.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
        const val compose = "androidx.hilt:hilt-navigation-compose:${Version.hiltCompose}"
    }
    object Compose {
        const val bom = "androidx.compose:compose-bom:${Version.composeBom}"
        const val ui = "androidx.compose.ui:ui"
        const val foundation = "androidx.compose.foundation:foundation"
        const val activity = "androidx.activity:activity-compose"
        const val tooling = "androidx.compose.ui:ui-tooling"
        const val material = "androidx.compose.material:material"
        const val icons = "androidx.compose.material:material-icons-core"
        const val iconsExtended = "androidx.compose.material:material-icons-extended"
        const val navigation = "androidx.navigation:navigation-compose:${Version.navigationCompose}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Version.constraintLayout}"
    }
    const val material = "com.google.android.material:material:${Version.material}"
    const val splash = "androidx.core:core-splashscreen:${Version.splash}"
    const val coil = "io.coil-kt:coil-compose:${Version.coil}"

    // Test
    object Mockito {
        const val core = "org.mockito:mockito-inline:${Version.mockito}"
        const val kotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockitoKotlin}"
    }
    const val junit = "junit:junit:${Version.junit}"
    const val kotestAssert = "io.kotest:kotest-assertions-core:${Version.kotest}"
    const val turbine = "app.cash.turbine:turbine:${Version.turbine}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}"
    const val archTest = "androidx.arch.core:core-testing:${Version.archTest}"
}
