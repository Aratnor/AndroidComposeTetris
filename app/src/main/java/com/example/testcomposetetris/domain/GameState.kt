package com.example.testcomposetetris.domain

import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.Tile
import com.example.testcomposetetris.domain.models.TileColor

data class GameState(
    val tiles: List<List<Tile>>,
    val previewLocation: Pair<List<Position>,TileColor>,
    val pieceDestinationLocation: List<Position>,
    val score: String,
    val difficultyLevel: String,
    val isGameOver: Boolean
)