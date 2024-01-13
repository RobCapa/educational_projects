package ru.aston.interfaces.ui_managing

import androidx.fragment.app.Fragment

interface StatusBarManaging {
    fun hideStatusBar(callingFragment: Fragment)
    fun showStatusBar(callingFragment: Fragment)
}