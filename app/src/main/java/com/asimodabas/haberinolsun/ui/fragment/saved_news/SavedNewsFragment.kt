package com.asimodabas.haberinolsun.ui.fragment.saved_news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.ui.activity.news.adapter.NewsAdapter
import com.asimodabas.haberinolsun.databinding.FragmentSavedNewsBinding
import com.asimodabas.haberinolsun.ui.activity.news.NewsActivity
import com.asimodabas.haberinolsun.util.viewBinding
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private val viewModel: NewsViewModel by lazy { (requireActivity() as NewsActivity).viewModel }
    private val newsAdapter = NewsAdapter()
    private val binding by viewBinding(FragmentSavedNewsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply { putSerializable("article", article) }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
        }

        viewModel.getArticles().observe(viewLifecycleOwner) { articles ->
            newsAdapter.differ.submitList(articles)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}