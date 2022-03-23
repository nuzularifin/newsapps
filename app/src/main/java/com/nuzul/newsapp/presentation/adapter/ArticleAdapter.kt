package com.nuzul.newsapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nuzul.newsapp.databinding.ItemArticleBinding
import com.nuzul.newsapp.domain.entity.ArticleEntity


class ArticleAdapter(
    private val articles: MutableList<ArticleEntity>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class OnClickListener(val clickListener: (url: String) -> Unit) {
        fun onClick(url: String) = clickListener(url)
    }

    fun updateList(mProducts: List<ArticleEntity>) {
        articles.clear()
        articles.addAll(mProducts)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val itemBinding: ItemArticleBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(article: ArticleEntity) {
            if (article.title != null){
                itemBinding.titleTextView.text = article.title
            } else itemBinding.titleTextView.text = article.name
            itemBinding.descriptionTextView.text = article.description
            if (article.urlToImage != null) {
                Glide.with(context).load(article.urlToImage)
                    .into(itemBinding.articleImageView)
                itemBinding.articleImageView.visibility = View.VISIBLE
            } else itemBinding.articleImageView.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val context = parent.context;
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(article = articles[position])
        holder.itemView.setOnClickListener {
            articles[position].url?.let { it1 -> onClickListener.onClick(it1) }
        }
    }


    override fun getItemCount() = articles.size
}