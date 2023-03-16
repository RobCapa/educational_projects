package com.example.myunsplash.repositories.managers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.Instant
import kotlin.random.Random

class DownloadPhotoManager(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        var imageUri: Uri? = null
        inputData.getString(KEY_URL)?.let { url ->
            try {
                val photoBitmap = Glide.with(applicationContext)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()

                val imageName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Instant.now().toString()
                } else {
                    Random.nextInt().toString()
                } + ".jpeg"
                imageUri = createUriForSaveImage(imageName)
                saveImage(imageUri!!, photoBitmap)
                makeImageVisible(imageUri!!)
            } catch (e: Exception) {
                imageUri?.let { deleteImage(it) }
                return Result.failure()
            }
            return Result.success(
                workDataOf(
                    KEY_URI to imageUri.toString(),
                )
            )
        }
        return Result.failure()
    }

    private fun createUriForSaveImage(imageName: String): Uri {
        val external: String =
            if (versionIsQ()) MediaStore.VOLUME_EXTERNAL_PRIMARY
            else MediaStore.VOLUME_EXTERNAL

        val contentUri = MediaStore.Images.Media.getContentUri(external)
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, imageName)
            put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg")
            if (versionIsQ()) put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        return applicationContext.contentResolver.insert(contentUri, imageDetails)!!
    }

    private fun deleteImage(uri: Uri) {
        applicationContext.contentResolver.delete(uri, null, null)
    }

    private fun versionIsQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private fun saveImage(uri: Uri, bitmap: Bitmap) {
        applicationContext.contentResolver.openOutputStream(uri)?.use { outputStream ->
            getInputStreamForBitmap(bitmap).use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    private fun getInputStreamForBitmap(bitmap: Bitmap): InputStream {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return ByteArrayInputStream(baos.toByteArray())
    }

    private fun makeImageVisible(image: Uri) {
        if (!versionIsQ()) return
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }
        applicationContext.contentResolver.update(image, imageDetails, null, null)
    }

    companion object {
        const val KEY_URL = "url"
        const val KEY_URI = "uri"
    }
}