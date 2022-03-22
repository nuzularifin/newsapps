package com.nuzul.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResponseNews(
    @SerializedName("code") var statusCode: Int,
    @SerializedName("message") val message: String,
    val articles: List<Article>? = emptyList(),
    val status: String? = null,
    @SerializedName("sources") val sources: List<Source>? = emptyList(),
    val totalResults: Int = 0
)