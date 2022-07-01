package com.example.testcomposetetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.example.testcomposetetris.MainViewModel

@Composable
fun Board(viewModel: MainViewModel) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        val totalWidth = size.width
        val numberOfRectInRow = 12
        val padding = 12
        val totalWidthWithoutMargin = totalWidth - (numberOfRectInRow - 1) * padding
        val widthOfRectangle = totalWidthWithoutMargin / numberOfRectInRow

        viewState.tiles.forEachIndexed { positionY, row ->
            row.forEachIndexed { positionX, isOccupied ->
                val startPositionX = (positionX) * (padding + widthOfRectangle)
                val startPositionY = (positionY) * (padding + widthOfRectangle)
                tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
            }
        }
    }
}