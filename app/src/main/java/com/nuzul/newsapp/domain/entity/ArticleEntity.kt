package com.nuzul.newsapp.domain.entity

import com.nuzul.newsapp.data.remote.dto.Source

data class ArticleEntity(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,

    val id: String? = null,
    val name: String? = null,
    val category: String? = null,
    val language: String? = null,
    val country: String? = null

)