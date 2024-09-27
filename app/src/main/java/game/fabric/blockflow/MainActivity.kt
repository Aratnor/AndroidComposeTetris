package game.fabric.blockflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import game.fabric.blockflow.ui.GameOverScreen
import game.fabric.blockflow.ui.GameScreen
import game.fabric.blockflow.ui.HomeScreen
import game.fabric.blockflow.ui.theme.TestComposeTetrisTheme
import game.fabric.blockflow.util.SoundUtil

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestComposeTetrisTheme {
                // A surface container using the 'background' color from the theme
                SetNavHost(navController = rememberNavController())
            }
        }
    }

    override fun onStart() {
        if(!viewModel.isMusicMuted) {
            SoundUtil.playGameTheme()
        }
        super.onStart()
    }

    override fun onPause() {
        SoundUtil.stopGameTheme()
        super.onPause()
    }
}


@Composable
fun SetNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.HOME
    ) {
        composable(NavDestination.HOME) {
            HomeScreen(navController)
        }
        composable(NavDestination.GAME) {
            GameScreen(navController)
        }
        composable(
            NavDestination.GAME_OVER
        ) {
            GameOverScreen(
                navController,
                it.arguments?.getString("score").orEmpty(),
                it.arguments?.getString("level").orEmpty()
            )
        }
    }
}