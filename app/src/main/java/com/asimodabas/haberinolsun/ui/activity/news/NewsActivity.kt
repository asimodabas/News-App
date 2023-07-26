package com.asimodabas.haberinolsun.ui.activity.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.databinding.ActivityNewsBinding
import com.asimodabas.haberinolsun.db.room.ArticleDB
import com.asimodabas.haberinolsun.data.repository.NewsRepository
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel
import com.asimodabas.haberinolsun.viewmodel.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        val newsRepository = NewsRepository(ArticleDB.getInstance(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
    }
}
