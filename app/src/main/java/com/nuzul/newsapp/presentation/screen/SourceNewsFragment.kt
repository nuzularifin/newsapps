package com.nuzul.newsapp.presentation.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.nuzul.newsapp.databinding.FragmentArticlesListBinding
import com.nuzul.newsapp.domain.entity.ArticleEntity
import com.nuzul.newsapp.presentation.NewsState
import com.nuzul.newsapp.presentation.NewsViewModel
import com.nuzul.newsapp.presentation.adapter.ArticleAdapter
import com.nuzul.newsapp.presentation.adapter.SourceAdapter
import com.nuzul.newsapp.presentation.extension.gone
import com.nuzul.newsapp.presentation.extension.showToast
import com.nuzul.newsapp.presentation.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SourceNewsFragment() : Fragment(R.layout.fragment_articles_list) {

    private var _binding: FragmentArticlesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var key: String

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArticlesListBinding.bind(view)
        setupRecycler()
        observe()

        arguments?.let {
            key = requireArguments().getString(Constants.FRAGMENT_KEY).toString().lowercase()
            Log.d("Arguments", "onViewCreated: $key")
        }

        viewModel.fetchNewsSourceByCategory(1, key)

        binding.searchView.visibility = View.GONE
    }

    private fun observe() {
        observeState()
        observeArticles()
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).onEach {
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
            if (it is SourceAdapter) {
                it.updateList(articles)
            }
        }
    }

    private fun handleState(state: NewsState){
        when(state){
            is NewsState.IsLoading -> handleLoading(state.isLoading)
            is NewsState.ShowToast ->
            {
                binding.loadingProgressBar.gone()
                binding.errorLayout.visible()
                binding.articlesRecyclerView.gone()
                requireActivity().showToast(state.message)
            }
            is NewsState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visible()
            binding.articlesRecyclerView.gone()
            binding.errorLayout.gone()
        }else{
            binding.loadingProgressBar.gone()
            binding.errorLayout.gone()
            binding.articlesRecyclerView.visible()
        }
    }

    private fun setupRecycler() {
        val mAdapter = SourceAdapter(mutableListOf(), SourceAdapter.OnClickListener {
            Log.d("SourceAdapterItem", "setupRecycler: ${it.source}")
            val b = Bundle()
            b.putString(Constants.SOURCE_KEY, Gson().toJson(it))
            findNavController().navigate(R.id.action_sourceNewsFragment_to_NewsListFragment, b)
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