package com.nuzul.newsapp.domain.repository

import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import com.nuzul.newsapp.domain.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNewsList(page: Int, q: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>

    suspend fun getTopHeadlines(page: Int, country: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>

    suspend fun getNewsSources(page: Int, category: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>

    suspend fun getNewsArticles(page: Int, source: String): Flow<BaseResult<List<ArticleEntity>, ResponseNews>>
}