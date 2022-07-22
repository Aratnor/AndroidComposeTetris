package com.example.testcomposetetris.ui

import android.graphics.Bitmap
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.example.testcomposetetris.MainViewModel
import com.example.testcomposetetris.NavDestination
import com.example.testcomposetetris.NavDestination.navigateGameOver
import com.example.testcomposetetris.R
import com.example.testcomposetetris.ViewState
import com.example.testcomposetetris.ui.theme.DARK_BLUE
import com.example.testcomposetetris.ui.theme.OUT_RECT
import com.example.testcomposetetris.util.SoundUtil

@Composable
fun Board(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.value

    val scoreTitleFont =  ResourcesCompat.getFont(LocalContext.current, R.font.roboto_regular)

    val scoreValueFont = ResourcesCompat.getFont(LocalContext.current, R.font.roboto_bold)

    val numberFont = ResourcesCompat.getFont(LocalContext.current, R.font.number_font)

    val muteSoundIcon =  ResourcesCompat.getDrawable(
        LocalContext.current.resources,
        R.drawable.ic_baseline_volume_off_24,
        null
    )?.toBitmap()

    val openSoundIcon =ResourcesCompat.getDrawable(
        LocalContext.current.resources,
        R.drawable.ic_baseline_volume_up_24,
        null
    )?.toBitmap()

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
            drawBoard(
                viewModel,
                viewState,
                viewModel.rectangleWidth,
                padding,
                scoreTitleFont,
                scoreValueFont,
                muteSoundIcon,
                openSoundIcon
            )
        }
    }
}

private fun DrawScope.drawBoard(
    viewModel: MainViewModel,
    viewState: ViewState,
    widthOfRectangle: Float,
    padding: Float,
    scoreTitleFont: Typeface?,
    scoreValueFont: Typeface?,
    muteSoundPainter: Bitmap?,
    openSoundPainter: Bitmap?,
    ) {

    val maxWidth = (padding + widthOfRectangle) * viewState.tiles[0].size
    val maxHeight = (padding + widthOfRectangle) * viewState.tiles.size + 18

    val totalMarginHeight = size.height - maxHeight
    val totalMarginWidth = size.width - maxWidth

    val marginTop = totalMarginHeight - 8
    val marginStart = totalMarginWidth / 2

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

    val muteButtonXPos = maxWidth + marginStart * 1.25.toFloat()
    val muteButtonYPos = marginTop + 16
    viewModel.muteButtonOffset = Offset(muteButtonXPos,muteButtonYPos)

    if(viewModel.isMuted) {
        muteSoundPainter?.let {
            viewModel.muteButtonSize = Size(it.width.toFloat(),it.height.toFloat())
            drawImage(
                it.asImageBitmap(),
                viewModel.muteButtonOffset
            )

        }
    } else {
        openSoundPainter?.let {
            viewModel.muteButtonSize = Size(it.width.toFloat(),it.height.toFloat())

            drawImage(
                it.asImageBitmap(),
                viewModel.muteButtonOffset
            )
        }

    }
    nextPieceLayout(
        viewState,
        maxWidth + 24,
        maxHeight  + 24,
        marginStart,
        scoreTitleFont,
        scoreValueFont
    )
}