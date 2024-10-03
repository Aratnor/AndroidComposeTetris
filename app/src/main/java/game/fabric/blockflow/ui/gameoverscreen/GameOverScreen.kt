package game.fabric.blockflow.ui.gameoverscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import game.fabric.blockflow.NavDestination
import game.fabric.blockflow.R
import game.fabric.blockflow.design.PrimaryButton
import game.fabric.blockflow.ui.homeBackgroundAnimation
import game.fabric.blockflow.ui.homescreen.HomeViewModel
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.ui.theme.Purple500
import game.fabric.blockflow.ui.theme.SOFT_PINK
import game.fabric.blockflow.ui.theme.TILE_BLUE_END
import game.fabric.blockflow.ui.theme.TILE_PURPLE_START
import game.fabric.blockflow.util.SoundUtil

@Composable
fun GameOverScreen(
    navController: NavHostController,
    score: String,
    level: String
) {
    SoundUtil.stopGameTheme()
    val viewModel = viewModel<HomeViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND)
            .drawBehind {
                homeBackgroundAnimation(viewModel.viewState.value)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            modifier = Modifier.padding(top = 8.dp), text = score,
            style = TextStyle(
                brush = Brush.linearGradient(colors = listOf(Purple500,SOFT_PINK)),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(0.10f))
        Text(modifier = Modifier.padding(top = 8.dp), text = level,
            style = TextStyle(
                brush = Brush.linearGradient(colors = listOf(Purple500,SOFT_PINK)),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.size(64.dp))
        PrimaryButton(
            text = stringResource(id = R.string.game_over_btn_text),
            style = MaterialTheme.typography.h6,
            fillMaxWidth = true,
            textPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
            buttonPadding = PaddingValues(horizontal = 24.dp)
        ) {
            SoundUtil.playGameTheme()
            navController.navigate(NavDestination.GAME) {
                popUpTo(NavDestination.HOME)
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        PrimaryButton(
            text = stringResource(id = R.string.game_over_btn_leaderboard),
            style = MaterialTheme.typography.h6,
            fillMaxWidth = true,
            textPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
            buttonPadding = PaddingValues(horizontal = 24.dp)
        ) {
            SoundUtil.playGameTheme()
            navController.navigate(NavDestination.GAME) {
                popUpTo(NavDestination.HOME)
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier
                .padding(top = 48.dp),
            text = stringResource(id = R.string.projectName),
            style = TextStyle(
                brush = Brush.linearGradient(colors = listOf(TILE_PURPLE_START,SOFT_PINK)),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            ),
            color = Color.White
        )
    }
    LaunchedEffect(Unit) {
        viewModel.startBackgroundAnimation()
    }
}

@Preview
@Composable
fun PreviewGameOverScreen() {
    GameOverScreen(navController = rememberNavController(), score = "Score: 50", level = "Level: 2")
}