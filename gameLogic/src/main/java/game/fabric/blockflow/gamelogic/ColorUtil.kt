package game.fabric.blockflow.gamelogic

import game.fabric.blockflow.gamelogic.domain.models.TileColor


object ColorUtil {
    fun generateRandomColor(): TileColor = TileColor.entries
        .filter { it != TileColor.EMPTY }.random()
}