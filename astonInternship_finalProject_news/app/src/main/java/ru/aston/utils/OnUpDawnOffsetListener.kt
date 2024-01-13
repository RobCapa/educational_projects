package ru.aston.utils

import com.google.android.material.appbar.AppBarLayout

class OnUpDawnOffsetListener(
    var onTopScrollPosition: () -> Unit,
    var onDownScrollPosition: () -> Unit,
) : AppBarLayout.OnOffsetChangedListener {

    private var isShow = true
    private var scrollRange = -1

    fun reset() {
        isShow = true
        scrollRange = -1
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout?.totalScrollRange ?: 0
        }
        if (scrollRange + verticalOffset == 0) {
            onTopScrollPosition()
            isShow = true
        } else if (isShow) {
            onDownScrollPosition()
            isShow = false
        }
    }
}