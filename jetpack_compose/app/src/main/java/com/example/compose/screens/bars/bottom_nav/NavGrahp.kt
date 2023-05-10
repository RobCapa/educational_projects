package com.example.compose.screens.bars.bottom_nav

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.data.Series
import com.example.compose.screens.seasons.SeasonsScreen
import com.example.compose.screens.personages.PersonagesScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomItem.page_home,
    ) {
        composable(BottomItem.page_home) {
            SeasonsScreen(
                scaffoldState,
            )
        }
        composable(BottomItem.page_profile) {
            PersonagesScreen()
        }
    }
}