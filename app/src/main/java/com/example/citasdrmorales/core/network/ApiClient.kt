package com.example.citasdrmorales.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://5295aedb9f234add97ec185370e4f3db.api.mockbin.io"

    private val loggin = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(loggin).build()

    val DoctorApi: DoctorAPI by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            DoctorAPI::class.java)
    }
}