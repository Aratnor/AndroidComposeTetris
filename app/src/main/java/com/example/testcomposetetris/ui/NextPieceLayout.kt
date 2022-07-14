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
    posXOffset: Float
) {
        val padding = 12F

        if(!viewState.gameOver) {
            drawNextPiecePreview(viewState,widthOfRectangle,posXOffset,padding)
        }

        drawText(
            viewState.currentTime,
            posXOffset + widthOfRectangle + padding,
            4 * (padding + widthOfRectangle) + 70,
            Paint().apply {
                textSize = 48F
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
        )

        drawText(
            viewState.score,
            posXOffset + widthOfRectangle + padding + 16,
            4 * (padding + widthOfRectangle) + 140,
            Paint().apply {
                textSize = 48F
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
        )

        drawText(
            viewState.level,
            posXOffset + widthOfRectangle + padding + 12,
            4 * (padding + widthOfRectangle) + 220,
            Paint().apply {
                textSize = 48F
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
        )
}

private fun DrawScope.drawNextPiecePreview(
    viewState: ViewState,
    widthOfRectangle: Float,
    posXOffset: Float,
    padding: Float
) {

    repeat(4) { posY ->
        repeat(4) { posX ->
            val startPositionX = posXOffset + (posX) * (padding + widthOfRectangle)
            val startPositionY = (posY) * (padding + widthOfRectangle)
            val isNotEmpty =viewState
                .nextPiecePreview
                .firstOrNull { it.x == posX && it.y == posY } != null
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isNotEmpty)
        }
    }
}