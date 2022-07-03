package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position

class TPiece(
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
    override val previewLocation: Array<Position> = arrayOf(
        Position(0,1),
        Position(1,1),
        Position(2,1),
        Position(1,0)
    )

    var currentRotation: Rotation = Rotation.BOTTOM

    override fun move() {
        if(location[0].y == -9) {
            initialize()
        } else {
            copyCurrentLocToPreviousLoc()
            moveDown(posYLimit)
        }
    }

    private fun initialize() {
        var referenceXPosition = generateRandomNumber()
        referenceXPosition = when(referenceXPosition) {
            0 -> 1
            posXLimit - 1 -> posXLimit - 2
            else -> referenceXPosition
        }
        location[0] = Position(referenceXPosition - 1, -1)
        location[1] = Position(referenceXPosition, -1)
        location[2] = Position(referenceXPosition + 1, -1)
        location[3] = Position(referenceXPosition, 0)
    }

    override fun moveLeft(tiles: Array<Array<Boolean>>) {
        if(canMoveLeft(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.dec())
            }
        }    }

    private fun canMoveLeft(
        tiles: Array<Array<Boolean>>
    )
            : Boolean {
        location.forEach { position ->
            if(position.x - 1 < 0) return false
            if(position.y < 0) return@forEach
            if(location.firstOrNull { it.x == position.x - 1 && it.y == position.y } != null) return@forEach
            if(tiles[position.y][position.x - 1]) return false
        }
        return true
    }

    override fun moveRight(tiles: Array<Array<Boolean>>) {
        if(canMoveRight(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.inc())
            }
        }    }

    private fun canMoveRight(tiles: Array<Array<Boolean>>): Boolean {
        location.forEach { position ->
            if(position.x + 1 >= posXLimit) return false
            if(position.y < 0) return@forEach
            if(location.firstOrNull { it.x == position.x + 1 && it.y == position.y } != null) return@forEach
            if(tiles[position.y][position.x + 1]) return false
        }
        return true
    }

    override fun rotate() {
        copyCurrentLocToPreviousLoc()
        currentRotation = when(currentRotation) {
            Rotation.BOTTOM -> {
                rotateLeft()
                Rotation.LEFT
            }
            Rotation.LEFT -> {
                rotateTop()
                Rotation.TOP
            }
            Rotation.TOP -> {
                rotateRight()
                Rotation.RIGHT
            }
            Rotation.RIGHT -> {
                rotateBottom()
                Rotation.BOTTOM
            }
        }
    }

    private fun rotateLeft() {
        val referencePosition = location[1]
        location[0] = Position(referencePosition.x,referencePosition.y - 1)
        location[2] = Position(referencePosition.x,referencePosition.y + 1)
        location[3] = Position(referencePosition.x -1,referencePosition.y)
    }

    private fun rotateTop() {
        val referencePosition = location[1]
        location[0] = Position(referencePosition.x - 1,referencePosition.y)
        location[2] = Position(referencePosition.x + 1,referencePosition.y)
        location[3] = Position(referencePosition.x,referencePosition.y - 1)
    }

    private fun rotateRight() {
        val referencePosition = location[1]
        location[0] = Position(referencePosition.x,referencePosition.y - 1)
        location[2] = Position(referencePosition.x, referencePosition.y + 1)
        location[3] = Position(referencePosition.x + 1,referencePosition.y)
    }

    private fun rotateBottom() {
        val referencePosition = location[1]
        location[0] = Position(referencePosition.x - 1,referencePosition.y)
        location[2] = Position(referencePosition.x + 1,referencePosition.y)
        location[3] = Position(referencePosition.x,referencePosition.y + 1)
    }

    override fun canRotate(tiles: Array<Array<Boolean>>): Boolean {
        return when(currentRotation) {
            Rotation.BOTTOM -> {
                canRotateLeft(tiles)
            }
            Rotation.LEFT -> {
                canRotateTop(tiles)
            }
            Rotation.TOP -> {
                canRotateRight(tiles)
            }
            Rotation.RIGHT -> {
                canRotateBottom(tiles)
            }
        }
    }

    private fun canRotateLeft(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePos = location[1]
        return canMoveToNextTiles(referencePos,tiles)
    }

    private fun canRotateTop(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePos = location[1]
        val maxX = referencePos.x + 1
        if(maxX >= posXLimit) return false
        return canMoveToNextTiles(referencePos,tiles)
    }

    private fun canRotateRight(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePos = location[1]
        val maxY = referencePos.y + 1
        if(maxY >= posYLimit) return false
        return canMoveToNextTiles(referencePos,tiles)
    }

    private fun canRotateBottom(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePos = location[1]
        val minX = referencePos.x - 1
        if(minX < 0) return false
        return canMoveToNextTiles(referencePos,tiles)
    }

    enum class Rotation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }
}