package com.example.kotlinsubmission2.network


import com.example.kotlinsubmission2.helper.Helper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object{
        private lateinit var retrofit : Retrofit
        private val logging =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val httpClient = OkHttpClient.Builder().addInterceptor(logging)

        fun<T> createService(serviceClass : Class<T>): T {
                retrofit = Retrofit.Builder()
                    .baseUrl(Helper.BASIC_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            return retrofit.create(serviceClass)
        }
    }
}