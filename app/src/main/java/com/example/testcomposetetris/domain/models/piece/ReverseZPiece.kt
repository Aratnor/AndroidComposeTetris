package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position

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
            Position(0,1),
            Position(1, 1),
            Position(1, 0),
            Position(2, 0)
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
            if(location.firstOrNull { it.x == position.x + 1 && it.y == position.y } != null) return@forEach
            if(tiles[position.y][position.x + 1]) return false
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
        location[0] = Position(referencePos.x + 1,referencePos.y)
        location[2] = Position(referencePos.x,referencePos.y +  1)
        location[3] = Position(referencePos.x - 1 ,referencePos.y + 1)
    }

    private fun rotateTop() {
        val referencePos = location[1]
        location[0] = Position(referencePos.x,referencePos.y - 1)
        location[2] = Position(referencePos.x + 1,referencePos.y)
        location[3] = Position(referencePos.x + 1,referencePos.y + 1)
    }

    override fun canRotate(tiles: Array<Array<Boolean>>): Boolean {
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
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePosition = location[2]
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun canRotateLeft(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        val referencePosition = location[2]
        val minX = referencePosition.x - 1
        if(minX < 0) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    enum class Rotation {
        LEFT,
        TOP
    }
}