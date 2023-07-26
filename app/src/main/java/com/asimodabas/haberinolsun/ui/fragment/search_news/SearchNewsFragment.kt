package com.asimodabas.haberinolsun.ui.fragment.search_news

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.ui.activity.news.adapter.NewsAdapter
import com.asimodabas.haberinolsun.databinding.FragmentSearchNewsBinding
import com.asimodabas.haberinolsun.ui.activity.news.NewsActivity
import com.asimodabas.haberinolsun.util.Constants.QUERY_PAGE_SIZE
import com.asimodabas.haberinolsun.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.asimodabas.haberinolsun.util.Resource
import com.asimodabas.haberinolsun.util.viewBinding
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val viewModel: NewsViewModel by lazy { (requireActivity() as NewsActivity).viewModel }
    private val newsAdapter = NewsAdapter()
    private val binding by viewBinding(FragmentSearchNewsBinding::bind)

    private var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        searchNewsObserve()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, bundle
            )
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun searchNewsObserve() {
        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = (newsResponse.totalResults + (QUERY_PAGE_SIZE - 1)) / QUERY_PAGE_SIZE
                        val isLastPage = viewModel.searchNewsPageNumber >= totalPages
                        binding.rvSearchNews.setPadding(0, 0, 0, if (isLastPage) 0 else 20)
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
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount

                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
                    val shouldPaginate =
                        isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
                    if (shouldPaginate) {
                        viewModel.searchNews(binding.etSearch.text.toString())
                        isScrolling = false
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }
            })
        }
    }
}