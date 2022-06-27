package com.example.testcomposetetris.domain.models.piece

import android.util.Log
import com.example.testcomposetetris.domain.models.Position

abstract class Piece {
    abstract val location: Array<Position>
    abstract val previousLocation: Array<Position>
    abstract fun move()
    abstract fun moveLeft(tiles: Array<Array<Boolean>>)
    abstract fun moveRight(tiles: Array<Array<Boolean>>)
    abstract fun rotate()
    abstract fun canRotate(tiles: Array<Array<Boolean>>): Boolean

    fun hitAnotherPiece(
        tiles: Array<Array<Boolean>>
    ): Boolean {
        location.forEachIndexed { _, position ->
            if(position.x < 0 || position.y < 0) return@forEachIndexed

            if(position.y == tiles.size - 1) return true

            val isNextLocationIsInPiece = location.firstOrNull { it.y == position.y + 1 } != null
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
}