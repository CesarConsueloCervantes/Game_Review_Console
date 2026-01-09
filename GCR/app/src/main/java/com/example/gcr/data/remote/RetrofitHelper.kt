package com.example.gcr.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //private val BASE_URL ="http://10.0.2.2:3005/api/"

    private val BASE_URL = if (isEmulator()) "http://10.0.2.2:3005/api/" else "http://localhost:3005/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

fun isEmulator(): Boolean {
    return (android.os.Build.FINGERPRINT.startsWith("google/sdk_gphone") ||
            android.os.Build.MODEL.contains("Emulator") ||
            android.os.Build.PRODUCT.contains("sdk_gphone") ||
            android.os.Build.HARDWARE.contains("ranchu") ||
            android.os.Build.MANUFACTURER.contains("Google") && android.os.Build.PRODUCT.contains("sdk"))
}