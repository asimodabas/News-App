package com.asimodabas.haberinolsun.ui

import androidx.lifecycle.ViewModel
import com.asimodabas.haberinolsun.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
}