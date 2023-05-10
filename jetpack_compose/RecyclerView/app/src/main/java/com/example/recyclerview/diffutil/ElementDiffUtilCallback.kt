package com.example.recyclerview.diffutil

import androidx.recyclerview.widget.DiffUtil

class ElementDiffUtilCallback(
    private val oldElements: List<Worker>,
    private val newElements: List<Worker>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldElements.size

    override fun getNewListSize(): Int = newElements.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldElements[oldItemPosition].name == newElements[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldElements[oldItemPosition].position == newElements[newItemPosition].position
}