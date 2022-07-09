package com.example.testcomposetetris.ui

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.ext.drawText

@Composable
fun Board(viewModel: MainViewModel) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .padding(start = 8.dp)

    ) {


        if(viewState.gameOver) {
            drawGameOverLayout(viewState)
        } else {
            drawBoard(viewState)
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
            textAlign = Paint.Align.CENTER
        }
    )

    drawText(
        viewState.gameOverScore,
        viewState.tiles[0].size.div(2) * (12 + 40F),
        viewState.tiles.size.div(2) * (12 + 40F) + 80,
        Paint().apply {
            textSize =52F
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
        }
    )

    drawText(
        viewState.gameOverLevel,
        viewState.tiles[0].size.div(2) * (12 + 40F),
        viewState.tiles.size.div(2) * (12 + 40F) + 160,
        Paint().apply {
            textSize =52F
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
        }
    )
}

private fun DrawScope.drawBorder(
) {
    drawRect(
        androidx.compose.ui.graphics.Color.Black,
        Offset(0F,0F),
        Size(size.width,size.height),
        1F,
        Stroke(2F),
    )
}

private fun DrawScope.drawBoard(
    viewState: ViewState
) {
    val padding = 12
    val widthOfRectangle = 45F

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, isOccupied ->
            val startPositionX = (positionX) * (padding + widthOfRectangle)
            val startPositionY = (positionY) * (padding + widthOfRectangle)
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied)
        }
    }
}