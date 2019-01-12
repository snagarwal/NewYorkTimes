package com.satya.newyorktimes.data.api

import android.arch.lifecycle.LiveData
import com.satya.newyorktimes.data.model.NewsFeed
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/bins/nl6jh")
    fun getNewsFeed(): LiveData<ApiResponse<NewsFeed>>


}