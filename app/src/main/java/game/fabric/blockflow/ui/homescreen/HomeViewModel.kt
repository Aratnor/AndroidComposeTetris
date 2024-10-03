package game.fabric.blockflow.ui.homescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import game.fabric.blockflow.gamelogic.domain.HomeAnimationState
import game.fabric.blockflow.gamelogic.domain.generateRandomNumber
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor
import game.fabric.blockflow.gamelogic.domain.models.piece.IPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.LPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.Piece
import game.fabric.blockflow.gamelogic.domain.models.piece.ReverseLPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.ReverseZPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.SquarePiece
import game.fabric.blockflow.gamelogic.domain.models.piece.TPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.ZPiece
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val _backgroundAnimationState: MutableState<HomeAnimationState> = mutableStateOf(
        HomeAnimationState(listOf())
    )

    val viewState : State<HomeAnimationState> = _backgroundAnimationState


    private val limX = 12
    private val limY = 20
    private var currentPiece = generatePiece()

    private val tiles = Array(limY) {
        Array(limX) {
            Tile(isOccupied = false, hasActivePiece = false, TileColor.EMPTY)
        }
    }

    private var isStarted = false



    fun startBackgroundAnimation() {
        if(isStarted) return
        isStarted = true
        viewModelScope.launch(Dispatchers.Default) {
            while(true) {
                if(currentPiece.location[0].y == -9) {
                    currentPiece.move()
                } else {
                    clearPreviousLocations()
                    currentPiece.location.forEachIndexed { index, position ->
                        currentPiece.location[index] = position.copy(y = position.y.inc())
                    }
                }
                updateUi()
                delay(40)
                var maxYCount = 0
                currentPiece.location.forEach {
                    if(it.y >= limY) maxYCount++
                }
                if(maxYCount == currentPiece.location.size) {
                    repeat(tiles.size) { y ->
                        repeat(tiles[y].size) { x ->
                            tiles[y][x] = tiles[y][x].copy(isOccupied = false)
                        }
                    }

                    currentPiece = generatePiece()
                }
            }
        }
    }

    private fun clearPreviousLocations() {
        currentPiece.location.forEach {
            if(it.x < 0 || it.y < 0 ||it.x >= limX || it.y >= limY) return@forEach
            tiles[it.y][it.x] = tiles[it.y][it.x].copy(isOccupied = false)
        }
    }


    private fun updateUi() {
        clearPreviousLocations()
        currentPiece.location.forEach {
            if(it.x < 0 || it.y < 0 ||it.x >= limX || it.y >= limY) return@forEach
            tiles[it.y][it.x] = tiles[it.y][it.x].copy(isOccupied = true,color = currentPiece.pieceColor)
        }

        val tileList = mutableListOf<List<Tile>>()
        tiles.forEach { tileList.add(it.toList()) }

        _backgroundAnimationState.value = HomeAnimationState(tileList)
    }

    private fun generatePiece(): Piece {
        val pieceType = generateRandomNumber(
            min = 0,
            max = 6
        )
        return when(pieceType) {
            0 -> LPiece(limX,limY)
            1 -> ReverseLPiece(limX,limY)
            2 -> IPiece(limX,limY)
            3 -> ZPiece(limX, limY)
            4 -> ReverseZPiece(limX,limY)
            5 -> TPiece(limX,limY)
            else -> SquarePiece(limX,limY)
        }
    }
}