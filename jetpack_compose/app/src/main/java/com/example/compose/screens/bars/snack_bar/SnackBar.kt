package com.example.compose.screens.bars.snack_bar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.compose.R
import com.example.compose.screens.MainScreenStaticStates

@Composable
fun SnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        backgroundColor = (colorResource(MainScreenStaticStates.accentColorIdState)),
        contentColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        actionColor = Color.White,
    )
}