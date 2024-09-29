package game.fabric.blockflow.gamelogic.domain

import game.fabric.blockflow.gamelogic.GameConfig

fun generateRandomNumber(
    min: Int = 0,
    max: Int = GameConfig.COLUMN_SIZE - 1
): Int = (min..max).random()
