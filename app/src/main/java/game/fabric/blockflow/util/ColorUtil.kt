package game.fabric.blockflow.util

import game.fabric.blockflow.domain.models.TileColor

object ColorUtil {
    fun generateRandomColor(): TileColor = TileColor.values()
        .filter { it != TileColor.EMPTY }.random()
}