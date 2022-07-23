package com.example.testcomposetetris

import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.Tile
import com.example.testcomposetetris.domain.models.TileColor


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