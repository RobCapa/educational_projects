package com.example.myunsplash

import android.app.Application
import android.content.Context
import com.example.myunsplash.utils.notifications.NotificationChannels
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UnsplashApp : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        UnsplashPhotoPicker.init(
            this,
            AuthConfig.client_id,
            AuthConfig.secretKey
        )

        NotificationChannels.createChannels()
    }

    companion object {
        private var instance: UnsplashApp? = null

        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
    }
}