package com.example.testcomposetetris.ui

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.ViewState

@Composable
fun Board(viewModel: MainViewModel) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .padding(start = 8.dp)

    ) {


        if(viewState.gameOver) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "GAME OVER",
                    viewState.tiles[0].size.div(2) * (12 + 40F),
                    viewState.tiles.size.div(2) * (12 + 40F),
                    Paint().apply {
                        textSize = 48F
                        color = Color.BLACK
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        } else {
            drawBoard(viewState)
        }
    }
}

private fun DrawScope.drawBoard(
    viewState: ViewState
) {
    val padding = 12
    val widthOfRectangle = 40F

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, isOccupied ->
            val startPositionX = (positionX) * (padding + widthOfRectangle)
            val startPositionY = (positionY) * (padding + widthOfRectangle)
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
        }
    }
}