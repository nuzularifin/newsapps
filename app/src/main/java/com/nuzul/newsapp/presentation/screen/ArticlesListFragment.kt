package com.nuzul.newsapp.presentation.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nuzul.newsapp.R
import com.nuzul.newsapp.core.util.Constants
import com.nuzul.newsapp.core.util.Constants.FRAGMENT_KEY
import com.nuzul.newsapp.core.util.Constants.SOURCE_KEY
import com.nuzul.newsapp.core.util.Constants.TOP_SOURCE_KEY
import com.nuzul.newsapp.core.util.Constants.URL_WEB_VIEW
import com.nuzul.newsapp.databinding.FragmentArticlesListBinding
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.presentation.NewsState
import com.nuzul.newsapp.presentation.NewsViewModel
import com.nuzul.newsapp.presentation.adapter.ArticleAdapter
import com.nuzul.newsapp.presentation.extension.gone
import com.nuzul.newsapp.presentation.extension.showToast
import com.nuzul.newsapp.presentation.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ArticlesListFragment() : Fragment(R.layout.fragment_articles_list) {

    private var _binding: FragmentArticlesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var key: String
    private lateinit var idSource: String

    private val viewModel: NewsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArticlesListBinding.bind(view)
        setupRecycler()
        observe()

        arguments?.let {
            if (requireArguments().getString(SOURCE_KEY)?.isNotEmpty() == true) {
                key = requireArguments().getString(SOURCE_KEY).toString()
                val article = Gson().fromJson(key, ArticleEntity::class.java)
                idSource = article.id ?: ""
                Log.d("Arguments", "onViewCreated: $key + $idSource")
            } else {
                key = requireArguments().getString(TOP_SOURCE_KEY).toString()
            }
        }

        if (key == Constants.EVERYTHING_KEY) {
            viewModel.fetchAllArticles(1, "bitcoin")
        } else if (key == Constants.TOP_HEADLINE_KEY) {
            viewModel.fetchTopHeadLinesArticles(1, "us")
        } else {
            viewModel.fetchNewsArticlesBySource(1, idSource)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (it.length > 2){
                        viewModel.searchNews(page = 1, it)
                        return true
                    }
                }
               return false
            }
        })
    }

    private fun observe() {
        observeState()
        observeArticles()
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleState(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeArticles() {
        viewModel.mArticles.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { articles ->
                handleProducts(articles)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleProducts(articles: List<ArticleEntity>) {
        binding.articlesRecyclerView.adapter?.let {
            if (it is ArticleAdapter) {
                it.updateList(articles)
            }
        }
    }

    private fun handleState(state: NewsState) {
        when (state) {
            is NewsState.IsLoading -> handleLoading(state.isLoading)
            is NewsState.ShowToast -> requireActivity().showToast(state.message)
            is NewsState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar.visible()
            binding.articlesRecyclerView.gone()
        } else {
            binding.loadingProgressBar.gone()
            binding.articlesRecyclerView.visible()
        }
    }

    private fun setupRecycler() {
        val mAdapter = ArticleAdapter(mutableListOf(), ArticleAdapter.OnClickListener {
            val b = Bundle()
            b.putString(URL_WEB_VIEW, it)
            findNavController().navigate(R.id.action_NewsListFragment_to_webViewFragment, b)
        })
        binding.articlesRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}