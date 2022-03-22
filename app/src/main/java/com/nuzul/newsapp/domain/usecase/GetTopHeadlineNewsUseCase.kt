package com.nuzul.newsapp.domain.usecase

import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlineNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
){
    suspend fun invoke(page: Int, country: String) : Flow<BaseResult<List<ArticleEntity>, ResponseNews>> {
        return newsRepository.getTopHeadlines(page, country)
    }
}