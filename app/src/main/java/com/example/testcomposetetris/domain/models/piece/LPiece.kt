package com.example.testcomposetetris.domain.models.piece

import com.example.testcomposetetris.domain.generateRandomNumber
import com.example.testcomposetetris.domain.models.Position
import com.example.testcomposetetris.domain.models.Tile

class LPiece(
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
        Position(0,-9),
        Position(0,-9)
    )
    override val previewLocation: Array<Position> = arrayOf(
        Position(0,1),
        Position(0,0),
        Position(1,0),
        Position(2,0)
    )

    override val destinationLocation: Array<Position> = arrayOf(
        Position(0,-9),
        Position(0,-9),
        Position(0,-9),
        Position(0,-9)
    )

    private var currentRotation: Rotation = Rotation.LEFT

    override fun move() {
        if(location[0].y == -9) {
            initialize()
        } else {
            copyCurrentLocToPreviousLoc()
            moveDown(posYLimit)
        }
    }

    private fun initialize() {
        var randomXPoint = generateRandomNumber()
        randomXPoint = when {
            randomXPoint == 0 -> 1
            randomXPoint == posXLimit - 1 -> posXLimit - 2
            else -> randomXPoint
        }
        initializeRotationLeft(randomXPoint)
    }

    private fun initializeRotationLeft(referenceXPoint: Int) {
        location[0] = Position(referenceXPoint - 1,0)
        location[1] = Position(referenceXPoint -1, -1)
        location[2] = Position(referenceXPoint,-1)
        location[3] = Position(referenceXPoint + 1,-1)
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

    override fun moveLeft(tiles: Array<Array<Tile>>) {
        if(canMoveLeft(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.dec())
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

    override fun moveRight(tiles: Array<Array<Tile>>) {
        if(canMoveRight(tiles)) {
            copyCurrentLocToPreviousLoc()
            location.forEachIndexed { index, position ->
                location[index] = position.copy(x = position.x.inc())
            }
        }
    }

    override fun rotate() {
        copyCurrentLocToPreviousLoc()
        currentRotation = when(currentRotation) {
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
            Rotation.BOTTOM -> {
                rotateLeft()
                Rotation.LEFT
            }
        }
    }

    private fun rotateTop() {
        val referencePosition = location[2]
        location[0] = Position(referencePosition.x -1,referencePosition.y - 1)
        location[1] = Position(referencePosition.x,referencePosition.y - 1)
        location[3] = Position(referencePosition.x,referencePosition.y + 1)
    }

    private fun rotateRight() {
        val referencePosition = location[2]
        location[0] = Position(referencePosition.x + 1,referencePosition.y - 1)
        location[1] = Position(referencePosition.x + 1,referencePosition.y)
        location[3] = Position(referencePosition.x -1,referencePosition.y)
    }

    private fun rotateBottom() {
        val referencePosition = location[2]
        location[0] = Position(referencePosition.x + 1,referencePosition.y + 1)
        location[1] = Position(referencePosition.x,referencePosition.y + 1)
        location[3] = Position(referencePosition.x,referencePosition.y - 1)
    }

    private fun rotateLeft() {
        val referencePosition = location[2]
        location[0] = Position(referencePosition.x - 1,referencePosition.y + 1)
        location[1] = Position(referencePosition.x - 1,referencePosition.y)
        location[3] = Position(referencePosition.x + 1,referencePosition.y)
    }

    override fun canRotate(tiles: Array<Array<Tile>>): Boolean {
        return when(currentRotation) {
            Rotation.LEFT -> {
                canRotateTop(tiles)
            }
            Rotation.TOP -> {
                canRotateRight(tiles)
            }
            Rotation.RIGHT -> {
                canRotateBottom(tiles)
            }
            Rotation.BOTTOM -> {
                canRotateLeft(tiles)
            }
        }
    }

    private fun canRotateLeft(tiles: Array<Array<Tile>>): Boolean {
        val referencePosition = location[2]
        val minX = referencePosition.x - 1
        if(minX < 0) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun canRotateBottom(tiles: Array<Array<Tile>>): Boolean {
        val referencePosition = location[2]
        val maxX = referencePosition.x + 1
        val maxY = referencePosition.y + 1
        if(maxX >= posXLimit || maxY >= posYLimit) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun canRotateRight(tiles: Array<Array<Tile>>): Boolean {
        val referencePosition = location[2]
        val maxX = referencePosition.x + 1
        val minY = referencePosition.y - 1
        if(maxX >= posXLimit || minY < 0) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    private fun canRotateTop(tiles: Array<Array<Tile>>): Boolean {
        val referencePosition = location[2]
        val possibleMinX = referencePosition.x -1
        val possibleMinY = referencePosition.y - 1
        val possibleMaxY = referencePosition.y
        if(possibleMinX < 0 || possibleMinY < 0 || possibleMaxY >= posYLimit) return false
        return canMoveToNextTiles(referencePosition,tiles)
    }

    enum class Rotation {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
}