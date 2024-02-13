package com.example.onlinestore.presentation.recycler.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.Product

class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}