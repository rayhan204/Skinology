package com.example.skinology.data.remote.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor{ chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    Log.d("Retrofit", "Request: $request")
                    Log.d("Retrofit", "Response: $response")
                    response}
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://articles-api-646980359508.asia-southeast2.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}