package com.asimodabas.haberinolsun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimodabas.haberinolsun.domain.model.Article
import com.asimodabas.haberinolsun.domain.model.NewsResponse
import com.asimodabas.haberinolsun.data.repository.NewsRepository
import com.asimodabas.haberinolsun.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>> get() = _breakingNews

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> get() = _searchNews

    var breakingNewsPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    var searchNewsPageNumber = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(country: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(country, breakingNewsPageNumber)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(queryString: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = repository.searchForNews(queryString, searchNewsPageNumber)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPageNumber++
                if (breakingNewsResponse == null) breakingNewsResponse = resultResponse
                else breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPageNumber++
                if (searchNewsResponse == null) searchNewsResponse = resultResponse
                else searchNewsResponse?.articles?.addAll(resultResponse.articles)
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getArticles() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }
}
