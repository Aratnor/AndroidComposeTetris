package com.example.testcomposetetris.ui


import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.ext.drawText

fun DrawScope.nextPieceLayout(
    viewState: ViewState,
    widthOfRectangle: Float,
    posXOffset: Float,
    boardWidth: Float,
    boardHeight: Float,
    marginTop: Float
) {
        val padding = 24F
        val centerX = (size.width - boardWidth) / 2

        if(!viewState.gameOver) {
            drawNextPiecePreview(
                viewState,
                widthOfRectangle,
                posXOffset + 40,
                padding,
                marginTop
            )
        }

        drawText(
            viewState.score,
            boardWidth + centerX,
            boardHeight / 2,
            Paint().apply {
                textSize = 64F
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
        )

        drawText(
            viewState.level,
            boardWidth + centerX,
            boardHeight / 2 + 100,
            Paint().apply {
                textSize = 84F
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
        )

    drawText(
        "Timer :",
        boardWidth + centerX,
        boardHeight - 120,
        Paint().apply {
            textSize = 68F
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
        }
    )

    drawText(
        viewState.currentTime,
        boardWidth + centerX,
        boardHeight,
        Paint().apply {
            textSize = 102F
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
        }
    )
}

private fun DrawScope.drawNextPiecePreview(
    viewState: ViewState,
    widthOfRectangle: Float,
    posXOffset: Float,
    padding: Float,
    marginTop: Float
) {

    repeat(4) { posY ->
        repeat(4) { posX ->
            val startPositionX = posXOffset + (posX) * (padding + widthOfRectangle)
            val startPositionY = (posY) * (padding + widthOfRectangle) + marginTop
            val isNotEmpty =viewState
                .nextPiecePreview
                .firstOrNull { it.x == posX && it.y == posY } != null
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isNotEmpty)
        }
    }
}