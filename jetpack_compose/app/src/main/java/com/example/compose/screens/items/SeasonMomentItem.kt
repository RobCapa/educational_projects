package com.example.compose.screens.items

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.R
import com.example.compose.data.SeasonMoment
import com.example.compose.refreshAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun SeasonMomentItem(
    item: SeasonMoment,
    scaffoldState: ScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = 5.dp,
    ) {
        Image(
            painter = refreshAsyncImagePainter(
                item.poster,
                ContentScale.FillWidth,
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 100.dp,
                    minWidth = 10.dp,
                ),
            contentScale = ContentScale.FillWidth,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd,
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Photo saved",
                        actionLabel = "Open",
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        Toast.makeText(
                            context,
                            "Imitation of photo opening",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_save),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}