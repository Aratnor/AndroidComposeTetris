package com.example.testcomposetetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.testcomposetetris.MainViewModel

@Composable
fun Board(viewModel: MainViewModel) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .padding(start = 8.dp)

    ) {
        val totalWidth = size.width
        val numberOfRectInRow = 12
        val padding = 12
        val totalWidthWithoutMargin = totalWidth - (numberOfRectInRow - 1) * padding
        val widthOfRectangle = 40F

        viewState.tiles.forEachIndexed { positionY, row ->
            row.forEachIndexed { positionX, isOccupied ->
                val startPositionX = (positionX) * (padding + widthOfRectangle)
                val startPositionY = (positionY) * (padding + widthOfRectangle)
                tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
            }
        }
    }
}