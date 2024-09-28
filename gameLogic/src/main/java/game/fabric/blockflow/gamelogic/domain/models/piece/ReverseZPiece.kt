package game.fabric.blockflow.gamelogic.domain.models.piece

import game.fabric.blockflow.gamelogic.domain.generateRandomNumber
import game.fabric.blockflow.gamelogic.domain.models.Position
import game.fabric.blockflow.gamelogic.domain.models.Tile

class ReverseZPiece(
    override val posXLimit: Int,
    override val posYLimit: Int
) : Piece() {

    override val location: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )
    override val previousLocation: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )

    override val previewLocation: Array<Position> =
        arrayOf(
            Position(0,2),
            Position(1, 2),
            Position(1, 1),
            Position(2, 1)
        )

    override val destinationLocation: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )

    var currentRotation: Rotation = Rotation.LEFT

    override fun move() {
        if(location[0].y == -9) {
            initializeMovement()
        } else {
            copyCurrentLocToPreviousLoc()
            moveDown(posYLimit)
        }
    }

    private fun initializeMovement() {
        var referenceXPosition = generateRandomNumber()
        referenceXPosition = when(referenceXPosition) {
            0 -> 1
            posXLimit - 1 -> posXLimit - 2
            else -> referenceXPosition
        }
        location[0] = Position(referenceXPosition + 1, -1)
        location[1] = Position(referenceXPosition, -1)
        location[2] = Position(referenceXPosition,0)
        location[3] = Position(referenceXPosition - 1,0)
    }

    override fun moveLeft(tiles: Array<Array<Tile>>) {
        if(canMoveLeft(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.dec())
            }
        }
    }

    private fun canMoveLeft(
        tiles: Array<Array<Tile>>
    )
            : Boolean {
        location.forEach { position ->
            if(position.x - 1 < 0) return false
            if(position.y < 0) return@forEach
            if(location.firstOrNull { it.x == position.x - 1 && it.y == position.y } != null) return@forEach
            if(tiles[position.y][position.x - 1].isOccupied) return false
        }
        return true
    }

    override fun moveRight(tiles: Array<Array<Tile>>) {
        if(canMoveRight(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.inc())
            }
        }
    }

    private fun canMoveRight(tiles: Array<Array<Tile>>): Boolean {
        location.forEach { position ->
            if(position.x + 1 >= posXLimit) return false
            if(position.y < 0) return@forEach
            if(location.firstOrNull { it.x == position.x + 1 && it.y == position.y } != null) return@forEach
            if(tiles[position.y][position.x + 1].isOccupied) return false
        }
        return true
    }

    override fun rotate() {
        copyCurrentLocToPreviousLoc()
        currentRotation = when(currentRotation) {
            Rotation.LEFT -> {
                rotateTop()
                Rotation.TOP
            }
            Rotation.TOP -> {
                rotateLeft()
                Rotation.LEFT
            }
        }
    }

    private fun rotateLeft() {
        val referencePos = location[1]

        val xOffset = if(referencePos.x - 1 < 0) {
            1
        } else {
            0
        }
        location[0] = Position(xOffset + referencePos.x + 1,referencePos.y)
        location[1] = Position(xOffset + referencePos.x, referencePos.y)
        location[2] = Position(xOffset + referencePos.x,referencePos.y +  1)
        location[3] = Position(xOffset + referencePos.x - 1 ,referencePos.y + 1)
    }

    private fun rotateTop() {
        val referencePos = location[1]
        location[0] = Position(referencePos.x,referencePos.y - 1)
        location[2] = Position(referencePos.x + 1,referencePos.y)
        location[3] = Position(referencePos.x + 1,referencePos.y + 1)
    }

    override fun canRotate(tiles: Array<Array<Tile>>): Boolean {
        return when(currentRotation) {
            Rotation.TOP -> {
                canRotateLeft(tiles)
            }
            Rotation.LEFT -> {
                canRotateTop(tiles)
            }
        }
    }

    private fun canRotateTop(
        tiles: Array<Array<Tile>>
    ): Boolean {
        val referencePosition = location[1]
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun canRotateLeft(
        tiles: Array<Array<Tile>>
    ): Boolean {
        val referencePosition = location[1]
        return canMoveToNextTiles(referencePosition,tiles)
    }

    enum class Rotation {
        LEFT,
        TOP
    }
}