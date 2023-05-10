package com.example.compose.screens.bars.top_bar

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.compose.screens.MainScreenStaticStates
import kotlinx.coroutines.launch
import com.example.compose.R

@Composable
fun TopBar(
    scaffoldState: ScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = MainScreenStaticStates.seriesState?.title ?: "",
                color = Color.White
            )
        },
        backgroundColor = colorResource(MainScreenStaticStates.accentColorIdState),
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        },
        actions = {
            var searchTextIsVisible by remember { mutableStateOf(false) }
            IconButton(onClick = {
                if (!searchTextIsVisible) {
                    searchTextIsVisible = true
                    return@IconButton
                }
                /* Make a search */
                searchTextIsVisible = false
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                )
            }
            val searchText = remember { mutableStateOf("") }
            if (searchTextIsVisible) {
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    label = { Text("Search") },
                    trailingIcon = {
                        IconButton(onClick = { searchTextIsVisible = false }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_close),
                                contentDescription = "Search",
                                tint = Color.White,
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        textColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedIndicatorColor = Color.White,
                    ),
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                )
            }
        }
    )
}