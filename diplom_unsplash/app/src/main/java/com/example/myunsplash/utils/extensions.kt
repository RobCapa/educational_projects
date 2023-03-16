package com.example.myunsplash.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.example.myunsplash.MainActivity
import com.google.android.material.snackbar.Snackbar

fun Context.getIntFromTheme(@AttrRes attrColor: Int): Int {
    val value = TypedValue()
    this.theme.resolveAttribute(attrColor, value, true)
    return value.data
}

fun View.showSnackbar(@StringRes stringId: Int) {
    Snackbar.make(
        this,
        stringId,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun View.showSnackbar(
    @StringRes message: Int,
    action: () -> Unit,
    @StringRes actionText: Int,
) {
    Snackbar
        .make(
            this,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction(actionText) {
            action()
        }
        .show()
}

fun Fragment.setOnBackPressedListener(action: () -> Unit) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        callback
    )
}

fun Group.getViews(): List<View> = referencedIds.map { rootView.findViewById(it) }

fun Activity.restart() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
