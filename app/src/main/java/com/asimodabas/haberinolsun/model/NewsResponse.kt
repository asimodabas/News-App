package com.androiddevs.mvvmnewsapp.models


import com.asimodabas.haberinolsun.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)