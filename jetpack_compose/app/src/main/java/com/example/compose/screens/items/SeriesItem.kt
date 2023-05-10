package com.example.compose.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.compose.data.Series
import com.example.compose.refreshAsyncImagePainter
import com.example.compose.screens.MainScreenStaticStates
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class, ExperimentalMaterialApi::class)
@Composable
fun SerialItem(
    item: Series,
    scaffoldState: ScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            MainScreenStaticStates.seriesState = item
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    ) {
        Image(
            painter = refreshAsyncImagePainter(
                item.poster,
                ContentScale.FillWidth,
            ),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    end = 10.dp,
                ),
            contentAlignment = Alignment.CenterEnd,
        ) {
            val textSize = 25.sp
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    drawStyle = Stroke(
                        miter = 10f,
                        width = 5f,
                        join = StrokeJoin.Round,
                    )
                ),
            )
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = Color.White,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}