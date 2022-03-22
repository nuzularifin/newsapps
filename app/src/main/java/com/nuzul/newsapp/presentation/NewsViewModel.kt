package com.nuzul.newsapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuzul.newsapp.core.BaseResult
import com.nuzul.newsapp.data.remote.dto.Article
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.domain.usecase.GetNewsListUseCase
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
    private val getTopHeadlineNewsUseCase: GetTopHeadlineNewsUseCase
) : ViewModel() {

    private var _page = 1
    private val page: Int get() = _page

    private var _q =  "bitcoin"
    private val q: String get() = _q

    private var _country = "us"
    private val mCountry: String get() = _country

    private val _searchQuery = MutableStateFlow("")
    val mSearchQuery: StateFlow<String> get() = _searchQuery

    private var searchJob: Job? = null

    private val _state = MutableStateFlow<NewsState>(NewsState.Init(page, q))
    val mState: StateFlow<NewsState> get() = _state

    private val _articles = MutableStateFlow<List<ArticleEntity>>(mutableListOf())
    val mArticles : StateFlow<List<ArticleEntity>> get() = _articles

    private fun showToast(message: String){
        _state.value = NewsState.ShowToast(message)
    }
    init {
//        fetchAllArticles(page, q)
        fetchTopHeadLinesArticles(page, mCountry)
    }

    private fun showLoading(){
        _state.value = NewsState.IsLoading(true)
    }
    private fun dismissLoading(){
        _state.value = NewsState.IsLoading(false)
    }

    private fun fetchTopHeadLinesArticles(page: Int, country: String){
        viewModelScope.launch {
            getTopHeadlineNewsUseCase.invoke(page, country).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                Log.d("Exceptions", "fetchAllArticles: "+exception.message.toString())
                showToast(exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        Log.d("Exceptions", "fetchTopHeadLinesArticles: "+result.rawResponse.status)
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }

    private fun fetchAllArticles(page: Int, q: String) {
        viewModelScope.launch {
            getNewsListUseCase.invoke(page, q).onStart {
                showLoading()
            }.catch { exception ->
                dismissLoading()
                Log.d("Exceptions", "fetchAllArticles: "+exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                      dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        Log.d("Exceptions", "fetchAllArticles: "+result.rawResponse.status)
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
                print(exception.message.toString())
                Log.d("Exceptions", "fetchAllArticles: "+exception.message.toString())
            }.collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        dismissLoading()
                        _articles.value = result.data
                    }
                    is BaseResult.Error -> {
                        dismissLoading()
                        Log.d("Exceptions", "fetchAllArticles: "+result.rawResponse.status)
                        showToast(result.rawResponse.status ?: "An expected error ocurred")
                    }
                }
            }
        }
    }
}

sealed class NewsState {
    class Init(page: Int, q: String) : NewsState()
    data class IsLoading(val isLoading: Boolean) : NewsState()
    data class ShowToast(val message : String) : NewsState()
}