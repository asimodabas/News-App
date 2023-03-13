package com.asimodabas.haberinolsun.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.asimodabas.haberinolsun.databinding.FragmentArticleBinding
import com.asimodabas.haberinolsun.ui.activity.NewsActivity
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {

    val args: ArticleFragmentArgs by navArgs()
    lateinit var viewModel: NewsViewModel
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(article.url)
            }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}