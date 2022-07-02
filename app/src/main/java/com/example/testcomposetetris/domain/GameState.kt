package com.example.testcomposetetris.domain

import com.example.testcomposetetris.domain.models.Position

data class GameState(
    val tiles: List<List<Boolean>>,
    val previewLocation: List<Position>
)