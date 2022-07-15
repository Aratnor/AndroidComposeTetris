package com.example.testcomposetetris

object NavDestination {
    const val HOME = "home"
    const val GAME = "game"
    const val GAME_OVER = "gameOver/{score}&{level}"

    fun navigateGameOver(score: String,level: String): String = "gameOver/$score&$level"

}