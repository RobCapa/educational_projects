package com.example.compose.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.R
import com.example.compose.data.Episode
import com.example.compose.refreshAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Preview(showBackground = true)
@Composable
fun EpisodesItem(item: Episode) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White.copy(0.9f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = refreshAsyncImagePainter(
                    item.poster,
                    ContentScale.Crop,
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .padding(start = 5.dp),
            ) {
                Text(
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    text = "${item.number} episode"
                )
                Text(
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = item.description,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_play),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp),
                    )
                }
            }
        }
    }
}