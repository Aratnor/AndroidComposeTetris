package com.example.testcomposetetris.domain.models

data class Tile(
    val isOccupied: Boolean,
    val hasActivePiece: Boolean,
    val color: TileColor
)