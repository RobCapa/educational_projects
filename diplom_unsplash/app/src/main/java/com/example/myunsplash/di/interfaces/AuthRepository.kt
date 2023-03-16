package com.example.myunsplash.di.interfaces

import android.content.Intent

interface AuthRepository {
    fun getAuthIntent(): Intent

    fun getLogoutIntent(): Intent

    fun processResultAuthRequest(
        intent: Intent,
        callback: (Boolean) -> Unit,
    )

    fun checkIfAccessTokenExists(): Boolean

    fun getAccessToken(): String?

    fun removeToken()
}