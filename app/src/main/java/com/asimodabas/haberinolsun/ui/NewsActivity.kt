package com.asimodabas.haberinolsun.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.databinding.ActivityNewsBinding
import com.asimodabas.haberinolsun.db.ArticleDatabase
import com.asimodabas.haberinolsun.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.newsNavHostFragment))
    }
}