package com.asimodabas.haberinolsun.repository

import com.asimodabas.haberinolsun.api.RetrofitInstance
import com.asimodabas.haberinolsun.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
}