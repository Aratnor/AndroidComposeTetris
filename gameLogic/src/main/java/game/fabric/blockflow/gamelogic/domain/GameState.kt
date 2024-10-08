package game.fabric.blockflow.gamelogic.domain

import game.fabric.blockflow.gamelogic.MoveUpState
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor

data class GameState(
    val tiles: List<List<Tile>>,
    val previewLocation: Pair<List<Position>,TileColor>,
    val pieceDestinationLocation: List<Position>,
    val moveUpState: MoveUpState,
    val score: String,
    val difficultyLevel: String,
    val isGameOver: Boolean
)