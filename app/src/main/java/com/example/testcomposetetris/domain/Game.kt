package com.example.testcomposetetris.domain

import android.util.Log
import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.piece.*
import com.example.testcomposetetris.util.SoundType
import com.example.testcomposetetris.util.SoundUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow

const val ITERATION_DELAY = 800L
const val MOVE_UP_ITERATION_DELAY = 5L
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

    private var completedLines: Int = 0

    val updateUi: MutableStateFlow<GameState> = MutableStateFlow(GameState(
        getTilesAsList(),
        emptyList(),
        emptyList(),
        "0",
        "1",
        false
    ))

    private var score: Int = 0

    private var lastMovedUpTime = -1L

    fun isGameOver(): Boolean = !isRunning

    fun getScore(): Int = score

    fun getLevel(): Int = level

    suspend fun startGame() {
        isRunning = true
        updateUi.value = updateUi.value.copy(
            previewLocation = getNextPiecePreviewLocation(),
            pieceDestinationLocation = getPieceDestinationLocation()
        )
        while(isRunning) {
            if(!isWaiting) {
                move()
            }
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = getPieceDestinationLocation()
            )
            when {
                resetTimer -> resetTimer = false
                moveUpPressed -> delay(MOVE_UP_ITERATION_DELAY)
                else -> delay(calculateDelayMillis())
            }
        }
    }

    private fun calculateDelayMillis()
    : Long = ITERATION_DELAY - ((level * 0.7).pow((level - 1) * 0.884.pow(level)) + (level - 1) * 30).toLong()


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
        SoundUtil.play(false,SoundType.Clean)
        repeat(4) {
            gameScoreHelper.removeCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = emptyList()
            )
            delay(100)
            gameScoreHelper.fillCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = emptyList()
            )
            delay(100)
        }
        delay(100)
        gameScoreHelper.removeCompletedLines(completedLines)
        updateUi.value = updateUi.value.copy(
            tiles = getTilesAsList(),
            pieceDestinationLocation = emptyList()
        )
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
            if(!isRunning) {
                updateUi.value = updateUi.value.copy(
                    isGameOver = !isRunning,
                    score = score.toString(),
                    difficultyLevel = level.toString()
                )
            }
            if(completedLines.isEmpty()) {
                generateNewPiece()
            } else {
                isWaiting = true
                removeCompletedLinesWithEffect(completedLines)
                gameScoreHelper.moveLinesOneDown(completedLines)
                updateUi.value = updateUi.value.copy(tiles = getTilesAsList())

                generateNewPiece()
                isWaiting = false
            }
        }
        currentPiece.move()
        currentPiece.calculateDestinationLoc(tiles)


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
        currentPiece.isDestinationLocationChanged = true
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
        currentPiece.calculateDestinationLoc(tiles)
    }

    fun moveRight() {
        if(isWaiting) return
        currentPiece.moveRight(tiles)
        currentPiece.isDestinationLocationChanged = true

        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
        currentPiece.calculateDestinationLoc(tiles)
    }

    fun rotate() {
        if(isWaiting) return
        if(currentPiece.canRotate(tiles)) {
            currentPiece.rotate()
            removePreviousPieceLocation()
            updateTilesWithCurrentPieceLocation()
            currentPiece.isDestinationLocationChanged = true

            currentPiece.calculateDestinationLoc(tiles)
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = getPieceDestinationLocation()
            )
        }
    }

    private fun calculateScore(completedLineNumber: Int) {
        completedLines += completedLineNumber
        if(completedLineNumber == 0) return
        val earnedScore = when(completedLineNumber) {
            1 -> 40 * level
            2 -> 100 * level
            3 -> 300 * level
            else -> 1200 * level
        }
        if(completedLines >= 10) {
            level++
            completedLines -= 10
        }
        score += earnedScore

        updateUi.value = updateUi.value.copy(
            score = score.toString(),
            difficultyLevel = level.toString()
        )
    }

    suspend fun moveDown() {
        if(isWaiting) return
        move()
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        resetTimer = true
    }

    suspend fun moveUp() {
        if(canMoveUp()) {
            moveUpPressed = true
            move()
            lastMovedUpTime = System.currentTimeMillis()
        }
    }

    private fun canMoveUp(): Boolean {
        if(lastMovedUpTime == -1L) return true

        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - lastMovedUpTime
        return timeDiff > 1000
    }

    fun getTilesAsList(): List<List<Boolean>> {
        val mutableList = mutableListOf<List<Boolean>>()
        tiles.forEach { mutableList.add(it.toList()) }
        return mutableList
    }

    private fun getNextPiecePreviewLocation(): List<Position> {
        return  nextPiece.previewLocation.toList()
    }

    fun getPieceDestinationLocation(): List<Position> {
        return currentPiece.destinationLocation.toList()
    }
}