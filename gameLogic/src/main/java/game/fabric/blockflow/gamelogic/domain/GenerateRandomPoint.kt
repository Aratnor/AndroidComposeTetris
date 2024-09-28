package game.fabric.blockflow.gamelogic.domain

fun generateRandomNumber(
    min: Int = 0,
    max: Int = 11
): Int = (min..max).random()
