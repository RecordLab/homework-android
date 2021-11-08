package com.recordlab.dailyscoop.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseAdapter<T>(private val responseType: Type) : CallAdapter<T, Call<Result<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<Result<T>> = ResponseCall(call)

}

class ResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) return null
        check(returnType is ParameterizedType)

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Result::class.javaObjectType) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val successType = getParameterUpperBound(0, responseType)

        return ResponseAdapter<Any>(successType)
    }
}