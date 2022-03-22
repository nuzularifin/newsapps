package com.nuzul.newsapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.core.util.Constants
import com.nuzul.newsapp.data.remote.NewsAPI
import com.nuzul.newsapp.data.remote.dto.ResponseNews
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsAPI: NewsAPI
) : NewsRepository {

    override suspend fun getNewsList(
        page: Int,
        q: String
    ): Flow<BaseResult<List<ArticleEntity>, ResponseNews>> {
        return flow {
            val response = newsAPI.getNewsList(
                xApiKey = Constants.API_KEY, apiKey = Constants.API_KEY,
                page = page, q
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                val products = mutableListOf<ArticleEntity>()

                body.articles?.forEach {
                    products.add(
                        ArticleEntity(
                            author = it.author,
                            content = it.content,
                            description = it.description,
                            publishedAt = it.publishedAt,
                            source = it.source,
                            title = it.title,
                            url = it.url,
                            urlToImage = it.urlToImage
                        )
                    )
                }
                emit(BaseResult.Success(products))
            } else {
                val type = object : TypeToken<ResponseNews>() {}.type
                val err = Gson().fromJson<ResponseNews>(
                    response.errorBody()!!.charStream(), type
                )!!
                err.statusCode = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getTopHeadlines(
        page: Int,
        country: String
    ): Flow<BaseResult<List<ArticleEntity>, ResponseNews>> {
        return flow {
            val response = newsAPI.getTopNewsList(
                xApiKey = Constants.API_KEY, apiKey = Constants.API_KEY,
                page = page, country
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                val products = mutableListOf<ArticleEntity>()

                body.articles?.forEach {
                    products.add(
                        ArticleEntity(
                            author = it.author,
                            content = it.content,
                            description = it.description,
                            publishedAt = it.publishedAt,
                            source = it.source,
                            title = it.title,
                            url = it.url,
                            urlToImage = it.urlToImage
                        )
                    )
                }
                emit(BaseResult.Success(products))
            } else {
                val type = object : TypeToken<ResponseNews>() {}.type
                val err = Gson().fromJson<ResponseNews>(
                    response.errorBody()!!.charStream(), type
                )!!
                err.statusCode = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

}