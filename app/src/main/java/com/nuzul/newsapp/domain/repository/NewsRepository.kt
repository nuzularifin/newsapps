package com.nuzul.newsapp.domain.repository

import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.core.util.WrappedListResponse
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import com.nuzul.newsapp.domain.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {

    suspend fun getNewsList(page: Int, q: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>

    suspend fun getTopHeadlines(page: Int, country: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>
}