package com.annevonwolffen.network_api

import com.annevonwolffen.di.Dependency
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface NetworkApi : Dependency {
    val okHttpClient: OkHttpClient
    val retrofitClient: Retrofit
}