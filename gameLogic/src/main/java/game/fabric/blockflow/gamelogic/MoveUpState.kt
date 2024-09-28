package game.fabric.blockflow.gamelogic

import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.piece.Piece


data class MoveUpState(
    val isMoveUpActive: Boolean,
    val moveUpInitialLocations: List<Position>,
    val moveUpMovementCount: Int,
    val currentPiece: Piece
)