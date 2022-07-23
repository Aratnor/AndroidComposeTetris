package com.example.testcomposetetris.ui


import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.toArgb
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.domain.models.Tile
import com.example.testcomposetetris.ext.drawText
import com.example.testcomposetetris.ui.theme.*

fun DrawScope.nextPieceLayout(
    viewState: ViewState,
    boardWidth: Float,
    boardHeight: Float,
    marginStart: Float,
    scoreTitleFont: Typeface?,
    scoreValueFont: Typeface?
) {


    if(!viewState.gameOver) {
        val totalAvailableHeight = size.height - boardHeight - 40
        val marginLeft = 48
        val marginStartEnd = 24F

        val availableWidthForEachPart = (size.width - marginStart * 2 - marginLeft * 2) / 3
        val yOffset = 16F
        drawPreviewLayout(
            Offset(
                marginStart,
                yOffset
            ),
            Size(
                availableWidthForEachPart,
                totalAvailableHeight),
            scoreTitleFont,
            viewState
        )

        drawLevelLayout(
            Offset(
                marginStart + availableWidthForEachPart + marginLeft,
                yOffset
            ),
            Size(
                availableWidthForEachPart,
                totalAvailableHeight),
            scoreValueFont,
            scoreTitleFont,
            "Level",
            viewState.level
        )

        drawLevelLayout(
            Offset(
                marginStart + (availableWidthForEachPart + marginLeft) * 2,
                yOffset
            ),
            Size(
                availableWidthForEachPart,
                totalAvailableHeight),
            scoreValueFont,
            scoreTitleFont,
            "Score",
            viewState.score
        )
    }
}

private fun DrawScope.drawPreviewLayout(
    leftTopStartOffset: Offset,
    size: Size,
    titleFont: Typeface?,
    viewState: ViewState,
) {
    drawRoundRect(
        SCORE_BACKGROUND_COLOR,
        leftTopStartOffset,
        size,
        CornerRadius(16F,16F),
        Fill,
        1F
    )

    val titlePaint = Paint().apply {
        textSize = size.width / 8F
        color = SCORE_TITLE_TEXT_COLOR.toArgb()
        typeface = titleFont
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    val title = "Next"
    val titleTextBound = Rect()
    titlePaint.getTextBounds(title,0,title.length,titleTextBound)


    drawText(
        title,
        leftTopStartOffset.x + titleTextBound.width() / 2 + size.width / 12,
        leftTopStartOffset.y + titleTextBound.height() / 2 + size.height / 8,
        titlePaint
    )

    val previewPadding = 4F

    val widthOfEachRect = (size.height * 2 / 3 - previewPadding * 3) / 4

    val totalWidthOfPreview = (widthOfEachRect ) * 4 + previewPadding * 3

    drawNextPiecePreview(
        viewState,
        widthOfEachRect,
        previewPadding,
        leftTopStartOffset.x + ( size.width - totalWidthOfPreview) / 2 + 20,
        leftTopStartOffset.y + (size.height - totalWidthOfPreview) - 10
    )
}

private fun DrawScope.drawLevelLayout(
    leftTopStartOffset: Offset,
    size: Size,
    valueFont: Typeface?,
    titleFont: Typeface?,
    title: String,
    value: String
) {
    drawRoundRect(
        SCORE_BACKGROUND_COLOR,
        leftTopStartOffset,
        size,
        CornerRadius(16F,16F),
        Fill,
        1F
    )


    val valuePaint = Paint().apply {
        textSize = size.width / 1.6F
        color = SCORE_VALUE_TEXT_COLOR.toArgb()
        typeface = valueFont
        textAlign = Paint.Align.CENTER
    }

    val titlePaint = Paint().apply {
        textSize = size.width / 8F
        color = SCORE_TITLE_TEXT_COLOR.toArgb()
        typeface = titleFont
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    val valueStrokePaint = Paint().apply {
        textSize = size.width / 1.6F
        typeface = valueFont
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4F
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }


    val titleTextBound = Rect()
    titlePaint.getTextBounds(title,0,title.length,titleTextBound)

    drawText(
        title,
        leftTopStartOffset.x + titleTextBound.width() / 2 + size.width / 12,
        leftTopStartOffset.y + titleTextBound.height() / 2 + size.height / 8,
        titlePaint
    )

    val centerX = leftTopStartOffset.x + size.width / 2
    val centerY = leftTopStartOffset.y + size.height / 2


    val initialTextSize = (size.width / 1.6).toInt()
    var finalRect = Rect()
    kotlin.run {
        for(textSize in initialTextSize downTo 8) {
            valuePaint.textSize = textSize.toFloat()
            valueStrokePaint.textSize = textSize.toFloat()
            val valueTextBound = Rect()
            valuePaint.getTextBounds(value,0,value.length,valueTextBound)
            if(valueTextBound.height()  < size.height - 50 && valueTextBound.width() < size.width - 50) {
                finalRect = valueTextBound
                return@run
            }
        }
    }

    val startPosY = centerY + finalRect.height() / 2 + 12
    drawText(
        value,
        centerX,
        startPosY,
        valuePaint
    )
    drawText(
        value,
        centerX,
        startPosY,
        valueStrokePaint
    )
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
                .first
                .firstOrNull { it.x == posX && it.y == posY } != null
            tile(
                Offset(startPositionX,startPositionY),
                widthOfRect
                ,isNotEmpty,
                tile  = Tile(isNotEmpty,false,viewState.nextPiecePreview.second),
                cornerRadius = 4F,
                isInvisible = !isNotEmpty
            )
        }
    }
}