package com.example.compose.screens.bars.drawer_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.compose.R
import com.example.compose.data.Series
import com.example.compose.repositories.SeriesRepository
import com.example.compose.screens.MainScreenStaticStates
import com.example.compose.screens.items.SerialItem
import androidx.paging.compose.items

@Preview
@Composable
fun DrawerHeader() {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(colorResource(MainScreenStaticStates.accentColorIdState)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "SERIES",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                )
            )
        }
    }
}

@Preview
@Composable
fun DrawerBody(
    scaffoldState: ScaffoldState,
) {
    val series = SeriesRepository
        .getSeriesFlow()
        .collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        items(series) {item ->
            item?.let {
                SerialItem(
                    item,
                    scaffoldState,
                )
            }
        }
    }
}