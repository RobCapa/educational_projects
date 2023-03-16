package com.example.myunsplash.recycler.holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

abstract class HolderAbstract(
    protected val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    protected val context: Context
        get() = binding.root.context

    protected val requestListenerShowView = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.root.isVisible = true
            return false
        }
    }

    abstract fun bind(item: Any)

    fun hideView() {
        binding.root.visibility = View.INVISIBLE
    }
}
