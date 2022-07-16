package com.example.testcomposetetris.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
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
            .padding(start = 8.dp)
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

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, isOccupied ->
            val isShadowed = viewState
                .pieceFinalLocation
                .firstOrNull { it.x == positionX && it.y == positionY} != null
            val startPositionX = (positionX) * (padding + widthOfRectangle) + 5
            val startPositionY = (positionY) * (padding + widthOfRectangle) + 2
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isOccupied,isShadowed)
        }
    }

    val posXOffSet = viewState.tiles.firstOrNull()?.size.orZero() * (padding + widthOfRectangle) + 40
    nextPieceLayout(viewState,widthOfRectangle,posXOffSet)
}