package com.nuzul.newsapp.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nuzul.newsapp.R
import com.nuzul.newsapp.core.util.Constants.FRAGMENT_KEY
import com.nuzul.newsapp.core.util.Constants.TOP_SOURCE_KEY
import com.nuzul.newsapp.databinding.FragmentNewsCategoriesBinding
import com.nuzul.newsapp.presentation.adapter.CategoryAdapter

class NewsCategoriesFragment : Fragment(R.layout.fragment_news_categories) {

    private var _binding: FragmentNewsCategoriesBinding? = null
    private val binding get() = _binding!!

    private val itemList: Array<String>
        get() = arrayOf(
            "Everything",
            "Top - Headline",
            "Business",
            "Entertainment",
            "General",
            "Health",
            "Science",
            "Sports",
            "Technology"
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewsCategoriesBinding.bind(view)

        setupRecyclerCategory()
    }

    private fun setupRecyclerCategory() {
        val mAdapter = CategoryAdapter(itemList, CategoryAdapter.OnClickListener {
            val b = Bundle()
            if (it == "Everything" || it == "Top - Headline"){
                b.putString(TOP_SOURCE_KEY, it)
                findNavController().navigate(R.id.action_newsCategoriesFragment_to_NewsListFragment, b)
            } else {
                b.putString(FRAGMENT_KEY, it)
                findNavController().navigate(R.id.action_newsCategoriesFragment_to_sourceNewsFragment, b)
            }
        })

        binding.categoryRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

}