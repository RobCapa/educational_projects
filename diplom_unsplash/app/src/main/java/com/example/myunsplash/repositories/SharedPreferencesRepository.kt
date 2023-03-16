package com.example.myunsplash.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.myunsplash.UnsplashApp

object SharedPreferencesRepository {
    val sharedPreferences: SharedPreferences
        get() = UnsplashApp.getAppContext()
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private const val SHARED_PREFERENCES_NAME = "shared_preferences"

    object Keys {
        const val IS_FIRST_RUN_KEY = "is_first_run"
        const val AUTH_STATE_KEY = "auth_state"
    }
}