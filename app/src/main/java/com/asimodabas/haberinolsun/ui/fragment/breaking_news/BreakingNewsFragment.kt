package com.asimodabas.haberinolsun.ui.fragment.breaking_news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.ui.activity.news.adapter.NewsAdapter
import com.asimodabas.haberinolsun.databinding.FragmentBreakingNewsBinding
import com.asimodabas.haberinolsun.ui.activity.news.NewsActivity
import com.asimodabas.haberinolsun.util.Constants.QUERY_PAGE_SIZE
import com.asimodabas.haberinolsun.util.Resource
import com.asimodabas.haberinolsun.util.viewBinding
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: NewsViewModel by lazy { (requireActivity() as NewsActivity).viewModel }
    private val newsAdapter = NewsAdapter()
    private val binding by viewBinding(FragmentBreakingNewsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        breakingNewsObserve()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }
    }

    private fun breakingNewsObserve() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        val totalPages =
                            (newsResponse.totalResults + (QUERY_PAGE_SIZE - 1)) / QUERY_PAGE_SIZE
                        val isLastPage = viewModel.breakingNewsPageNumber >= totalPages
                        binding.rvBreakingNews.setPadding(0, 0, 0, if (isLastPage) 0 else 20)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(
                            requireContext(), "An error occurred $it", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}