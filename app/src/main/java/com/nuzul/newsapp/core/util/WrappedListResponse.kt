package com.nuzul.newsapp.core.util

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    var code: Int,
    @SerializedName("status") var status: String,
    @SerializedName("totalResults") var totalResult: Int,
    @SerializedName("articles") var data: List<T>? = null
    )

data class WrappedResponse<T>(
    var code: Int,
    @SerializedName("status") var status: String,
    @SerializedName("totalResults") var totalResult: Int,
    @SerializedName("articles") var data: T? = null
)