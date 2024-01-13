package ru.aston.interfaces.ui_managing

import androidx.fragment.app.Fragment
import ru.aston.R

interface ToolbarManaging {
    fun addActionsToToolbar(
        callingFragment: Fragment,
        toolbarActions: Map<ToolbarItem, () -> Unit?>,
        showSearchItem: Boolean,
        navigationOnClickListener: (() -> Unit)?,
    )

    fun setTitleToolbar(
        callingFragment: Fragment,
        imageUrl: String?,
        title: String,
    )

    fun replaceToolbarItem(
        callingFragment: Fragment,
        oldToolbarItem: ToolbarItem,
        newToolbarItem: ToolbarItem,
        onClickListener: () -> Unit?
    )

    enum class ToolbarItem(val id: Int) {
        FILTERS_ITEM(R.id.menuItem_filter),
        CHECK_MARK_ITEM(R.id.menuItem_checkMark),
        NOT_SAVED_ITEM(R.id.menuItem_not_saved),
        SAVED_ITEM(R.id.menuItem_saved),
    }
}