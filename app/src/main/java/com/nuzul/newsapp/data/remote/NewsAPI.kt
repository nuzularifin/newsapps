package com.nuzul.newsapp.data.remote

import com.nuzul.newsapp.core.util.WrappedListResponse
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/everything")
    suspend fun getNewsList(
        @Header("X-Api-Key") xApiKey: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page : Int? = null,
        @Query("q") source: String? = null
    ): Response<ResponseNews>

    @GET("v2/top-headlines")
    suspend fun getTopNewsList(
        @Header("X-Api-Key") xApiKey: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page : Int? = null,
        @Query("country") country: String? = null
    ): Response<ResponseNews>
}