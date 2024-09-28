package game.fabric.blockflow

import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor


data class ViewState(
    val tiles: List<List<Tile>>,
    val nextPiecePreview: Pair<List<Position>, TileColor>,
    val pieceFinalLocation: List<Position>,
    val currentTime: String,
    val moveUpState: game.fabric.blockflow.gamelogic.MoveUpState,
    val score: String,
    val level: String,
    val gameOver: Boolean,
    val gameOverScore: String,
    val gameOverLevel: String
) {
}