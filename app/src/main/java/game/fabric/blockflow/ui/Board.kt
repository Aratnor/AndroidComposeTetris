package game.fabric.blockflow.ui

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import game.fabric.blockflow.MainViewModel
import game.fabric.blockflow.NavDestination
import game.fabric.blockflow.NavDestination.navigateGameOver
import game.fabric.blockflow.R
import game.fabric.blockflow.ViewState
import game.fabric.blockflow.gamelogic.domain.models.ButtonIcons
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.util.ResourceUtil
import game.fabric.blockflow.util.SoundUtil
import java.lang.NullPointerException

@Composable
fun Board(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.value

    val scoreTitleFont =  ResourcesCompat.getFont(LocalContext.current, R.font.roboto_regular)

    val scoreValueFont = ResourcesCompat.getFont(LocalContext.current, R.font.roboto_bold)

    ResourceUtil.init(LocalContext.current.resources)

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
                scoreValueFont
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
    scoreValueFont: Typeface?
    ) {

    val maxWidth = (padding + widthOfRectangle) * viewState.tiles[0].size
    val maxHeight = (padding + widthOfRectangle) * viewState.tiles.size + 18

    val totalMarginHeight = size.height - maxHeight
    val totalMarginWidth = size.width - maxWidth

    val marginTop = totalMarginHeight - 8
    val marginStart = totalMarginWidth / 2

    viewState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, tile ->
            val isShadowed = viewState
                .pieceFinalLocation
                .firstOrNull { it.x == positionX && it.y == positionY} != null
            val startPositionX = (positionX) * (padding + widthOfRectangle) + marginStart
            val startPositionY = (positionY) * (padding + widthOfRectangle) + marginTop
            tile(Offset(startPositionX,startPositionY),widthOfRectangle,isShadowed,tile)
        }
    }
    if(
        viewState.moveUpState.isMoveUpActive &&
        viewState.moveUpState.moveUpMovementCount > 0) {
        val moveUpLocations: List<Position?> = viewState.moveUpState.moveUpInitialLocations.toMutableList()
        val maxX = moveUpLocations.maxOf { it?.x ?: -1 }
        moveUpLocations.forEach {
            if (it == null) return@forEach
            if(it.x < 0 || it.y < 0 ) return@forEach
            val isUpperTileIsEmpty =
                it.y == 0 ||
                        moveUpLocations
                            .firstOrNull { t ->  t?.y == it.y -1 && t.x == it.x } == null

            if(isUpperTileIsEmpty) {
                var hasTileOnLeft = false
                var hasTileOnLeftTop = false
                var hasTileOnRight = false
                var hasTileOnRightTop = false
                moveUpLocations.forEach { nextTile ->
                    if(nextTile?.x == it.x-1) {
                        hasTileOnLeft = true
                    }
                    if(nextTile?.x == it.x-1 && nextTile.y == it.y-1) {
                        hasTileOnLeftTop = true
                    }
                    if(nextTile?.x == it.x+1) {
                        hasTileOnRight = true
                    }
                    if(nextTile?.x == it.x+1 && nextTile.y == it.y+1) {
                        hasTileOnRightTop = true
                    }
                }
                val initialLeftX = (it.x) * (padding + widthOfRectangle) + marginStart
                var leftXOffset = 0F
                val leftX = when {
                    hasTileOnLeft && hasTileOnLeftTop -> {
                        leftXOffset += padding
                        initialLeftX - padding
                    }
                    else -> {
                        leftXOffset = 0F
                        initialLeftX
                    }
                }
                val rightX = when (it.x) {
                    maxX -> leftX + widthOfRectangle + leftXOffset
                    else -> leftX + widthOfRectangle + padding + leftXOffset
                }
                val endY = (it.y) * (padding + widthOfRectangle) + marginTop
                val height = 200F
                val startY = endY - height

                moveUpEffect(
                    initialOffset = Offset(leftX,startY),
                    destinationOffset = Offset(leftX,endY),
                    Size(rightX - leftX,height),
                    viewState.moveUpState.currentPiece.pieceColor
                )
            }
        }
    }





    val muteButtonXPos = maxWidth + marginStart * 1.25.toFloat()
    val muteButtonYPos = marginTop + 16
    viewModel.muteButtonOffset = Offset(muteButtonXPos,muteButtonYPos)

    val muteMusicYPos = muteButtonYPos + viewModel.muteButtonSize.height + 80
    viewModel.muteMusicOffset = Offset(muteButtonXPos, muteMusicYPos)

    viewModel.playButtonOffset = Offset(
        muteButtonXPos,
        muteMusicYPos + viewModel.muteMusicSize.height + 80
    )

    val muteSoundBitmap = ResourceUtil.getBitmap(
        ButtonIcons.MUTE_SOUND.resId
    )

    val openSoundBitmap = ResourceUtil.getBitmap(
        ButtonIcons.UNMUTE_SOUND.resId
    )

    soundButton(
        viewModel.muteButtonOffset,
        viewModel.isMuted,
        muteSoundBitmap,
        openSoundBitmap) {
        viewModel.muteButtonSize = it
    }

    val muteMusicBitmap = ResourceUtil.getBitmap(
        ButtonIcons.MUTE_MUSIC.resId
    )

    val openMusicBitmap = ResourceUtil.getBitmap(
        ButtonIcons.UNMUTE_MUSIC.resId
    )

    soundButton(
        viewModel.muteMusicOffset,
        viewModel.isMusicMuted,
        muteMusicBitmap,
        openMusicBitmap) {
        viewModel.muteMusicSize = it
    }

    val pauseGameBitmap = ResourceUtil.getBitmap(
        ButtonIcons.PLAY.resId
    )

    val playGameBitmap = ResourceUtil.getBitmap(
        ButtonIcons.PAUSE.resId
    )

    soundButton(
        viewModel.playButtonOffset,
        viewModel.isGamePaused,
        pauseGameBitmap,
        playGameBitmap
    ) {
        viewModel.playButtonSize = it
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