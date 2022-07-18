package com.example.testcomposetetris.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.NavDestination
import com.example.testcomposetetris.NavDestination.navigateGameOver
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.orZero

@Composable
fun Board(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()

    ) {
        if(viewState.tiles.isEmpty()) return@Canvas

        Log.i("Board :","Width ${size.width} Height $size")

        val padding = 12F

        val nextPieceLayoutMaxX = 250F
        val nextPieceLayoutMaxY = 250F

        if(viewModel.rectangleWidth == -1F) {
            viewModel.rectangleWidth = calculateWidthOfRect(padding,5F,nextPieceLayoutMaxX,nextPieceLayoutMaxY,viewState)
        }

        if(viewState.gameOver && navController.currentBackStackEntry?.destination?.route != NavDestination.GAME_OVER) {
            navController.navigate(navigateGameOver(viewState.gameOverScore,viewState.gameOverLevel))
        } else {
            drawBoard(viewState,viewModel.rectangleWidth,padding)
        }
    }
}

private fun DrawScope.drawBoard(
    viewState: ViewState,
    widthOfRectangle: Float,
    padding: Float
) {
    val marginTop = 20F
    val marginStart = 20F

    val maxWidth = (padding + widthOfRectangle) * viewState.tiles[0].size + 30F
    val maxHeight = (padding + widthOfRectangle) * viewState.tiles.size + 18

    drawRoundRect(
        Color.Black,
        Offset(1F,5F),
        Size(maxWidth,maxHeight),
        CornerRadius(25F,25F),
        Stroke(6F),
        1F
    )

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, isOccupied ->
            val isShadowed = viewState
                .pieceFinalLocation
                .firstOrNull { it.x == positionX && it.y == positionY} != null
            val startPositionX = (positionX) * (padding + widthOfRectangle) + marginStart
            val startPositionY = (positionY) * (padding + widthOfRectangle) + marginTop
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied,isShadowed)
        }
    }

    val posXOffSet = viewState.tiles.firstOrNull()?.size.orZero() * (padding + widthOfRectangle) + 40
    nextPieceLayout(viewState,widthOfRectangle,posXOffSet,maxWidth,maxHeight)
}