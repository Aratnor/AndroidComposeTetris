package com.example.testcomposetetris

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcomposetetris.domain.Game
import com.example.testcomposetetris.domain.GameState
import com.example.testcomposetetris.domain.models.piece.IPiece
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.math.pow

class MainViewModel: ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState(
            emptyList(),
            emptyList(),
            "00:00",
            "",
            "Level 1",
            false
        ))

    val viewState : State<ViewState> = _viewState

    private val game = Game()



    fun start() {
        viewModelScope.launch {
            game.startGame()

        }
    }

    fun startTimer() {
        viewModelScope.launch {
            (0..Int.MAX_VALUE)
                .asSequence()
                .asFlow()
                .onEach { delay(1_000) }
                .collectLatest {
                    val minute = it / 60
                    val minuteAsString = if(minute < 10) {
                        "0$minute"
                    } else {
                        minute.toString()
                    }
                    val second = it % 60
                    val secondAsString = if(second < 10) {
                        "0$second"
                    } else {
                        second.toString()
                    }

                    _viewState.value = _viewState.value.copy(currentTime = "$minuteAsString:$secondAsString")

                }
        }
    }

    suspend fun collect() {
        game.updateUi.collectLatest {
            _viewState.value = _viewState.value.copy(
                tiles = it.tiles,
                nextPiecePreview = it.previewLocation,
                score = it.score,
                level = it.difficultyLevel,
                gameOver = it.isGameOver
            )
        }
    }

    private fun updateUi() {
        val list = game.getTilesAsList()
        _viewState.value = _viewState.value.copy(tiles = list)
    }

    fun rotate() {
        game.rotate()
    }

    fun moveLeft() {
        game.moveLeft()
        updateUi()
    }

    fun moveRight() {
        game.moveRight()
        updateUi()
    }

    fun moveDown() {
        viewModelScope.launch {
            game.moveDown()
        }
    }
}