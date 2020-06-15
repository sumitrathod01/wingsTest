package com.example.wings.network

import com.example.wings.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Sumit R@thod on 02-03-2017.
 */

object ApiClient {
    private var retrofit: Retrofit? = null
    private var gson: Gson? = null

    val client: Retrofit?
        get() {
            if (gson == null)
                gson = GsonBuilder()
                    .setLenient()
                    .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.base_url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson!!))
                    .build()

            }
            return retrofit
        }

}
