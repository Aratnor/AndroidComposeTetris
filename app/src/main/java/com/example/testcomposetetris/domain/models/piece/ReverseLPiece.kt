package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position

class ReverseLPiece(
    override val posXLimit: Int,
    override val posYLimit: Int
): Piece() {

    private var currentRotation: Rotation = Rotation.RIGHT

    override val location: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0, -9),
        Position(0, -9),
        Position(0, -9)
    )
    override val previousLocation: Array<Position> =
        arrayOf(
            Position(0,-9),
            Position(0, -9),
            Position(0, -9),
            Position(0, -9)
    )
    override val previewLocation: Array<Position> =
        arrayOf(
            Position(0,0),
            Position(0, 1),
            Position(1, 1),
            Position(2, 1)
        )

    override fun move() {
        if(location[0].y == -9) {
            initializeRotationRight()
        } else {
            copyCurrentLocToPreviousLoc()
            moveDown(posYLimit)
        }
    }

    private fun initializeRotationRight() {
        var referenceXPos = generateRandomNumber()
        referenceXPos = when(referenceXPos) {
            0 -> 1
            posXLimit - 1 -> posXLimit - 2
            else -> referenceXPos
        }
        location[0] = Position(referenceXPos + 1,0)
        location[1] = Position(referenceXPos + 1, -1)
        location[2] = Position(referenceXPos,-1)
        location[3] = Position(referenceXPos -1,-1)
    }

    override fun moveLeft(tiles: Array<Array<Boolean>>) {
        if(canMoveLeft(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.dec())
            }
        }
    }

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
        }
    }

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
            Rotation.RIGHT -> {
                rotateBottom()
                Rotation.BOTTOM
            }
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
        }
    }

    private fun rotateRight() {
        val referencePoint = location[2]
        location[0] = Position(referencePoint.x + 1,referencePoint.y + 1)
        location[1] = Position(referencePoint.x + 1,referencePoint.y )
        location[3] = Position(referencePoint.x - 1,referencePoint.y)
    }

    private fun rotateTop() {
        val referenceLocation = location[2]
        location[0] = Position(referenceLocation.x + 1,referenceLocation.y - 1)
        location[1] = Position(referenceLocation.x, referenceLocation.y - 1)
        location[3] = Position(referenceLocation.x , referenceLocation.y + 1)
    }

    private fun rotateBottom() {
        val referencePosition = location[2]
        location[0] = Position(referencePosition.x - 1,referencePosition.y + 1)
        location[1] = Position(referencePosition.x, referencePosition.y + 1)
        location[3] = Position(referencePosition.x,referencePosition.y - 1)
    }

    private fun rotateLeft() {
        val referencePoint = location[2]
        location[0] = Position(referencePoint.x - 1,referencePoint.y - 1)
        location[1] = Position(referencePoint.x - 1, referencePoint.y)
        location[3] = Position(referencePoint.x + 1, referencePoint.y)
    }

    override fun canRotate(tiles: Array<Array<Boolean>>): Boolean {
        return when(currentRotation) {
            Rotation.RIGHT -> {
                canRotateBottom(tiles)
            }
            Rotation.BOTTOM -> {
                canRotateLeft(tiles)
            }
            Rotation.LEFT -> {
                canRotateTop(tiles)
            }
            Rotation.TOP -> {
                canRotateRight(tiles)
            }
        }
    }

    private fun canRotateRight(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePosition = location[2]
        val minX = referencePosition.x - 1
        if(minX < 0) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun  canRotateTop(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePoint = location[2]
        val maxY = referencePoint.y + 1
        if(maxY >= posYLimit) return false
        return canMoveToNextTiles(referencePoint,tiles)
    }

    private fun canRotateBottom(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePoint = location[2]
        return canMoveToNextTiles(referencePoint,tiles)
    }

    private fun canRotateLeft(
        tiles: Array<Array<Boolean>>

    ): Boolean {
        val referencePosition = location[2]
        val maxX = referencePosition.x + 1
        if(maxX >= posXLimit) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }
    enum class Rotation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }
}