package com.prateek.booksapp.ui.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prateek.booksapp.R
import kotlinx.android.synthetic.main.line_item_category.view.*

class CategoryAdapter(private val categoryClickListener: CategoryClickListener) :
    ListAdapter<String, CategoryAdapter.CategoryViewHolder>(DiffCallback) {

    interface CategoryClickListener {
        fun onCategoryClicked(categoryName: String)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(categoryName: String, clickListener: CategoryClickListener) {
            itemView.categoryName.text = categoryName
            itemView.setOnClickListener {
                categoryClickListener.onCategoryClicked(categoryName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.line_item_category,
            parent,
            false
        )

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), categoryClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}