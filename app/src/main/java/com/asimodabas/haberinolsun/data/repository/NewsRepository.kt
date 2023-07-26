package com.asimodabas.haberinolsun.data.repository

import com.asimodabas.haberinolsun.data.service.NetworkInstance
import com.asimodabas.haberinolsun.db.room.ArticleDB
import com.asimodabas.haberinolsun.domain.model.Article

class NewsRepository(
    private val articleDB: ArticleDB
) {
    suspend fun getBreakingNews(country: String, page: Int) = NetworkInstance.api.getBreakingNews(country, page)

    suspend fun searchForNews(searchQuery: String, page: Int) = NetworkInstance.api.getSearchForNews(searchQuery, page)

    suspend fun upsert(article: Article) = articleDB.articleDao().insertArticle(article)

    suspend fun deleteNews(article: Article) = articleDB.articleDao().deleteArticle(article)

    fun getSavedNews() = articleDB.articleDao().getAllArticles()
}