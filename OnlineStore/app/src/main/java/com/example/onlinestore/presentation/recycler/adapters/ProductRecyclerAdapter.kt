package com.example.onlinestore.presentation.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Product
import com.example.onlinestore.databinding.ItemProductBinding
import com.example.onlinestore.presentation.recycler.diff_utils.ProductDiffUtil
import com.example.onlinestore.presentation.recycler.holders.ProductHolder

class ProductRecyclerAdapter(
    private val onClickOnItem: (Product) -> Unit,
    private val onClickOnLikeCallback: (Product) -> Unit,
) : RecyclerView.Adapter<ProductHolder>() {

    private val differ = AsyncListDiffer(this, ProductDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )

        return ProductHolder(
            binding = binding,
            onClickOnItem = onClickOnItem,
            onClickOnLikeCallback = onClickOnLikeCallback,
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    fun updateItems(items: List<Product>) {
        differ.submitList(items)
    }

    fun getItems(): List<Product> = differ.currentList.map { it.copy() }

}