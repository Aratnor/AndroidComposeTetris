package com.example.testcomposetetris.util

import com.example.testcomposetetris.domain.models.TileColor

object ColorUtil {
    fun generateRandomColor(): TileColor = TileColor.values()
        .filter { it != TileColor.EMPTY }.random()
}