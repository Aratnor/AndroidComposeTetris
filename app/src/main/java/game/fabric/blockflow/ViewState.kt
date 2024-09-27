package game.fabric.blockflow

import game.fabric.blockflow.domain.models.Position
import game.fabric.blockflow.domain.models.Tile
import game.fabric.blockflow.domain.models.TileColor


data class ViewState(
    val tiles: List<List<Tile>>,
    val nextPiecePreview: Pair<List<Position>,TileColor>,
    val pieceFinalLocation: List<Position>,
    val currentTime: String,
    val moveUpState: MoveUpState,
    val score: String,
    val level: String,
    val gameOver: Boolean,
    val gameOverScore: String,
    val gameOverLevel: String
) {
}