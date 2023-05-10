package com.example.compose.screens.seasons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.compose.R
import com.example.compose.data.Season
import com.example.compose.refreshAsyncImagePainter
import com.example.compose.repositories.SeriesRepository
import com.example.compose.screens.EpisodesItem
import com.example.compose.screens.MainScreenStaticStates
import com.example.compose.screens.items.SeasonItem
import com.example.compose.screens.items.SeasonMomentItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun SeasonsScreen(
    scaffoldState: ScaffoldState,
) {
    val currentSeasonNumberState = remember {
        mutableStateOf(1)
    }
    val seasonSelectionDialogState = remember {
        mutableStateOf(false)
    }
    val seasonState = remember {
        derivedStateOf {
            SeriesRepository.getSeason(
                seriesId = MainScreenStaticStates.seriesState?.id ?: -1,
                seasonNumber = currentSeasonNumberState.value,
            )
        }
    }

    if (seasonSelectionDialogState.value) {
        SeasonSelectionDialog(
            seasonSelectionDialogState,
            currentSeasonNumberState,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        MainCard(
            seasonState,
            seasonSelectionDialogState,
        )
        Tabs(
            seasonState,
            scaffoldState,
        )
    }
}

@Composable
private fun MainCard(
    seasonState: State<Season?>,
    seasonSelectionDialogState: MutableState<Boolean>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp,
    ) {
        Image(
            painter = refreshAsyncImagePainter(
                seasonState.value?.poster,
                ContentScale.Crop,
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.9f),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    end = 15.dp,
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${seasonState.value?.number}",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 60.sp
                    )
                )
                Text(
                    text = "season",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            IconButton(
                onClick = { seasonSelectionDialogState.value = true },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_more_seasons),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Tabs(
    seasonState: State<Season?>,
    scaffoldState: ScaffoldState,
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabTitle = listOf("Episodes", "Season's Gallery")

    Column(
        modifier = Modifier
            .padding(
                start = 5.dp,
                end = 5.dp
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { pos ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, pos)
                )
            },
            backgroundColor = colorResource(MainScreenStaticStates.accentColorIdState),
            contentColor = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .padding(top = 5.dp, bottom = 5.dp),
        ) {
            tabTitle.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title) },
                )
            }
        }

        val episodes = SeriesRepository.getEpisodesFlow(
            MainScreenStaticStates.seriesState?.id ?: -1,
            seasonState.value?.number ?: -1,
        ).collectAsLazyPagingItems()

        val seasonMoments = SeriesRepository.getSeasonMomentsFlow(
            MainScreenStaticStates.seriesState?.id ?: -1,
            seasonState.value?.number ?: -1,
        ).collectAsLazyPagingItems()

        HorizontalPager(
            count = tabTitle.size,
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                when (page) {
                    0 -> {
                        items(episodes) { item ->
                            item?.let { EpisodesItem(item = item) }
                        }
                    }

                    1 -> {
                        items(seasonMoments) { item ->
                            item?.let {
                                SeasonMomentItem(
                                    item = item,
                                    scaffoldState = scaffoldState,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonSelectionDialog(
    dialogState: MutableState<Boolean>,
    seasonState: MutableState<Int>,
) {
    val seasons = SeriesRepository
        .getSeasonsFlow(MainScreenStaticStates.seriesState?.id ?: -1)
        .collectAsLazyPagingItems()

    Dialog(onDismissRequest = { dialogState.value = false },
        content = {
            Card(
                backgroundColor = Color.White.copy(0.9f),
                shape = RoundedCornerShape(10.dp),
                elevation = 5.dp,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    IconButton(
                        onClick = { dialogState.value = false },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_close),
                            contentDescription = null,
                            tint = Color.Black,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .padding(10.dp),
                ) {
                    Text(
                        text = "Seasons:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp,
                            ),
                    ) {
                        items(seasons) { item ->
                            item?.let {
                                SeasonItem(
                                    item = item,
                                    onClickToItem = {
                                        seasonState.value = item.number
                                        dialogState.value = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}