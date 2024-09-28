package game.fabric.blockflow.gamelogic.domain.models

import androidx.compose.ui.graphics.Color
import game.fabric.blockflow.gamelogic.TILE_BLUE_END
import game.fabric.blockflow.gamelogic.TILE_BLUE_START
import game.fabric.blockflow.gamelogic.TILE_BLUE_STROKE
import game.fabric.blockflow.gamelogic.TILE_GREEN_END
import game.fabric.blockflow.gamelogic.TILE_GREEN_START
import game.fabric.blockflow.gamelogic.TILE_GREEN_STROKE
import game.fabric.blockflow.gamelogic.TILE_ORANGE_END
import game.fabric.blockflow.gamelogic.TILE_ORANGE_START
import game.fabric.blockflow.gamelogic.TILE_ORANGE_STROKE
import game.fabric.blockflow.gamelogic.TILE_PURPLE_END
import game.fabric.blockflow.gamelogic.TILE_PURPLE_START
import game.fabric.blockflow.gamelogic.TILE_PURPLE_STROKE
import game.fabric.blockflow.gamelogic.TILE_RED_END
import game.fabric.blockflow.gamelogic.TILE_RED_START
import game.fabric.blockflow.gamelogic.TILE_RED_STROKE_COLOR
import game.fabric.blockflow.gamelogic.TILE_YELLOW_END
import game.fabric.blockflow.gamelogic.TILE_YELLOW_START
import game.fabric.blockflow.gamelogic.TILE_YELLOW_STROKE

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