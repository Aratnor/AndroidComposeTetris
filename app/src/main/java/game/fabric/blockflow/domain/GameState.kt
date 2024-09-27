package game.fabric.blockflow.domain

import game.fabric.blockflow.MoveUpState
import game.fabric.blockflow.domain.models.Position
import game.fabric.blockflow.domain.models.Tile
import game.fabric.blockflow.domain.models.TileColor

data class GameState(
    val tiles: List<List<Tile>>,
    val previewLocation: Pair<List<Position>,TileColor>,
    val pieceDestinationLocation: List<Position>,
    val moveUpState: MoveUpState,
    val score: String,
    val difficultyLevel: String,
    val isGameOver: Boolean
)