package com.example.echolock.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.215.141.28/echolock_api/"

    // Lenient Gson (important during development)
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // OkHttpClient with timeouts
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val instance: ApiService by lazy {
        try {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        } catch (e: Exception) {
            android.util.Log.e("RetrofitClient", "Error creating Retrofit instance", e)
            throw e
        }
    }
}
