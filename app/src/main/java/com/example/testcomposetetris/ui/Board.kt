package com.example.testcomposetetris.ui

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.ext.drawText
import com.example.testcomposetetris.orZero

@Composable
fun Board(viewModel: MainViewModel) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxSize()

    ) {
        if(viewState.tiles.isEmpty()) return@Canvas


        Log.i("Board :","Width ${size.width} Height $size")

        val padding = 12F

        val nextPieceLayoutMaxX = 250
        val nextPieceLayoutMaxY = 250

        val paddingForTotalWidth = (viewState.tiles[0].size - 1) * padding
        val rectWidthByLayoutWidth = (size.width - paddingForTotalWidth - nextPieceLayoutMaxX) / viewState.tiles[0].size
        val totalTilesHeight = (rectWidthByLayoutWidth + padding) * viewState.tiles.size
        val widthOfRectangle = if(totalTilesHeight > size.height - nextPieceLayoutMaxY) {
            val paddingForTotalHeight = (viewState.tiles.size - 1) * padding
            val rectWidthByLayoutHeight = (size.height - paddingForTotalHeight - nextPieceLayoutMaxY) / viewState.tiles.size
            rectWidthByLayoutHeight
        } else {
            rectWidthByLayoutWidth
        }

        if(viewState.gameOver) {
            drawGameOverLayout(viewState)
        } else {
            drawBoard(viewState,widthOfRectangle,padding)
        }
    }
}

private fun DrawScope.drawGameOverLayout(
    viewState: ViewState
) {

    drawText(
        "GAME OVER",
        viewState.tiles[0].size.div(2) * (12 + 40F),
        viewState.tiles.size.div(2) * (12 + 40F),
        Paint().apply {
            textSize = 72F
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
        }
    )

    drawText(
        viewState.gameOverScore,
        viewState.tiles[0].size.div(2) * (12 + 40F),
        viewState.tiles.size.div(2) * (12 + 40F) + 80,
        Paint().apply {
            textSize =52F
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
        }
    )

    drawText(
        viewState.gameOverLevel,
        viewState.tiles[0].size.div(2) * (12 + 40F),
        viewState.tiles.size.div(2) * (12 + 40F) + 160,
        Paint().apply {
            textSize =52F
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
        }
    )
}

private fun DrawScope.drawBoard(
    viewState: ViewState,
    widthOfRectangle: Float,
    padding: Float
) {

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, isOccupied ->
            val startPositionX = (positionX) * (padding + widthOfRectangle)
            val startPositionY = (positionY) * (padding + widthOfRectangle)
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
        }
    }

    val posXOffSet = viewState.tiles.firstOrNull()?.size.orZero() * (padding + widthOfRectangle) + 40
    nextPieceLayout(viewState,widthOfRectangle,posXOffSet)
}