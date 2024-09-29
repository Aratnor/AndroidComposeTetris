package game.fabric.blockflow

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import game.fabric.blockflow.ext.convertToMinute
import game.fabric.blockflow.ext.convertToRemainingSecond
import game.fabric.blockflow.gamelogic.GameConfig
import game.fabric.blockflow.gamelogic.MoveUpState
import game.fabric.blockflow.gamelogic.domain.Game
import game.fabric.blockflow.gamelogic.domain.SoundPlayAction
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor
import game.fabric.blockflow.gamelogic.domain.models.piece.LPiece
import game.fabric.blockflow.ui.MoveUpEffectUiState
import game.fabric.blockflow.util.SoundType
import game.fabric.blockflow.util.SoundUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.NoSuchElementException

class MainViewModel : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(
        ViewState(
            List(GameConfig.ROW_SIZE) {
                List(GameConfig.COLUMN_SIZE) {
                    Tile(isOccupied = false, hasActivePiece = false, color = TileColor.EMPTY)
                }
            },
            emptyList<Position>() to TileColor.EMPTY,
            emptyList(),
            "00:00",
            MoveUpState(false, emptyList(), -1, LPiece(-1, -1)),
            "",
            "Level 1",
            false,
            "",
            ""
        )
    )

    val viewState: StateFlow<ViewState> = _viewState

    private val game = Game()

    private lateinit var timerJob: Job

    var rectangleWidth = -1F

    var muteButtonOffset = Offset(0F, 0F)
    var muteButtonSize = Size(120F, 120F)

    var muteMusicOffset = Offset(0F, 0F)
    var muteMusicSize = Size(120F, 120F)

    var playButtonOffset = Offset(0F, 0F)
    var playButtonSize = Size(120F, 120F)

    var isMuted = false
    var isMusicMuted = false
    var isGamePaused = false


    private fun start() {
        viewModelScope.launch(Dispatchers.Default) {
            game.startGame()
        }
    }

    private fun startTimer() {
        val isInitialized = this::timerJob.isInitialized
        if (isInitialized) {
            timerJob.isActive
            return
        }
        timerJob = viewModelScope.launch {
            val flow = (0..Int.MAX_VALUE)
                .asSequence()
                .asFlow()
                .onEach { delay(1_000) }

            flow.collectLatest { second ->
                val minuteAsString = second.convertToMinute()
                val secondAsString = second.convertToRemainingSecond()
                if (game.isGameOver()) {
                    timerJob.cancel()
                    return@collectLatest
                }

                _viewState.value = _viewState
                    .value
                    .copy(
                        currentTime = "$minuteAsString:$secondAsString"
                    )
            }
        }
    }

    suspend fun collect() {
        game.updateUi.collectLatest {
            _viewState.value = _viewState.value.copy(
                tiles = it.tiles,
                nextPiecePreview = it.previewLocation,
                pieceFinalLocation = it.pieceDestinationLocation,
                score = it.score,
                gameOverScore = "SCORE : ${game.getScore()}",
                gameOverLevel = "LEVEL : ${game.getLevel()}",
                level = it.difficultyLevel,
                gameOver = it.isGameOver,
                moveUpState = it.moveUpState
            )
        }
    }

    fun removeMoveUpEffect() = viewModelScope.launch {
        _viewState.update { it.copy(moveUpEffectUiState = emptyList()) }
    }

    fun calculateMoveUpEffect(
        padding: Float,
        widthOfRectangle: Float,
        marginStart: Float,
        marginTop: Float
    ) = viewModelScope.launch(Dispatchers.Default) {
        val state = viewState.value
        if (
            state.moveUpState.isMoveUpActive &&
            state.moveUpState.moveUpMovementCount > 0
        ) {
            val moveUpLocations: List<Position?> =
                state.moveUpState.moveUpInitialLocations.toList()
            val maxX = try {
                moveUpLocations.maxOf { it?.x ?: -1 }
            } catch (exception: NoSuchElementException) {
                return@launch
            }
            val moveUpEffectList = moveUpLocations
                .filterNotNull()
                .filter {
                    val isUpperTileIsEmpty =
                        it.y == 0 ||
                                moveUpLocations
                                    .firstOrNull { t -> t?.y == it.y - 1 && t.x == it.x } == null

                    it.x >= 0 && it.y >= 0 && isUpperTileIsEmpty
                }
                .map {
                    var hasTileOnLeft = false
                    var hasTileOnLeftTop = false
                    moveUpLocations.forEach { nextTile ->
                        if (nextTile?.x == it.x - 1) {
                            hasTileOnLeft = true
                        }
                        if (nextTile?.x == it.x - 1 && nextTile.y == it.y - 1) {
                            hasTileOnLeftTop = true
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

                    MoveUpEffectUiState(
                        initialOffset = Offset(leftX, startY),
                        destinationOffset = Offset(leftX, endY),
                        Size(rightX - leftX, height),
                        state.moveUpState.currentPiece.pieceColor
                    )
            }
            _viewState.update { it.copy(moveUpEffectUiState = moveUpEffectList) }
        }
    }

    fun collectGameSoundActions() = viewModelScope.launch {
        game.soundActions.collectLatest {
            when (it) {
                SoundPlayAction.CLEAN -> SoundUtil.play(SoundType.Clean)
                else -> {}
            }
        }
    }

    fun updateUi() {
        val list = game.getTilesAsList()
        _viewState.value = _viewState.value.copy(
            tiles = list,
            pieceFinalLocation = game.getPieceDestinationLocation()
        )
    }

    fun startGame() {
        start()
        startTimer()
    }

    fun rotate() {
        game.rotate()
    }

    fun moveLeft(repeatTime: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            repeat(repeatTime) {
                withContext(Dispatchers.Default) {
                    game.moveLeft()
                }
            }
        }
    }

    fun moveRight(repeatTime: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            repeat(repeatTime) {
                game.moveRight()
            }
        }
    }

    fun moveDown() {
        viewModelScope.launch {
            game.moveDown()
        }
    }

    fun moveUp() {
        viewModelScope.launch {
            game.moveUp()
        }
    }

    fun pauseGame() {
        game.pause()
    }

    fun continueGame() {
        viewModelScope.launch(Dispatchers.Default) {
            game.continueGame()

        }
    }
}