package game.fabric.blockflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import game.fabric.blockflow.HomeViewModel
import game.fabric.blockflow.NavDestination
import game.fabric.blockflow.ext.coloredShadow
import game.fabric.blockflow.ui.theme.BACKGROUND
import game.fabric.blockflow.ui.theme.BUTTON_BACKGROUND
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
        OutlinedButton(
            modifier = Modifier
                .coloredShadow(Color.Black)
            ,
            shape = RoundedCornerShape(12),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = BUTTON_BACKGROUND,
                contentColor = Color.White
            ),
            onClick = {
                navController.navigate(NavDestination.GAME)
            }
        ) {
            Text(
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
                text = "Start Game",
                style = MaterialTheme.typography.body2
            )
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