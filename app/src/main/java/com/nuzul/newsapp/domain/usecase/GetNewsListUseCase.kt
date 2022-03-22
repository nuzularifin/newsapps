package com.nuzul.newsapp.domain.usecase

import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.core.util.WrappedListResponse
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend fun invoke(page: Int, q: String) : Flow<BaseResult<List<ArticleEntity>, ResponseNews>> {
        return newsRepository.getNewsList(page, q)
    }
}