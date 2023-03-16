package com.example.myunsplash.utils.notifications

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.example.myunsplash.R
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.repositories.managers.DownloadPhotoManager
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IssuerDownloadPhotoNotification @Inject constructor(
    private val photosRep: PhotosRepository,
) {
    private val observers: MutableMap<LiveData<WorkInfo>, Observer<WorkInfo>> = mutableMapOf()

    fun observe(
        liveData: LiveData<WorkInfo>,
        lifecycle: Lifecycle,
        view: View,
        context: Context,
    ) {
        val observer = Observer<WorkInfo> { workInfo ->
            workInfo?.let {
                processStateOfPhotoDownload(workInfo, liveData, view, context, lifecycle)
            }
        }
        observers[liveData] = observer
        liveData.observeForever(observer)
    }

    private fun processStateOfPhotoDownload(
        workInfo: WorkInfo,
        liveData: LiveData<WorkInfo>,
        view: View,
        context: Context,
        lifecycleOwner: Lifecycle,
    ) {
        when (workInfo.state) {
            WorkInfo.State.BLOCKED -> {
                if (appIsResumed(lifecycleOwner)) view.showSnackbar(R.string.notification_downloadWillContinueLater)
            }
            WorkInfo.State.SUCCEEDED -> {
                if (appIsResumed(lifecycleOwner)) showSuccessfulSnackbar(workInfo, view, context)
                else showSuccessfulNotification(workInfo, context)
                removeObserver(liveData)
            }
            WorkInfo.State.FAILED -> {
                if (appIsResumed(lifecycleOwner)) view.showSnackbar(R.string.notification_photoDownloadFailed)
                else showFailedNotification(workInfo, context)
                removeObserver(liveData)
            }
        }
    }

    private fun appIsResumed(lifecycleOwner: Lifecycle): Boolean {
        return lifecycleOwner.currentState == Lifecycle.State.RESUMED
    }

    private fun showSuccessfulSnackbar(
        workInfo: WorkInfo,
        view: View,
        context: Context,
    ) {
        val uri = getUriFromWorkInfo(workInfo)
        view.showSnackbar(
            R.string.notification_photoWasDownloaded,
            { photosRep.openPhotoIn(uri, context) },
            R.string.notification_openPhotoIn,
        )
    }

    private fun getUriFromWorkInfo(
        workInfo: WorkInfo,
    ): Uri {
        return workInfo
            .outputData
            .getString(DownloadPhotoManager.KEY_URI)
            .let { Uri.parse(it) }
    }

    private fun removeObserver(liveData: LiveData<WorkInfo>) {
        observers[liveData]?.let(liveData::removeObserver)
        observers.remove(liveData)
    }

    private fun showSuccessfulNotification(
        workInfo: WorkInfo,
        context: Context,
        image: Bitmap? = null,
    ) {
        val uri = getUriFromWorkInfo(workInfo)

        if (image == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val photo = Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .submit()
                    .get()

                withContext(Dispatchers.Main) {
                    showSuccessfulNotification(workInfo, context, photo)
                }
            }
        }

        val sendPhotoPendingIntent = PendingIntent.getActivity(
            context,
            123,
            photosRep.getSendPhotoIntent(uri, context),
            0
        )

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannels.DOWNLOAD_CHANNEL_ID
        ).apply {
            setContentTitle(context.getString(R.string.notification_photoWasDownloaded))
            image?.let { setLargeIcon(image) }
            setSmallIcon(R.drawable.ic_baseline_image)
            setContentIntent(sendPhotoPendingIntent)
            setAutoCancel(true)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        }.build()

        NotificationManagerCompat.from(context)
            .notify(uri.lastPathSegment?.toInt() ?: 1, notification)
    }

    private fun showFailedNotification(
        workInfo: WorkInfo,
        context: Context,
    ) {
        val uri = getUriFromWorkInfo(workInfo)

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannels.DOWNLOAD_CHANNEL_ID
        ).apply {
            setContentTitle(context.getString(R.string.notification_photoDownloadFailed))
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_baseline_image_not_found)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        }.build()

        NotificationManagerCompat.from(context)
            .notify(uri.lastPathSegment?.toInt() ?: 2, notification)
    }
}