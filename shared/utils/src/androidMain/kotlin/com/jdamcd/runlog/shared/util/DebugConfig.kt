package com.jdamcd.runlog.shared.util

import com.jdamcd.runlog.shared.utils.BuildConfig

actual object DebugConfig {
    actual val isDebug = BuildConfig.DEBUG
}
