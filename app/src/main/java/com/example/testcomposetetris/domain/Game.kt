package com.example.testcomposetetris.domain

import android.util.Log
import com.example.testcomposetetris.MoveUpState
import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.Tile
import com.example.testcomposetetris.domain.models.TileColor
import com.example.testcomposetetris.domain.models.piece.*
import com.example.testcomposetetris.util.SoundType
import com.example.testcomposetetris.util.SoundUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

const val ITERATION_DELAY = 800L
const val MOVE_UP_ITERATION_DELAY = 30L
class Game {
    private var isRunning = false
    var resetTimer = false
    var isWaiting = false
    var isGamePaused = false
    private var moveUpPressed = false

    var currentMoveUpIterationDelay = MOVE_UP_ITERATION_DELAY

    private val tiles by lazy {
        Array(24) {
            Array(12) {
                Tile(isOccupied = false, hasActivePiece = false, color = TileColor.EMPTY)
            }
        }
    }

    private val gameScoreHelper = GameScoreHelper(tiles)

    var currentPiece: Piece = generatePiece()

    private var nextPiece: Piece = generatePiece()

    private var level: Int = 1

    private var completedLines: Int = 0

    private val moveUpInitialLocations: MutableList<Position> = mutableListOf()

    var moveUpMovementCount = 0

    var moveUpVelocity = 1.0

    val updateUi: MutableStateFlow<GameState> = MutableStateFlow(GameState(
        getTilesAsList(),
        emptyList<Position>() to TileColor.EMPTY,
        emptyList(),
        MoveUpState(false, emptyList(),-1,currentPiece),
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
            previewLocation = getNextPiecePreviewLocation() to nextPiece.pieceColor,
            pieceDestinationLocation = getPieceDestinationLocation()
        )
        while(isRunning && !isGamePaused) {
            if(!isWaiting) {
                move()
            }
            if(moveUpPressed) {
                moveUpMovementCount++
            }
            moveUpInitialLocations.clear()
            currentPiece.location.forEach { moveUpInitialLocations.add(it) }
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = getPieceDestinationLocation(),
                moveUpState = MoveUpState(
                    moveUpPressed,
                    moveUpInitialLocations,
                    moveUpMovementCount,
                    currentPiece
                )
            )
            when {
                resetTimer -> resetTimer = false
                moveUpPressed -> {
                    delay(currentMoveUpIterationDelay)
                    currentMoveUpIterationDelay = (MOVE_UP_ITERATION_DELAY / (moveUpVelocity * 2 / 3)).roundToLong()
                    moveUpVelocity += 1.4
                }
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
        SoundUtil.play(SoundType.Clean)
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
            previewLocation = getNextPiecePreviewLocation() to nextPiece.pieceColor,
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
            removeActivePieceFromTiles()
            isRunning = !checkGameOver()
            isWaiting = true && isRunning
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
                removeCompletedLinesWithEffect(completedLines)
                gameScoreHelper.moveLinesOneDown(completedLines)
                updateUiWithTiles()

                generateNewPiece()
            }
        }
        currentPiece.move()
        currentPiece.calculateDestinationLoc(tiles)
        isWaiting = false

        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
    }

    private fun updateUiWithTiles() {
        updateUi.value = updateUi.value.copy(
            tiles = getTilesAsList(),
            pieceDestinationLocation = getPieceDestinationLocation()
        )

    }


    private fun removePreviousPieceLocation() {
        currentPiece.previousLocation.forEach {
            if(it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = Tile(
                isOccupied = false,
                hasActivePiece = false,
                color = TileColor.EMPTY
            )
        }
    }

    private fun removeActivePieceFromTiles() {
        currentPiece.location.forEach {
            if(it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] =  tiles[it.y][it.x].copy(hasActivePiece = false)
        }
    }

    private fun updateTilesWithCurrentPieceLocation() {
        currentPiece.location.forEach {
            if(it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = Tile(
                isOccupied = true,
                hasActivePiece = true,
                color = currentPiece.pieceColor
            )
        }
    }

    fun moveLeft() {
        if(isWaiting || moveUpPressed || isGamePaused) return
        currentPiece.moveLeft(tiles)
        currentPiece.isDestinationLocationChanged = true
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
        currentPiece.calculateDestinationLoc(tiles)
        updateUiWithTiles()
    }

    fun moveRight() {
        if(isWaiting || moveUpPressed || isGamePaused) return
        currentPiece.moveRight(tiles)
        currentPiece.isDestinationLocationChanged = true
        removePreviousPieceLocation()
        updateTilesWithCurrentPieceLocation()
        currentPiece.calculateDestinationLoc(tiles)
        updateUiWithTiles()
    }

    fun rotate() {
        if(isWaiting || moveUpPressed || isGamePaused) return
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
        if(isWaiting ||  isGamePaused) return
        move()
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        resetTimer = true
    }

    suspend fun moveUp() {
        if(canMoveUp() && !isGamePaused) {
            moveUpPressed = true
            currentMoveUpIterationDelay = MOVE_UP_ITERATION_DELAY
            moveUpInitialLocations.clear()
            moveUpMovementCount = 0
            moveUpVelocity = 1.0
            move()
            lastMovedUpTime = System.currentTimeMillis()
        }
    }

    private fun canMoveUp(): Boolean {
        if(lastMovedUpTime == -1L) return true

        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - lastMovedUpTime
        return timeDiff > 500
    }

    fun getTilesAsList(): List<List<Tile>> {
        val mutableList = mutableListOf<List<Tile>>()
        tiles.forEach { mutableList.add(it.toList()) }
        return mutableList
    }

    private fun getNextPiecePreviewLocation(): List<Position> {
        return  nextPiece.previewLocation.toList()
    }

    fun getPieceDestinationLocation(): List<Position> {
        return currentPiece.destinationLocation.toList()
    }

    fun pause() {
        isGamePaused = true
    }

    suspend fun continueGame() {
        isGamePaused = false
        startGame()
    }
}