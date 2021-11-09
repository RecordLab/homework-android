package com.recordlab.dailyscoop.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

public class AuthenticationInterceptor(val token: String) : Interceptor {
    private val authToken: String = token
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", authToken)

        val request: Request = builder.build()
        return chain.proceed(request)
    }

}