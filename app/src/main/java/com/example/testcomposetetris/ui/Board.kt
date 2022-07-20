package com.example.testcomposetetris.ui

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.NavDestination
import com.example.testcomposetetris.NavDestination.navigateGameOver
import com.example.testcomposetetris.R
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.orZero
import com.example.testcomposetetris.ui.theme.DARK_BLUE
import com.example.testcomposetetris.ui.theme.OUT_RECT
import com.example.testcomposetetris.util.SoundUtil

@Composable
fun Board(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.value

    val timerFont =  ResourcesCompat.getFont(LocalContext.current, R.font.ds_digit)

    val gameFont = ResourcesCompat.getFont(LocalContext.current, R.font.game_font_2)

    val numberFont = ResourcesCompat.getFont(LocalContext.current, R.font.number_font)

    Canvas(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()

    ) {
        if(viewState.tiles.isEmpty()) return@Canvas

        Log.i("Board :","Width ${size.width} Height $size")

        val padding = 12F

        if(viewModel.rectangleWidth == -1F) {
            viewModel.rectangleWidth = calculateWidthOfRect(
                padding,
                5F,
                viewState
            )
        }

        if(viewState.gameOver && navController.currentBackStackEntry?.destination?.route != NavDestination.GAME_OVER) {
            SoundUtil.stopGameTheme()
            navController.navigate(navigateGameOver(viewState.gameOverScore,viewState.gameOverLevel))
        } else {
            drawBoard(viewState,viewModel.rectangleWidth,padding,gameFont,timerFont,numberFont)
        }
    }
}

private fun DrawScope.drawBoard(
    viewState: ViewState,
    widthOfRectangle: Float,
    padding: Float,
    gameFont: Typeface?,
    typeface: Typeface?,
    numberFont: Typeface?
) {

    val maxWidth = (padding + widthOfRectangle) * viewState.tiles[0].size + 30F
    val maxHeight = (padding + widthOfRectangle) * viewState.tiles.size + 18

    val totalMarginHeight = size.height - maxHeight
    val totaMarginWidth = size.width - maxWidth

    val marginTop = totalMarginHeight - 8
    val marginStart = totaMarginWidth / 2

    drawShadowBehindTile(
        topLeftPosition = Offset(marginStart - 12,marginTop - 12),
        width = maxWidth,
        height = maxHeight,
        topShadowMultiplier = 0.015F,
        leftShadowMultiplier = 0.04F,
        rightShadowMultiplier = 0.04F,
        bottomShadowMultiplier = 0.04F,
        shadowRadius = 10F,
        alpha = 0.1F
    )

    drawRoundRect(
        OUT_RECT,
        Offset(marginStart - 12,marginTop - 12),
        Size(maxWidth + 24,maxHeight + 24),
        CornerRadius(25F,25F),
        Stroke(24F),
        1F
    )

    drawRect(
        DARK_BLUE,
        Offset(marginStart,marginTop),
        Size(maxWidth,maxHeight),
        1F,
        Fill
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

    nextPieceLayout(viewState,maxWidth + 24,maxHeight  + 24,gameFont,typeface,numberFont)
}