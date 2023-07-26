package com.asimodabas.haberinolsun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asimodabas.haberinolsun.data.repository.NewsRepository

class NewsViewModelProviderFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}