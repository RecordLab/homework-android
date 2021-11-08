package com.recordlab.dailyscoop.network

import com.recordlab.dailyscoop.BuildConfig
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object RetrofitClient {
    val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    var retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(ResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val service: RequestService = retrofit.create(RequestService::class.java)
}