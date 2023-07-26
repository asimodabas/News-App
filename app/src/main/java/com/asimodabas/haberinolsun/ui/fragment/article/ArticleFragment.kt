package com.asimodabas.haberinolsun.ui.fragment.article

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.databinding.FragmentArticleBinding
import com.asimodabas.haberinolsun.ui.activity.news.NewsActivity
import com.asimodabas.haberinolsun.util.viewBinding
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val viewModel: NewsViewModel by lazy { (requireActivity() as NewsActivity).viewModel }
    private val binding by viewBinding(FragmentArticleBinding::bind)
    private val args by navArgs<ArticleFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            webViewClient = WebViewClient()
            args.article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(args.article)
            Toast.makeText(
                requireContext(), "Article saved successfully", Toast.LENGTH_SHORT
            ).show()
        }
    }
}