package ru.aston.interfaces.ui_managing

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

interface SearchManaging {
    fun configureSearch(
        callingFragment: Fragment,
        recyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        layoutManager: RecyclerView.LayoutManager,
        itemDecorations: List<RecyclerView.ItemDecoration>,
        onSearchRequest: (String) -> Unit,
        onHideSearchView: () -> Unit,
    )

    fun hideSearch()
}