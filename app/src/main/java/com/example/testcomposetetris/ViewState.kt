package com.example.testcomposetetris

import com.example.testcomposetetris.domain.models.Position


data class ViewState(
    val tiles: List<List<Boolean>>,
    val nextPiecePreview: List<Position>,
    val pieceFinalLocation: List<Position>,
    val currentTime: String,
    val score: String,
    val level: String,
    val gameOver: Boolean,
    val gameOverScore: String,
    val gameOverLevel: String
) {
}