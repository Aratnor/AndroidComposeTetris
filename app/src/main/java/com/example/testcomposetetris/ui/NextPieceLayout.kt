package com.example.testcomposetetris.ui


import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.testcomposetetris.R
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.ext.drawText
import com.example.testcomposetetris.ui.theme.fonts

fun DrawScope.nextPieceLayout(
    viewState: ViewState,
    boardWidth: Float,
    boardHeight: Float,
    gameFont: Typeface?,
    font: Typeface?,
    numberFont: Typeface?
) {


    if(!viewState.gameOver) {
        val totalAvailableHeight = size.height - boardHeight - 40
        val widthOfEachRect = totalAvailableHeight / 4
        val previewPadding = 4F
        val xOffset = 16F
        val yOffset = 16F
            drawNextPiecePreview(
                viewState,
                widthOfEachRect,
                previewPadding,
                xOffset,
                yOffset
            )


        val startX = (widthOfEachRect + previewPadding) * 4 + xOffset + 12
        val centerY = totalAvailableHeight / 2

        val paint = Paint().apply {
            textSize = 64F
            color = Color.BLACK
            typeface = gameFont
            textAlign = Paint.Align.CENTER
        }
        val eachTextWidth = (size.width - startX) / 3

        val scoreText = "Score"

        drawText(
            scoreText,
            startX + 94,
            centerY - 12,
            paint
            )

        val scoreTextBound = Rect()
        paint.getTextBounds(scoreText,0,scoreText.length,scoreTextBound)

        drawText(
            viewState.score,
            startX + scoreTextBound.width() / 2 + 38,
            centerY + scoreTextBound.height() + 8,
            Paint().apply {
                textSize = 64F
                color = Color.BLACK
                typeface = numberFont
                textAlign = Paint.Align.CENTER
            }
        )

        val levelText = "Level"

        drawText(
            levelText,
            startX + 64   + eachTextWidth *  1,
            centerY - 12,
            paint
        )

        val levelTextBound = Rect()
        paint.getTextBounds(levelText,0,levelText.length,levelTextBound)

        drawText(
            viewState.level,
            startX + 64  + eachTextWidth *  1,
            centerY + levelTextBound.height(),
            Paint().apply {
                textSize = 64F
                color = Color.BLACK
                typeface = numberFont
                textAlign = Paint.Align.CENTER
            }
        )

        val timerText = "Timer : "


        drawText(
            "Timer : ",
            startX + 64  + eachTextWidth *  2,
            centerY - 12,
            Paint().apply {
                textSize = 64F
                color = Color.BLACK
                typeface = font
                textAlign = Paint.Align.CENTER
            }
        )


        val timerTextBound = Rect()
        paint.getTextBounds(timerText,0,timerText.length,timerTextBound)

        drawText(
            viewState.currentTime,
            startX + 48  + eachTextWidth *  2,
            centerY + timerTextBound.height() + 12,
            Paint().apply {
                textSize = 72F
                style = Paint.Style.FILL
                color = Color.BLACK
                typeface = font
                textAlign = Paint.Align.CENTER
            }
        )
        }
}

private fun DrawScope.drawNextPiecePreview(
    viewState: ViewState,
    widthOfRect: Float,
    padding: Float,
    xOffset: Float,
    yOffset: Float,
) {

    repeat(4) { posY ->
        repeat(4) { posX ->
            val startPositionX = xOffset + (posX) * (padding + widthOfRect)
            val startPositionY = (posY) * (padding + widthOfRect) + yOffset
            val isNotEmpty =viewState
                .nextPiecePreview
                .firstOrNull { it.x == posX && it.y == posY } != null
            tile(
                Offset(startPositionX,startPositionY),
                widthOfRect
                ,isNotEmpty,
                cornerRadius = 4F
            )
        }
    }
}