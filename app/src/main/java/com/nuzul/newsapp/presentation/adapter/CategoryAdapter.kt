package com.nuzul.newsapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.nuzul.newsapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categoryItems: Array<String>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    class OnClickListener(val clickListener: (categoryName: String) -> Unit) {
        fun onClick(categoryName: String) = clickListener(categoryName)
    }

    override fun getItemCount() = categoryItems.size


    inner class ViewHolder(
        private val itemBinding: ItemCategoryBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(categoryName: String){
            itemBinding.categoryTextView.text = categoryName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val context = parent.context;
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryName = categoryItems[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(categoryItems[position])
        }
    }
}