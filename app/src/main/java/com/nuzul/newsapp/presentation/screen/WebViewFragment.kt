package com.nuzul.newsapp.presentation.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nuzul.newsapp.R
import com.nuzul.newsapp.core.util.Constants
import com.nuzul.newsapp.databinding.FragmentWebViewBinding


class WebViewFragment() : Fragment(R.layout.fragment_web_view) {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private var url: String = ""

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

        arguments?.let {
            if (requireArguments().getString(Constants.URL_WEB_VIEW)?.isNotEmpty() == true) {
                url = requireArguments().getString(Constants.URL_WEB_VIEW).toString()
                Log.d("WebViewFragment", "onViewCreated: $url")
            }
        }

        _binding = FragmentWebViewBinding.bind(view)

        setupWebView()
    }

    private fun setupWebView() {

        binding.swipeRefresh.isRefreshing = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = true
        binding.webView.setInitialScale(1)
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url)
        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.webView.loadUrl(url)
        })

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.swipeRefresh.isRefreshing = progress != 100
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}