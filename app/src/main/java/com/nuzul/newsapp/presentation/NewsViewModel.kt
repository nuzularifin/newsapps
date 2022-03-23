package com.nuzul.newsapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.domain.usecase.GetNewsListUseCase
import com.nuzul.newsapp.domain.usecase.GetNewsSourcesUseCase
import com.nuzul.newsapp.domain.usecase.GetTopHeadlineArticlesUseCase
import com.nuzul.newsapp.domain.usecase.GetTopHeadlineNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase,
    private val getTopHeadlineNewsUseCase: GetTopHeadlineNewsUseCase,
    private val getNewsSourcesUseCase: GetNewsSourcesUseCase,
    private val getTopHeadlineArticlesUseCase: GetTopHeadlineArticlesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val mSearchQuery: StateFlow<String> get() = _searchQuery

    private var searchJob: Job? = null

    private var loadMoreJob: Job? = null

    private val _loadMore = MutableStateFlow(true)
    val loadMore: StateFlow<Boolean> get() = _loadMore

    private val _state = MutableStateFlow<NewsState>(NewsState.Init)
    val mState: StateFlow<NewsState> get() = _state

    private val _articles = MutableStateFlow<List<ArticleEntity>>(mutableListOf())
    val mArticles: StateFlow<List<ArticleEntity>> get() = _articles


    private fun showToast(message: String) {
        _state.value = NewsState.ShowToast(message)
    }

    private fun showLoading() {
        _state.value = NewsState.IsLoading(true)
    }

    private fun dismissLoading() {
        _state.value = NewsState.IsLoading(false)
    }

    fun fetchTopHeadLinesArticles(page: Int, country: String) {
        viewModelScope.launch {
            getTopHeadlineNewsUseCase.invoke(page, country).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }

    fun fetchNewsArticlesBySource(page: Int, source: String) {
        viewModelScope.launch {
            getTopHeadlineArticlesUseCase.invoke(page, source).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }

    fun fetchNewsSourceByCategory(page: Int, category: String) {
        viewModelScope.launch {
            getNewsSourcesUseCase.invoke(page, category).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }

    fun fetchAllArticles(page: Int, q: String) {
        viewModelScope.launch {
            getNewsListUseCase.invoke(page, q).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                        _loadMore.value = result.data.size > 10
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }

    fun searchNews(page: Int, q: String) {
        _searchQuery.value = q
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1500L)
            getNewsListUseCase.invoke(page, q).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }
}

sealed class NewsState {
    object Init : NewsState()
    data class IsLoading(val isLoading: Boolean) : NewsState()
    data class ShowToast(val message: String) : NewsState()
}