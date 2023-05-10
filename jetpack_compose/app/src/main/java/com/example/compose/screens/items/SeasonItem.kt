package com.example.compose.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.compose.R
import com.example.compose.data.Season
import com.example.compose.refreshAsyncImagePainter
import com.example.compose.screens.MainScreenStaticStates

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SeasonItem(
    item: Season,
    onClickToItem: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
            ),
        backgroundColor = colorResource(MainScreenStaticStates.accentColorIdState),
        elevation = 5.dp,
        onClick = { onClickToItem() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Image(
                painter = refreshAsyncImagePainter(
                    item.poster,
                    ContentScale.FillBounds,
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp),
                contentScale = ContentScale.FillBounds,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 5.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = buildAnnotatedString {
                        val text = "${item.number}season"
                        append(text)
                        addStyle(
                            style = SpanStyle(
                                fontSize = 30.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            ),
                            start = 0,
                            end = 1,
                        )
                        addStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                color = Color.White,
                            ),
                            start = 1,
                            end = text.length,
                        )
                    },
                )
            }
        }
    }
}