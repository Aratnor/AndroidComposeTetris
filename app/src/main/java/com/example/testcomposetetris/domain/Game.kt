package com.example.testcomposetetris.domain

import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.piece.IPiece
import com.example.testcomposetetris.domain.models.piece.Piece
import com.example.testcomposetetris.domain.models.piece.SquarePiece
import com.example.testcomposetetris.orZero
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

const val ITERATION_DELAY = 1000L
class Game {
    var isRunning = false
    var resetTimer = false
    var isWaiting = false
    private val tiles by lazy {
        Array(24) {
            Array(12) {
                false
            }
        }
    }

    var currentPiece: Piece = IPiece(tiles[0].size,tiles.size)

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

    private fun moveDownTiles(
        completedLineYPos: Int
    ) {
        run {
            if(completedLineYPos == 0) return@run
            for(posY in completedLineYPos - 1 downTo 0 step 1) {
                val containsTrue = moveOneDown(posY)
                if(!containsTrue) return@run
            }
        }
    }

    // return true if array contains element which is true otherwise false
    private fun moveOneDown(startPosY: Int): Boolean {
        if(startPosY == 0) return false
        var containsTrue = false
        tiles[startPosY].forEachIndexed { posX, isOccupied ->
            if(isOccupied) {
                containsTrue = true
                tiles[startPosY][posX] = false
                tiles[startPosY-1][posX] = true
            }
        }
        return containsTrue
    }

    /**
     * 2,3,4 ,8
     */
    private fun removeCompletedLinesAndMoveDownUpTiles(completedLines: List<Int>) {

        completedLines.sorted().forEach{ posY ->
            tiles[posY].forEachIndexed { index, _ ->
                tiles[posY][index] = false
            }
        }
        completedLines.sorted().forEach { posY ->
            moveDownTiles(posY)
        }
    }

    private fun getCompletedLines(
        locations: Array<Position>
    ): List<Int> {
        val iteratedYPoints = mutableListOf<Int>()
        val completedYPoints = mutableListOf<Int>()
        locations.forEach {
            if(iteratedYPoints.contains(it.y)) return@forEach

            var occupiedTileCount = 0
            tiles[it.y].forEach { isOccupied ->
                if(isOccupied) occupiedTileCount++
            }
            if(occupiedTileCount == tiles[it.y].size) completedYPoints.add(it.y)
        }
        return completedYPoints.distinct()
    }

    private fun generatePiece(): Piece {
        return if(currentPiece is SquarePiece) {
            IPiece(tiles[0].size,tiles.size)
        } else {
            SquarePiece(tiles[0].size,tiles.size)
        }
    }

    private fun move() {
        if(currentPiece.hitAnotherPiece(tiles)) {
            val completedLines = getCompletedLines(currentPiece.location)
            if(completedLines.isEmpty()) {
                currentPiece = generatePiece()
            } else {
                isWaiting = true
                removeCompletedLinesAndMoveDownUpTiles(completedLines)
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
        currentPiece.moveLeft(tiles)
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    fun moveRight() {
        currentPiece.moveRight(tiles)
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    fun rotate() {
        if(currentPiece.canRotate(tiles)) {
            currentPiece.rotate()
            removePreviousPieceLocation()
            updateTilesWithCurrentPieceLocation()
        }
    }

    fun moveDown() {
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