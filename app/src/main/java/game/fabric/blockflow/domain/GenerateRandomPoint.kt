package game.fabric.blockflow.domain

fun generateRandomNumber(
    min: Int = 0,
    max: Int = 11
): Int = (min..max).random()
