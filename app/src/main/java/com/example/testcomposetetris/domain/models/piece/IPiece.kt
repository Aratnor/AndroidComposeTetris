package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class IPiece(
    override val posXLimit: Int,
    override val posYLimit: Int
): Piece() {
    private var currentRotation: Rotation = Rotation.VERTICAL


    override val previousLocation: Array<Position> = arrayOf(
            Position(0,-9),
            Position(0,-9),
            Position(0,-9),
            Position(0,-9)
        )
    override val location: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )

    override val previewLocation: Array<Position> = arrayOf(
        Position(1,0),
        Position(1,1),
        Position(1,2),
        Position(1,3)
    )

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

    private fun canMoveLeft(
        tiles: Array<Array<Boolean>>
    )
    : Boolean {
        if(location[0].x <= 0) return false
        when(currentRotation) {
            Rotation.VERTICAL -> {
                location.forEach {
                    if(it.x < 0 || it.y < 0 ) return@forEach
                    val xPos = it.x - 1
                    if(xPos >= 0 && tiles[it.y][xPos]) return false
                }
            }
            Rotation.HORIZONTAL -> {
                val referenceLocation = location[0]
                val referenceX = referenceLocation.x - 1
                if(referenceX >= 0 && tiles[referenceLocation.y][referenceX]) return false
            }
        }
        return true
    }

    private fun canMoveRight(
        tiles: Array<Array<Boolean>>
    )
    : Boolean {
        if(location[3].x >= posXLimit - 1) return false
        when(currentRotation) {
            Rotation.VERTICAL -> {
                location.forEach {
                    if(it.x < 0 || it.y < 0) return@forEach
                    val xPos = it.x + 1
                    if(xPos < posXLimit && tiles[it.y][xPos]) return false
                }
            }
            Rotation.HORIZONTAL -> {
                val referenceLocation = location[3]
                val referenceX = referenceLocation.x + 1
                if(referenceX >= 0 && tiles[referenceLocation.y][referenceX]) return false
            }
        }
        return true
    }

    override fun canRotate(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        return when(currentRotation) {
            Rotation.HORIZONTAL -> true
            Rotation.VERTICAL -> {
                val referenceLocation = location[2]
                if(referenceLocation.x < 0 || referenceLocation.y < 0) return false

                val minX = max((referenceLocation.x - 2),0)
                val maxX = min((referenceLocation.x + 1),tiles[0].size - 1)

                val minY =referenceLocation.y - 2
                val maxY = referenceLocation.y + 1
                for (positionY in minY..maxY){
                    for(positionX in minX..maxX) {
                        if(location.firstOrNull { it.x == positionX && it.y == positionY } != null) continue
                        if(tiles[positionY][positionX]) return false
                    }
                }
                true
            }
        }
    }


    override fun move() {
        if(location[0].y == -9) {
            initialMove()
        } else {
            copyCurrentLocToPreviousLoc()

            moveDown(posYLimit)
        }
    }

    private fun initialMove() {
        val randomXPoint = generateRandomNumber()
        location[0] = Position(randomXPoint,-3)
        location[1] = Position(randomXPoint,-2)
        location[2] = Position(randomXPoint,-1)
        location[3] = Position(randomXPoint,0)
    }

    override fun rotate() {
        copyCurrentLocToPreviousLoc()
        currentRotation = when(currentRotation) {
            Rotation.HORIZONTAL -> {
                rotateVertical()
                Rotation.VERTICAL
            }
            Rotation.VERTICAL -> {
                rotateHorizontal()
                Rotation.HORIZONTAL
            }
        }
    }

    private fun rotateVertical() {
        val referenceLocation = location[2]
        val referenceYPoint = if((referenceLocation.y - 2) > 0) {
            if((referenceLocation.y + 1) > posYLimit) {
                referenceLocation.y - 1
            } else {
                referenceLocation.y
            }
        } else {
            val diff = abs(referenceLocation.y - 2)
            referenceLocation.y + diff
        }
        location.forEachIndexed { index, _ ->
            val posY = (index - 2) + referenceYPoint
            location[index] = Position(referenceLocation.x, posY)
        }
    }

    private fun rotateHorizontal() {
        val referenceLocation = location[2]
        val referenceXPoint = if((referenceLocation.x - 2) > 0) {
            if(referenceLocation.x + 1 >= posXLimit) {
                referenceLocation.x-1
            } else {
                referenceLocation.x
            }
        } else {
            val diff = abs(referenceLocation.x - 2)
            referenceLocation.x + diff
        }
            location.forEachIndexed { index, _ ->
                val posX = (index - 2) + referenceXPoint
                location[index] = Position(posX, referenceLocation.y)
            }

    }

    enum class Rotation {
        HORIZONTAL,
        VERTICAL
    }
}