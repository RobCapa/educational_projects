package com.example.compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.data.Personage
import com.example.compose.refreshAsyncImagePainter

@Preview()
@Composable
fun PersonageItem(item: Personage) {
    Card(
        backgroundColor = Color.White.copy(alpha = 0.8f),
    ) {
        Column {
            Image(
                painter = refreshAsyncImagePainter(
                    item.avatar,
                    ContentScale.FillWidth,
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = 10.dp,
                        minWidth = 10.dp,
                    ),
                contentScale = ContentScale.FillWidth,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(
                        colorResource(MainScreenStaticStates.accentColorIdState)
                            .copy(alpha = 0.9f)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.name,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        start = 10.dp,
                        end = 10.dp,
                    ),
                text = buildAnnotatedString {
                    append(item.description)
                    addStyle(
                        style = SpanStyle(
                            fontSize = 30.sp,
                            color = colorResource(MainScreenStaticStates.accentColorIdState),
                            fontWeight = FontWeight.Bold,
                        ),
                        start = 0,
                        end = 1,
                    )
                    addStyle(
                        style = SpanStyle(
                            fontSize = 18.sp
                        ),
                        start = 1,
                        end = item.description.length - 1,
                    )
                },
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 18.sp
                )
            )
        }
    }
}