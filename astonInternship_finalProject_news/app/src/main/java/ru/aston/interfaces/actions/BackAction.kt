package ru.aston.interfaces.actions

interface BackAction {
    fun onBackPressed(): Boolean
    fun popBackStackToFirst()
}