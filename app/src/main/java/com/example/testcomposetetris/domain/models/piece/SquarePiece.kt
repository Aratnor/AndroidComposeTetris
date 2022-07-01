package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position

class SquarePiece(
    override val posXLimit: Int,
    override val posYLimit: Int
): Piece() {
    override val location: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )
    override val previousLocation: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0, -9),
        Position(0, -9)
    )

    private fun initializeFirstMovement() {
        var randomXPosition = generateRandomNumber()
        randomXPosition = when {
            randomXPosition == (posXLimit - 1) -> posXLimit - 2
            else -> randomXPosition
        }
        location[0] = Position(randomXPosition,-1)
        location[1] = Position(randomXPosition+1,-1)
        location[2] = Position(randomXPosition+1,0)
        location[3] = Position(randomXPosition,0)
    }

    private fun canMoveLeft(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        if(location[0].x == 0) return false
        val loc0 = location[0]
        val loc2 = location[2]

        val destinationX = loc0.x - 1
        if(loc0.y < 0 || loc2.y < 0) return false
        if(tiles[loc0.y][destinationX] || tiles[loc2.y][destinationX]) return false

        return true
    }

    private fun canMoveRight(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        if(location[1].x + 1 >= posXLimit) return false
        val loc1 = location[1]
        val loc3 = location[3]

        val destinationX = loc1.x + 1
        if(loc1.y < 0 || loc3.y < 0) return false
        if(tiles[loc1.y][destinationX] || tiles[loc3.y][destinationX]) return false

        return true
    }

    override fun move() {
        if(location[0].y == -9) {
            initializeFirstMovement()
        } else {
            copyCurrentLocToPreviousLoc()
            moveDown(posYLimit)
        }
    }

    override fun moveLeft(
        tiles: Array<Array<Boolean>>
    ) {
        if(canMoveLeft(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.dec())
            }
        }
    }

    override fun moveRight(
        tiles: Array<Array<Boolean>>

    ) {
        if(canMoveRight(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.inc())
            }
        }
    }

    override fun rotate() { }

    override fun canRotate(tiles: Array<Array<Boolean>>): Boolean = false
}