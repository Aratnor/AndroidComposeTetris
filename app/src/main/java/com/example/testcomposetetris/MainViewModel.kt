package com.example.testcomposetetris

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcomposetetris.domain.Game
import com.example.testcomposetetris.ext.convertToMinute
import com.example.testcomposetetris.ext.convertToRemainingSecond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState(
            List(24) {
                   List(12) {
                       false
                   }
            },
            emptyList(),
            emptyList(),
            "00:00",
            "",
            "Level 1",
            false,
            "",
            ""
        ))

    val viewState : State<ViewState> = _viewState

    private val game = Game()

    lateinit var timerJob: Job

    var rectangleWidth = -1F

    var muteButtonOffset = Offset(0F,0F)
    var muteButtonSize = Size(0F,0F)

    var isMuted = false

    private fun start() {
        viewModelScope.launch {
            game.startGame()

        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            val flow = (0..Int.MAX_VALUE)
                .asSequence()
                .asFlow()
                .onEach { delay(1_000) }

            flow.collectLatest { second ->
                val minuteAsString = second.convertToMinute()
                val secondAsString = second.convertToRemainingSecond()
                if(game.isGameOver()) {
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
                gameOver = it.isGameOver
            )
        }
    }

    private fun updateUi() {
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
                game.moveLeft()
                updateUi()
                delay(50)
            }
        }
    }

    fun moveRight(repeatTime: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            repeat(repeatTime) {
                game.moveRight()
                updateUi()
                delay(50)
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
}