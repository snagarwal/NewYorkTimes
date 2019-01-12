package com.satya.newyorktimes.data.model

import com.google.gson.annotations.SerializedName

data class NewsFeed(
    val copyright: String,
    val last_updated: String,
    val num_results: Int,
    @SerializedName("results")
    val results: List<News>,
    val section: String,
    val status: String
)