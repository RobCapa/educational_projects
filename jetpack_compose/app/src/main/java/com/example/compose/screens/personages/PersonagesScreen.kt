package com.example.compose.screens.personages

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.compose.data.Series
import com.example.compose.repositories.SeriesRepository
import com.example.compose.screens.MainScreenStaticStates
import com.example.compose.screens.PersonageItem

@Composable
fun PersonagesScreen() {
    val personages = SeriesRepository
        .getPersonagesFlow(MainScreenStaticStates.seriesState?.id ?: -1)
        .collectAsLazyPagingItems()

    LazyColumn() {
        items(personages) { item ->
            item ?: return@items
            val padding = 20.dp
            Card(
                modifier = Modifier
                    .padding(
                        top = padding / 2,
                        bottom = padding / 2,
                        start = padding,
                        end = padding,
                    ),
            ) { PersonageItem(item) }
        }
    }
}