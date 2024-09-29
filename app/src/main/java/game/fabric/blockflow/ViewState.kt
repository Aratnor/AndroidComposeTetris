package game.fabric.blockflow

import game.fabric.blockflow.gamelogic.MoveUpState
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor
import game.fabric.blockflow.ui.MoveUpEffectUiState


data class ViewState(
    val tiles: List<List<Tile>>,
    val nextPiecePreview: Pair<List<Position>, TileColor>,
    val pieceFinalLocation: List<Position>,
    val currentTime: String,
    val moveUpState: MoveUpState,
    val score: String,
    val level: String,
    val gameOver: Boolean,
    val gameOverScore: String,
    val gameOverLevel: String,
    val moveUpEffectUiState: List<MoveUpEffectUiState> = emptyList()
    ) {
}