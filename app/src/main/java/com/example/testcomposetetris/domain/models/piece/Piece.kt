package com.example.testcomposetetris.domain.models.piece

import android.util.Log
import com.example.testcomposetetris.domain.models.Position

abstract class Piece {
    abstract val location: Array<Position>
    abstract val previousLocation: Array<Position>
    abstract val posXLimit: Int
    abstract val posYLimit: Int
    abstract fun move()
    abstract fun moveLeft(tiles: Array<Array<Boolean>>)
    abstract fun moveRight(tiles: Array<Array<Boolean>>)
    abstract fun rotate()
    abstract fun canRotate(tiles: Array<Array<Boolean>>): Boolean

    fun moveDown(posYLimit: Int) {
        location.forEachIndexed { index, position ->
            if(position.y < posYLimit - 1) {
                location[index] = position.copy(y = position.y.inc())
            }
        }
    }

    fun hitAnotherPiece(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        location.forEachIndexed { _, position ->
            if(position.x < 0 || position.y < 0) return@forEachIndexed

            if(position.y == tiles.size - 1) return true

            val isNextLocationIsInPiece = location.firstOrNull { it.y == position.y + 1 && it.x == position.x } != null
            if(!isNextLocationIsInPiece && tiles[position.y + 1][position.x]) return true
        }

        return false
    }

    protected fun copyCurrentLocToPreviousLoc() {
        location.forEachIndexed { index, position ->
            previousLocation[index] = Position(x = position.x,y = position.y)
        }
        Log.i("Piece",previousLocation.toString())
    }

    protected fun canMoveToNextTiles(
        referencePosition: Position,
        tiles: Array<Array<Boolean>>
    ): Boolean {
        for(posX in (referencePosition.x - 1)..(referencePosition.x + 1)){
            for(posY in (referencePosition.y - 1)..(referencePosition.y + 1)) {
                if(posY < 0 || posY >= posYLimit)  continue
                if(posX < 0 || posX >= posXLimit) continue
                if(location.firstOrNull { it.x == posX && it.y == posY } != null) continue

                if(tiles[posY][posX]) return false
            }
        }
        return true
    }
}