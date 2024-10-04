package game.fabric.blockflow

object NavDestination {
    const val HOME = "home"
    const val GAME = "game"
    const val GAME_OVER = "gameOver/{score}&{level}"
    const val LEADERBOARD = "leaderBoard"

    fun navigateGameOver(score: String,level: String): String = "gameOver/$score&$level"

}