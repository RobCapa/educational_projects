package com.example.compose.screens.bars.bottom_nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.compose.screens.MainScreenStaticStates

@Composable
fun BottomNav(
    navController: NavController,
) {
    val buttons = listOf(
        BottomItem.PageHome,
        BottomItem.PageProfile,
    )

    BottomNavigation(
        backgroundColor = Color.White,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        buttons.forEach { button ->
            BottomNavigationItem(
                selected = button.route == currentDestination?.route,
                onClick = {
                    navController.navigate(button.route)  {
                        if (currentDestination != null) {
                            popUpTo(currentDestination.id) {
                                saveState = true
                                inclusive = true
                            }
                        } else {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                                inclusive = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = button.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = button.title)
                },
                selectedContentColor = colorResource(MainScreenStaticStates.accentColorIdState),
                unselectedContentColor = Color.Gray,
            )
        }
    }
}