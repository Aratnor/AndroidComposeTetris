package game.fabric.blockflow.gamelogic.domain

import android.util.Log
import game.fabric.blockflow.gamelogic.GameConfig
import game.fabric.blockflow.gamelogic.MoveUpState
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile
import game.fabric.blockflow.gamelogic.domain.models.TileColor
import game.fabric.blockflow.gamelogic.domain.models.piece.IPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.LPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.Piece
import game.fabric.blockflow.gamelogic.domain.models.piece.PieceFactory
import game.fabric.blockflow.gamelogic.domain.models.piece.ReverseLPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.ReverseZPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.SquarePiece
import game.fabric.blockflow.gamelogic.domain.models.piece.TPiece
import game.fabric.blockflow.gamelogic.domain.models.piece.ZPiece
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlin.math.pow
import kotlin.math.roundToLong

const val ITERATION_DELAY = 800L
const val MOVE_UP_ITERATION_DELAY = 15L

class Game {
    private var isRunning = false
    var resetTimer = false
    var isWaiting = false
    var isGamePaused = false
    private var moveUpPressed = false

    var currentMoveUpIterationDelay = MOVE_UP_ITERATION_DELAY

    private val tiles by lazy {
        Array(GameConfig.ROW_SIZE) {
            Array(GameConfig.COLUMN_SIZE) {
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

    val updateUi: MutableStateFlow<GameState> = MutableStateFlow(
        GameState(
            getTilesAsList(),
            emptyList<Position>() to TileColor.EMPTY,
            emptyList(),
            MoveUpState(false, emptyList(), -1, currentPiece),
            "0",
            "1",
            false
        )
    )

    val soundActions = MutableStateFlow(SoundPlayAction.IDLE)

    private var verticalMoveJob: Deferred<Unit>? = null
    private var mainGameLoopJob: Deferred<Unit>? = null
    private var moveUpJob: Deferred<Unit>? = null


    private var score: Int = 0

    fun isGameOver(): Boolean = !isRunning

    fun getScore(): Int = score

    fun getLevel(): Int = level

    suspend fun startGame() {
        isRunning = true
        updateUi.value = updateUi.value.copy(
            previewLocation = getNextPiecePreviewLocation() to nextPiece.pieceColor,
            pieceDestinationLocation = getPieceDestinationLocation()
        )
        while (isRunning && !isGamePaused) {
            runBlocking {
                moveUpJob?.join()
                verticalMoveJob?.join()
                mainGameLoopJob = async { gameLoopExecution() }
                mainGameLoopJob?.await()
            }
        }
    }

    private suspend fun gameLoopExecution() {
        if (!isWaiting) {
            move()
        }
        if (moveUpPressed) {
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
                currentMoveUpIterationDelay =
                    (MOVE_UP_ITERATION_DELAY / (moveUpVelocity * 2 / 3)).roundToLong()
                moveUpVelocity += 1.4
            }

            else -> delay(calculateDelayMillis())
        }

    }

    private fun calculateDelayMillis()
            : Long =
        ITERATION_DELAY - ((level * 0.7).pow((level - 1) * 0.884.pow(level)) + (level - 1) * 30).toLong()

    private fun generatePiece(): Piece {
        val pieceType = generateRandomNumber(
            min = 0,
            max = 6
        )
        return when (pieceType) {
            0 -> LPiece(tiles[0].size, tiles.size)
            1 -> ReverseLPiece(tiles[0].size, tiles.size)
            2 -> IPiece(tiles[0].size, tiles.size)
            3 -> ZPiece(tiles[0].size, tiles.size)
            4 -> ReverseZPiece(tiles[0].size, tiles.size)
            5 -> TPiece(tiles[0].size, tiles.size)
            else -> SquarePiece(tiles[0].size, tiles.size)
        }
    }

    private suspend fun removeCompletedLinesWithEffect(
        completedLines: List<Int>
    ) {
        updateUi.value = updateUi.value.copy(
            moveUpState = updateUi.value.moveUpState.copy(
                moveUpMovementCount = 0
            )
        )
        soundActions.emit(SoundPlayAction.CLEAN)
        repeat(2) {
            gameScoreHelper.removeCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = emptyList()
            )
            delay(50)
            gameScoreHelper.fillCompletedLines(completedLines)
            updateUi.value = updateUi.value.copy(
                tiles = getTilesAsList(),
                pieceDestinationLocation = emptyList()
            )
            delay(50)
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
        Log.i("Game", "New Piece Generated")
        Log.i("Game", "Is Game Over : ${!isRunning}")
        if (moveUpPressed) {
            moveUpPressed = false
        }

        updateUi.value = updateUi.value.copy(
            previewLocation = getNextPiecePreviewLocation() to nextPiece.pieceColor,
            isGameOver = !isRunning
        )
    }

    private fun checkGameOver(): Boolean {
        currentPiece.location.forEach {
            if (it.y < 0) return true
        }
        return false
    }

    private suspend fun move() {
        if (currentPiece.hitAnotherPiece(tiles)) {
            isWaiting = true && isRunning
            removeActivePieceFromTiles()
            isRunning = !checkGameOver()
            val completedLines = gameScoreHelper.getCompletedLines(currentPiece.location)
            calculateScore(completedLines.size)
            if (!isRunning) {
                updateUi.value = updateUi.value.copy(
                    isGameOver = !isRunning,
                    score = score.toString(),
                    difficultyLevel = level.toString()
                )
            }
            if (completedLines.isEmpty()) {
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
            if (it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = Tile(
                isOccupied = false,
                hasActivePiece = false,
                color = TileColor.EMPTY
            )
        }
    }

    private fun removeActivePieceFromTiles() {
        currentPiece.previousLocation.forEach {
            if (it.x < 0 || it.y < 0) return@forEach
            if (
                currentPiece
                    .location
                    .firstOrNull { currentPos ->
                        it.x == currentPos.x && it.y == currentPos.y
                    } != null
            ) return@forEach
            tiles[it.y][it.x] = tiles[it.y][it.x].copy(hasActivePiece = false, isOccupied = false)
        }
    }

    private fun updateTilesWithCurrentPieceLocation() {
        currentPiece.location.forEach {
            if (it.x < 0 || it.y < 0) return@forEach
            tiles[it.y][it.x] = Tile(
                isOccupied = true,
                hasActivePiece = true,
                color = currentPiece.pieceColor
            )
        }
    }

    suspend fun moveLeft() {
        if (verticalMoveJob?.isActive == true) return
        verticalMoveJob = runBlocking {
            async {
                if (isWaiting || moveUpPressed || isGamePaused) return@async
                currentPiece.moveLeft(tiles)
                currentPiece.isDestinationLocationChanged = true
                removePreviousPieceLocation()
                updateTilesWithCurrentPieceLocation()
                currentPiece.calculateDestinationLoc(tiles)
                updateUiWithTiles()
            }
        }
        verticalMoveJob?.await()
    }

    suspend fun moveRight() {
        if (verticalMoveJob?.isActive == true) return
        verticalMoveJob = runBlocking {
            async {
                if (isWaiting || moveUpPressed || isGamePaused) return@async
                currentPiece.moveRight(tiles)
                currentPiece.isDestinationLocationChanged = true
                removePreviousPieceLocation()
                updateTilesWithCurrentPieceLocation()
                currentPiece.calculateDestinationLoc(tiles)
                updateUiWithTiles()
            }
        }
        verticalMoveJob?.await()
    }

    fun rotate() {
        if (isWaiting || moveUpPressed || isGamePaused) return
        if (currentPiece.canRotate(tiles)) {
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
        if (completedLineNumber == 0) return
        val earnedScore = when (completedLineNumber) {
            1 -> 40 * level
            2 -> 100 * level
            3 -> 300 * level
            else -> 1200 * level
        }
        if (completedLines >= 10) {
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
        if (isWaiting || isGamePaused) return
        move()
        updateUi.value = updateUi.value.copy(tiles = getTilesAsList())
        resetTimer = true
    }

    suspend fun moveUp() = runBlocking {
        mainGameLoopJob?.join()
        moveUpJob = async {
            if (verticalMoveJob?.isActive == true) {
                verticalMoveJob?.join()
                onMoveUp()
            } else {
                onMoveUp()
            }
        }
    }

    private fun onMoveUp() {
        if (!isGamePaused) {
            moveUpPressed = true
            currentMoveUpIterationDelay = MOVE_UP_ITERATION_DELAY
            moveUpInitialLocations.clear()
            moveUpMovementCount = 0
            moveUpVelocity = 1.0
        }
    }

    fun getTilesAsList(): List<List<Tile>> {
        val mutableList = mutableListOf<List<Tile>>()
        tiles.forEach { mutableList.add(it.toList()) }
        return mutableList
    }

    private fun getNextPiecePreviewLocation(): List<Position> {
        return nextPiece.previewLocation.toList()
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