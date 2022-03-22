package com.nuzul.newsapp.data.remote.dto

data class Source(
    val id: String? = null,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?,
)