package com.recordlab.dailyscoop.network

sealed class Result<T> {
    data class Success<T>(val code: Int, val data: T) : Result<T>()

//    class Loading<T> : Result<T>()

    data class ApiError<T>(val code: Int, val message: String) : Result<T>()

    data class NetworkError<T>(val throwable: Throwable) : Result<T>()

//    data class UnknownApiError<T>(val throwable: Throwable) : Result<T>()

    class NullResult<T> : Result<T>()
}
