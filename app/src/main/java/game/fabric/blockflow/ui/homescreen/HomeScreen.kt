package game.fabric.blockflow.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import game.fabric.blockflow.NavDestination
import game.fabric.blockflow.design.PrimaryButton
import game.fabric.blockflow.ui.homeBackgroundAnimation
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.util.SoundUtil

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    SoundUtil.stopGameTheme()
    val viewModel = viewModel<HomeViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND)
            .drawBehind {
                homeBackgroundAnimation(viewModel.viewState.value)
            }
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LaunchedEffect(key1 = "x") {
            viewModel.startBackgroundAnimation()
        }
        PrimaryButton(text = "Start Game") {
            navController.navigate(NavDestination.LEADERBOARD)

        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier
                .padding(top = 48.dp)
                ,
            text = "Falling Block X",
            fontSize = TextUnit(24F, TextUnitType.Sp),
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}