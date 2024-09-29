package game.fabric.blockflow.ui

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import game.fabric.blockflow.MainViewModel
import game.fabric.blockflow.NavDestination
import game.fabric.blockflow.NavDestination.navigateGameOver
import game.fabric.blockflow.R
import game.fabric.blockflow.ViewState
import game.fabric.blockflow.gamelogic.domain.models.ButtonIcons
import game.fabric.blockflow.util.ResourceUtil
import game.fabric.blockflow.util.SoundUtil

@Composable
fun Board(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

    val scoreTitleFont = ResourcesCompat.getFont(LocalContext.current, R.font.roboto_regular)

    val scoreValueFont = ResourcesCompat.getFont(LocalContext.current, R.font.roboto_bold)

    ResourceUtil.init(LocalContext.current.resources)

    val padding = 12F

    Canvas(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()

    ) {
        if (viewState.tiles.isEmpty()) return@Canvas

        Log.i("Board :", "Width ${size.width} Height $size")

        if (viewModel.rectangleWidth == -1F) {
            viewModel.rectangleWidth = calculateWidthOfRect(
                padding,
                5F,
                viewState
            )
        }

        if (viewState.gameOver && navController.currentBackStackEntry?.destination?.route != NavDestination.GAME_OVER) {
            SoundUtil.stopGameTheme()
            navController.navigate(
                navigateGameOver(
                    viewState.gameOverScore,
                    viewState.gameOverLevel
                )
            )
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
                .firstOrNull { it.x == positionX && it.y == positionY } != null
            val startPositionX = (positionX) * (padding + widthOfRectangle) + marginStart
            val startPositionY = (positionY) * (padding + widthOfRectangle) + marginTop
            tile(Offset(startPositionX, startPositionY), widthOfRectangle, isShadowed, tile)
        }
    }

    if (
        viewState.moveUpState.isMoveUpActive &&
        viewState.moveUpState.moveUpMovementCount > 0
    ) {
        viewModel.calculateMoveUpEffect(padding,widthOfRectangle,marginStart,marginTop)
    } else {
        viewModel.removeMoveUpEffect()
    }

    viewState.moveUpEffectUiState.takeIf { it.isNotEmpty() }?.forEach {
        moveUpEffect(
            it.initialOffset,
            it.destinationOffset,
            it.size,
            it.color
        )
    }


    val muteButtonXPos = maxWidth + marginStart * 1.25.toFloat()
    val muteButtonYPos = marginTop + 16
    viewModel.muteButtonOffset = Offset(muteButtonXPos, muteButtonYPos)

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
        openSoundBitmap
    ) {
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
        openMusicBitmap
    ) {
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
        maxHeight + 24,
        marginStart,
        scoreTitleFont,
        scoreValueFont
    )
}