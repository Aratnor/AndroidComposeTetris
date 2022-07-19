package com.example.testcomposetetris.domain.models.piece

import android.util.Log
import com.example.testcomposetetris.domain.models.Position

abstract class Piece {
    abstract val location: Array<Position>
    abstract val previousLocation: Array<Position>
    abstract val previewLocation: Array<Position>
    abstract val destinationLocation: Array<Position>
    var isDestinationLocationChanged = true
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

    fun calculateDestinationLoc(
        tiles: Array<Array<Boolean>>
    ) {
        if(isDestinationLocationChanged) {
            isDestinationLocationChanged = false
            location.forEachIndexed { index, position ->
                destinationLocation[index] = position
            }
            var canMove = true
            while(canMove) {
                destinationLocation.forEach { piece ->
                    if(piece.x < 0 || piece.y < 0) return@forEach
                    if(destinationLocation.firstOrNull { it.y == piece.y + 1 && it.x == piece.x } != null ) return@forEach
                    if(location.firstOrNull { it.y == piece.y + 1 && it.x == piece.x } != null) return@forEach
                    if(piece.y + 1 >= tiles.size) {
                        canMove = false
                        return
                    }
                    if(tiles[piece.y + 1][piece.x]) {
                        canMove = false
                        return
                    }
                }
                destinationLocation.forEachIndexed { index, position ->
                    destinationLocation[index] = position.copy(y = position.y.inc())
                }
            }
        }
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