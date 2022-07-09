package com.example.testcomposetetris.domain

import android.util.Log
import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.piece.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Long.max
import java.lang.Long.min
import kotlin.math.pow

const val ITERATION_DELAY = 1000L
const val MOVE_UP_ITERATION_DELAY = 5L

const val ITERATION_LEVEL_MULTIPLY =100L
const val SCORE_MULTIPLIER = 100
class Game {
    private var isRunning = false
    var resetTimer = false
    var isWaiting = false
    private var moveUpPressed = false

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

    private var level: Int = 1

    val updateUi: MutableStateFlow<GameState> = MutableStateFlow(GameState(
        getTilesAsList(),
        emptyList(),
        "Score 0",
        "Level 1",
        false
    ))

    private var score: Int = 0

    private val scoreForEachCompletedLine = 100

    private val scoreStrikeMultiplier = 0.2

    fun isGameOver(): Boolean = !isRunning

    fun getScore(): Int = score

    fun getLevel(): Int = level

    suspend fun startGame() {
        isRunning = true
        updateUi.value = updateUi.value.copy(previewLocation = getNextPiecePreviewLocation())
        while(isRunning) {
            if(!isWaiting) {
                move()
            }
            updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
            when {
                resetTimer -> resetTimer = false
                moveUpPressed -> delay(MOVE_UP_ITERATION_DELAY)
                else -> delay(calculateDelayMillis())
            }
        }
    }

    private fun calculateDelayMillis()
    : Long = max(
            ITERATION_DELAY - ITERATION_LEVEL_MULTIPLY  * 1.25.pow(level).toLong(),
            50L
        )


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
        Log.i("Game","New Piece Generated")
        Log.i("Game","Is Game Over : ${!isRunning}")
        if(moveUpPressed) { moveUpPressed = false }

        updateUi.value = updateUi.value.copy(
            previewLocation = getNextPiecePreviewLocation(),
            isGameOver = !isRunning
        )
    }

    private fun checkGameOver(): Boolean {
        currentPiece.location.forEach {
            if(it.y < 0) return true
        }
        return false
    }

    private suspend fun move() {
        if(currentPiece.hitAnotherPiece(tiles)) {
            isRunning = !checkGameOver()
            val completedLines = gameScoreHelper.getCompletedLines(currentPiece.location)
            calculateScore(completedLines.size)
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

    private fun calculateScore(completedLineNumber: Int) {
        if(completedLineNumber == 0) return
        val totalEarnedScore = scoreForEachCompletedLine * (completedLineNumber * scoreStrikeMultiplier.pow(completedLineNumber))
        score += totalEarnedScore.toInt()
        level = score / SCORE_MULTIPLIER + 1

        updateUi.value = updateUi.value.copy(
            score = "Score: $score",
            difficultyLevel = "Level $level"
        )
    }

    suspend fun moveDown() {
        if(isWaiting) return
        move()
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        resetTimer = true
    }

    suspend fun moveUp() {
        moveUpPressed = true
        move()
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