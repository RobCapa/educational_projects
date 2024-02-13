package com.example.onlinestore.presentation.utils

import androidx.recyclerview.widget.RecyclerView

class AdapterDataObserverAnyChanges(
    val action: () -> Unit,
) : RecyclerView.AdapterDataObserver() {

    var needToMakeAction = false

    override fun onItemRangeInserted(
        positionStart: Int,
        itemCount: Int
    ) {
        checkIfNeedMakeAction()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        checkIfNeedMakeAction()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        checkIfNeedMakeAction()
    }

    private fun checkIfNeedMakeAction() {
        if (needToMakeAction) {
            action()
            needToMakeAction = false
        }
    }
}