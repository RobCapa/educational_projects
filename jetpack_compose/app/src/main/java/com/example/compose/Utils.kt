package com.example.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.lang.Float.min

fun Color.changeIntensity(factor: Float): Color {
    val red = min(max(red * factor, 0f), 1f)
    val green = min(max(green * factor, 0f), 1f)
    val blue = min(max(blue * factor, 0f), 1f)
    return copy(
        red = red,
        green = green,
        blue = blue,
    )
}

@Composable
fun refreshAsyncImagePainter(
    data: Any?,
    contentScale: ContentScale,
    timeRefresh: Long = 1000,
): AsyncImagePainter {
    val scope = rememberCoroutineScope()
    var retryKey by remember { mutableStateOf(true) }
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .setParameter("retry_hash", retryKey, memoryCacheKey = null)
            .build(),
        contentScale = contentScale,
        onError = {
            scope.launch {
                delay(timeRefresh)
                retryKey = !retryKey
            }
        },
    )
}