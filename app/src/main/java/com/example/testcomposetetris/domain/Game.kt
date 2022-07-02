package com.example.testcomposetetris.domain

import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.piece.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

const val ITERATION_DELAY = 1000L
class Game {
    private var isRunning = false
    var resetTimer = false
    var isWaiting = false

    private val tiles by lazy {
        Array(24) {
            Array(12) {
                false
            }
        }
    }

    private val gameScoreHelper = GameScoreHelper(tiles)

    var currentPiece: Piece = generatePiece()

    private var nextPiece: Piece = generatePiece()

    val updateUi: MutableStateFlow<GameState> = MutableStateFlow(GameState(
        getTilesAsList(),
        emptyList()))

    suspend fun startGame() {
        isRunning = true
        updateUi.value = updateUi.value.copy(previewLocation = getNextPiecePreviewLocation())
        while(isRunning) {
            if(!isWaiting) {
                move()
            }
            updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
            if(resetTimer) {
                resetTimer = false
            } else {
                delay(ITERATION_DELAY)
            }
        }
    }

    private fun generatePiece(): Piece {
        val pieceType = generateRandomNumber(
            min = 0,
            max = 6
        )
        return when(pieceType) {
            0 -> LPiece(tiles[0].size,tiles.size)
            1 -> ReverseLPiece(tiles[0].size,tiles.size)
            2 -> IPiece(tiles[0].size,tiles.size)
            3 -> ZPiece(tiles[0].size,tiles.size)
            4 -> ReverseZPiece(tiles[0].size,tiles.size)
            5 -> TPiece(tiles[0].size,tiles.size)
            else -> SquarePiece(tiles[0].size,tiles.size)
        }
    }
    private suspend fun removeCompletedLinesWithEffect(
    completedLines: List<Int>
    ) {
        repeat(4) {
            gameScoreHelper.removeCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
            delay(100)
            gameScoreHelper.fillCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
            delay(100)
        }
        delay(100)
        gameScoreHelper.removeCompletedLines(completedLines)
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
    }

    private fun generateNewPiece() {
        currentPiece = PieceFactory.generatePiece(nextPiece)
        nextPiece = generatePiece()
        updateUi.value = updateUi.value.copy(previewLocation = getNextPiecePreviewLocation())
    }

    private suspend fun move() {
        if(currentPiece.hitAnotherPiece(tiles)) {
            val completedLines = gameScoreHelper.getCompletedLines(currentPiece.location)
            if(completedLines.isEmpty()) {
                generateNewPiece()
            } else {
                isWaiting = true
                removeCompletedLinesWithEffect(completedLines)
                gameScoreHelper.moveLinesOneDown(completedLines)
                updateUi.value = updateUi.value.copy(tiles = getTilesAsList())

                isWaiting = false
                generateNewPiece()
            }
        }
        currentPiece.move()

        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    private fun removePreviousPieceLocation() {
        currentPiece.previousLocation.forEach {
            if(it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = false
        }
    }

    private fun updateTilesWithCurrentPieceLocation() {
        currentPiece.location.forEach {
            if(it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = true
        }
    }

    fun moveLeft() {
        if(isWaiting) return
        currentPiece.moveLeft(tiles)
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    fun moveRight() {
        if(isWaiting) return
        currentPiece.moveRight(tiles)
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    fun rotate() {
        if(isWaiting) return
        if(currentPiece.canRotate(tiles)) {
            currentPiece.rotate()
            removePreviousPieceLocation()
            updateTilesWithCurrentPieceLocation()
            updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        }
    }

    suspend fun moveDown() {
        if(isWaiting) return
        move()
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        resetTimer = true
    }

    fun getTilesAsList(): List<List<Boolean>> {
        val mutableList = mutableListOf<List<Boolean>>()
        tiles.forEach { mutableList.add(it.toList()) }
        return mutableList
    }

    fun getNextPiecePreviewLocation(): List<Position> {
        return  nextPiece.previewLocation.toList()
    }

}