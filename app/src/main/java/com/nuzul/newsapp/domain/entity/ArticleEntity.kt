package com.nuzul.newsapp.domain.entity

import com.nuzul.newsapp.data.remote.dto.Source

data class ArticleEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)