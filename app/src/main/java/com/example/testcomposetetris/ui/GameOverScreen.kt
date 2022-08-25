package com.example.testcomposetetris.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.NavDestination
import com.example.testcomposetetris.R
import com.example.testcomposetetris.util.SoundUtil

@Composable
fun GameOverScreen(
    navController: NavHostController,
    score: String,
    level: String
) {
    SoundUtil.stopGameTheme()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("GAME OVER")
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = score)
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = level)
        Button(onClick = {
            SoundUtil.playGameTheme()
            navController.navigate(NavDestination.GAME) {
                popUpTo(NavDestination.HOME)
            }
        }) {
            Text(stringResource(id = R.string.game_over_btn_text))
        }
    }
}