package com.nuzul.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nuzul.newsapp.R

class NewsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }
}