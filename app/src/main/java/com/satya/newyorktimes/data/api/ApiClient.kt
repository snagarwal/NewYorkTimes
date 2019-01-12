package com.satya.newyorktimes.data.api

import com.google.gson.GsonBuilder
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.NewsFeed
import com.satya.newyorktimes.data.util.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provider class for retrofit instance.
 */
object ApiClient {

    fun getRetrofitClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
            .registerTypeAdapter(News::class.java, NewsDeserializer())
            .create()
        return Retrofit.Builder().baseUrl("https://api.myjson.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()

    }
}