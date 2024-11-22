package com.jdamcd.runlog.shared.util

sealed class Result<out T> {
    data class Data<T>(val value: T) : Result<T>()
    data class Error<Nothing>(val error: Throwable) : Result<Nothing>()
    object Empty : Result<Nothing>()
}

inline fun <reified T> Result<T>.ifSuccess(callback: (value: T) -> Unit) {
    if (this is Result.Data) {
        callback(value)
    }
}

inline fun <reified T> Result<T>.ifError(callback: (error: Throwable) -> Unit) {
    if (this is Result.Error) {
        callback(error)
    }
}

enum class RefreshState {
    LOADING,
    SUCCESS,
    ERROR
}

inline fun <T> tryCall(call: () -> Result<T>): Result<T> = try {
    call.invoke()
} catch (error: Throwable) {
    Result.Error(error)
}
