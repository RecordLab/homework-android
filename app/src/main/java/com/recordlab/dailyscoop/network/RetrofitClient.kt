package com.recordlab.dailyscoop.network

import android.text.TextUtils
import com.recordlab.dailyscoop.BuildConfig
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object RetrofitClient {
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    var client = OkHttpClient.Builder()
        .addInterceptor(interceptor = interceptor)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    var retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestService = retrofit.create(RequestService::class.java)



/*
    private val client = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(CookieManager()))

    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(ResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())

    private var retrofit: Retrofit = builder.build()

    fun createService(serviceClass: Class<RequestService>) : RequestService = createService(serviceClass, null)

    fun createService(serviceClass: Class<RequestService>, authToken: String?) : RequestService {
        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = AuthenticationInterceptor("Bearer $authToken")

            if (!client.interceptors().contains(interceptor)) {
                client.addInterceptor(interceptor)

                builder.client(client.build())
                retrofit = builder.build()
            }
        }
        return retrofit.create(serviceClass)
    }*/

//    val service: RequestService = retrofit.create(RequestService::class.java)
}