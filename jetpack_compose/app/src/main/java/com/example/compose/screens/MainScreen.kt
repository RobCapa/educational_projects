package com.example.compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import com.example.compose.R
import com.example.compose.changeIntensity
import com.example.compose.refreshAsyncImagePainter
import com.example.compose.repositories.SeriesRepository
import com.example.compose.screens.MainScreenStaticStates.accentColorIdState
import com.example.compose.screens.MainScreenStaticStates.seriesState
import com.example.compose.screens.bars.bottom_nav.BottomNav
import com.example.compose.screens.bars.bottom_nav.NavGraph
import com.example.compose.screens.bars.drawer_nav.DrawerBody
import com.example.compose.screens.bars.drawer_nav.DrawerHeader
import com.example.compose.screens.bars.snack_bar.SnackBar
import com.example.compose.screens.bars.top_bar.TopBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen() {
    val navControllerState = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = colorResource(accentColorIdState).changeIntensity(0.7f)
    )

    Scaffold(
        bottomBar = {
            BottomNav(navController = navControllerState)
        },
        topBar = {
            TopBar(
                scaffoldState,
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { host ->
            SnackbarHost(
                hostState = host,
            ) { data ->
                SnackBar(data = data)
            }
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                scaffoldState,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding(),
                )
        ) {
            Image(
                painter = refreshAsyncImagePainter(
                    seriesState?.wallpaper,
                    ContentScale.FillBounds,
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alpha = 0.9f,
            )
            NavGraph(
                navHostController = navControllerState,
                scaffoldState = scaffoldState,
            )
        }
    }
}

object MainScreenStaticStates {
    var seriesState by
    mutableStateOf(SeriesRepository.getSeries(1))

    val accentColorIdState by derivedStateOf {
        when (seriesState?.id) {
            1 -> R.color.green
            2 -> R.color.red
            else -> R.color.white
        }
    }
}