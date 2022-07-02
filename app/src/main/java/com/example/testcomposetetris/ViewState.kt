package com.example.testcomposetetris

import com.example.testcomposetetris.domain.models.Position


data class ViewState(
    val tiles: List<List<Boolean>>,
    val nextPiecePreview: List<Position>
) {
}