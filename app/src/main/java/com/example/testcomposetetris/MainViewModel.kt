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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState(
            emptyList(),
            emptyList()
        ))
    val viewState : State<ViewState> = _viewState

    private val game = Game()



    fun start() {
        viewModelScope.launch {
            game.startGame()

        }
    }

    suspend fun collect() {
        game.updateUi.collectLatest {
            _viewState.value = _viewState.value.copy(
                tiles = it.tiles,
                nextPiecePreview = it.previewLocation
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