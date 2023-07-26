package com.asimodabas.haberinolsun.data.service

import com.asimodabas.haberinolsun.domain.model.NewsResponse
import com.asimodabas.haberinolsun.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines?apiKey=$API_KEY")
    suspend fun getBreakingNews(
        @Query("country") country: String = "in",
        @Query("page") pageNumber: Int = 1
    ): Response<NewsResponse>

    @GET("v2/top-headlines?apiKey=$API_KEY")
    suspend fun getSearchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1
    ): Response<NewsResponse>
}
