package game.fabric.blockflow

import game.fabric.blockflow.domain.models.Position
import game.fabric.blockflow.domain.models.piece.Piece

data class MoveUpState(
    val isMoveUpActive: Boolean,
    val moveUpInitialLocations: List<Position>,
    val moveUpMovementCount: Int,
    val currentPiece: Piece
)