package com.jdamcd.runlog.shared.util

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual object DebugConfig {
    actual val isDebug = Platform.isDebugBinary
}
