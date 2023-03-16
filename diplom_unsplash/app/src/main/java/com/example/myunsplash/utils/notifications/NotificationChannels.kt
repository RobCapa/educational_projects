package com.example.myunsplash.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.myunsplash.UnsplashApp

object NotificationChannels {
    const val DOWNLOAD_CHANNEL_ID = "download_channel_id"

    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDownloadChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDownloadChannel() {
        val nameChannel = "Download"
        val descriptionChannel = "Photo save alerts"
        val priorityChannel = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(
            DOWNLOAD_CHANNEL_ID,
            nameChannel,
            priorityChannel
        ).apply {
            description = descriptionChannel
        }

        NotificationManagerCompat
            .from(UnsplashApp.getAppContext())
            .createNotificationChannel(channel)
    }
}