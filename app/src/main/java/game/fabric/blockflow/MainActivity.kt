package game.fabric.blockflow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.games.PlayGames
import dagger.hilt.android.AndroidEntryPoint
import game.fabric.blockflow.data.Cache
import game.fabric.blockflow.data.logging.Logger
import game.fabric.blockflow.data.logging.LoggerTag
import game.fabric.blockflow.gamelogic.WHITE_SPACE
import game.fabric.blockflow.ui.gameoverscreen.GameOverScreen
import game.fabric.blockflow.ui.gamescreen.GameScreen
import game.fabric.blockflow.ui.homescreen.HomeScreen
import game.fabric.blockflow.ui.leaderboard.LeaderBoardScreenRoute
import game.fabric.blockflow.ui.theme.TestComposeTetrisTheme
import game.fabric.blockflow.util.SoundUtil

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val logger: Logger =
        Logger(LoggerTag.MAIN_ACTIVITY + WHITE_SPACE + LoggerTag.GOOGLE_PLAY_SERVICES_GAMES)

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
        super.onStart()
        if (!viewModel.isMusicMuted) {
            SoundUtil.playGameTheme()
        }
        googlePlayGamesLogin()
    }

    override fun onPause() {
        SoundUtil.stopGameTheme()
        super.onPause()
    }

    private fun googlePlayGamesLogin() {
        val signInClient = PlayGames.getGamesSignInClient(this)

        signInClient.signIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logger.log("Sign-in successful!")
                Cache.isPlayGamesLoginSuccess = true
            } else {
                Cache.isPlayGamesLoginSuccess = false
                task.exception?.printStackTrace()
                logger.log("Sign-in failed ${task.exception?.message}")
            }
        }
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
        composable(
            NavDestination.LEADERBOARD
        ) {
            LeaderBoardScreenRoute()
        }
    }
}