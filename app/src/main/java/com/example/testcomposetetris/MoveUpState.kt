package com.example.testcomposetetris

import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.piece.Piece

data class MoveUpState(
    val isMoveUpActive: Boolean,
    val moveUpInitialLocations: List<Position>,
    val moveUpMovementCount: Int,
    val currentPiece: Piece
)