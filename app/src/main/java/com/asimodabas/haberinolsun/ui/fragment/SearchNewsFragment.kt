package com.asimodabas.haberinolsun.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.adapter.NewsAdapter
import com.asimodabas.haberinolsun.databinding.FragmentSearchNewsBinding
import com.asimodabas.haberinolsun.ui.activity.NewsActivity
import com.asimodabas.haberinolsun.util.Constants
import com.asimodabas.haberinolsun.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.asimodabas.haberinolsun.util.Resource
import com.asimodabas.haberinolsun.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.itemErrorMessage
import kotlinx.android.synthetic.main.fragment_search_news.etSearch
import kotlinx.android.synthetic.main.item_error_message.btnRetry
import kotlinx.android.synthetic.main.item_error_message.tvErrorMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, bundle
            )
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgessBar()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgessBar()
                    it.message?.let {
                        Toast.makeText(activity, "Error : $it", Toast.LENGTH_SHORT).show()
                        showErrorMessage(it)
                    }
                }
                is Resource.Loading -> {
                    showProgessBar()
                }
            }
        })
        btnRetry.setOnClickListener {
            if (etSearch.text.toString().isNotEmpty()) {
                viewModel.getSearchNews(etSearch.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun hideProgessBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgessBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        isError = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    var isLoading = false
    var isLastPage = false
    var isError = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getSearchNews(binding.etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}