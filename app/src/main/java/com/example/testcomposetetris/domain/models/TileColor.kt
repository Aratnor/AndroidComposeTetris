package com.example.testcomposetetris.domain.models

import androidx.compose.ui.graphics.Color
import com.example.testcomposetetris.ui.theme.*

enum class TileColor(
    val startColor: Color,
    val endColor: Color,
    val strokeColor: Color
) {
    EMPTY(Color.White,Color.White,Color.White),
    ORANGE(TILE_ORANGE_START,TILE_ORANGE_END,TILE_ORANGE_STROKE),
    GREEN(TILE_GREEN_START,TILE_GREEN_END,TILE_GREEN_STROKE),
    BLUE(TILE_BLUE_START,TILE_BLUE_END,TILE_BLUE_STROKE),
    YELLOW(TILE_YELLOW_START, TILE_YELLOW_END, TILE_YELLOW_STROKE),
    PURPLE(TILE_PURPLE_START, TILE_PURPLE_END, TILE_PURPLE_STROKE),
    RED(TILE_RED_START,TILE_RED_END,TILE_RED_STROKE_COLOR)
}