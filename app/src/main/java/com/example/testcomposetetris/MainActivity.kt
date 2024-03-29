package com.example.testcomposetetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testcomposetetris.ui.GameOverScreen
import com.example.testcomposetetris.ui.GameScreen
import com.example.testcomposetetris.ui.HomeScreen
import com.example.testcomposetetris.ui.theme.TestComposeTetrisTheme
import com.example.testcomposetetris.util.SoundUtil

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