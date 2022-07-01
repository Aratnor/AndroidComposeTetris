package com.example.testcomposetetris.domain

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

    val updateUi: MutableStateFlow<List<List<Boolean>>> = MutableStateFlow(getTilesAsList())


    suspend fun startGame() {
        isRunning = true
        while(isRunning) {
            if(!isWaiting) {
                move()
            }
            updateUi.value = getTilesAsList()
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
            updateUi.value = getTilesAsList()
            delay(100)
            gameScoreHelper.fillCompletedLines(completedLines)
            updateUi.value = getTilesAsList()
            delay(100)
        }
        delay(100)
        gameScoreHelper.removeCompletedLines(completedLines)
        updateUi.value = getTilesAsList()
    }

    private suspend fun move() {
        if(currentPiece.hitAnotherPiece(tiles)) {
            val completedLines = gameScoreHelper.getCompletedLines(currentPiece.location)
            if(completedLines.isEmpty()) {
                currentPiece = generatePiece()
            } else {
                isWaiting = true
                removeCompletedLinesWithEffect(completedLines)
                gameScoreHelper.moveLinesOneDown(completedLines)
                updateUi.value = getTilesAsList()

                isWaiting = false
                currentPiece = generatePiece()
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
            updateUi.value = getTilesAsList()
        }
    }

    suspend fun moveDown() {
        if(isWaiting) return
        move()
        updateUi.value = getTilesAsList()
        resetTimer = true
    }

    fun getTilesAsList(): List<List<Boolean>> {
        val mutableList = mutableListOf<List<Boolean>>()
        tiles.forEach { mutableList.add(it.toList()) }
        return mutableList
    }

}