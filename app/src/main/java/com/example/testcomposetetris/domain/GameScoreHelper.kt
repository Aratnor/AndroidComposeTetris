package com.example.testcomposetetris.domain

import com.example.testcomposetetris.domain.models.Position

class GameScoreHelper(
    private val tiles: Array<Array<Boolean>>
) {

    fun removeCompletedLines(
        completedLines: List<Int>
    ) {
        completedLines.forEach { posY ->
            for(posX in 0 until tiles[posY].size) {
                tiles[posY][posX] = false
            }
        }
    }

    fun fillCompletedLines(
        completedLines: List<Int>
    ) {
        completedLines.forEach { posY ->
            for(posX in 0 until tiles[posY].size) {
                tiles[posY][posX] = true
            }
        }
    }

    fun moveLinesOneDown(
        completedLines: List<Int>
    ) {
        completedLines.forEach { posY ->
            moveYLinesOneDown(posY)

        }
    }

    private fun moveYLinesOneDown(
        completedLinePosY: Int
    ) {
        val startingLinePosY = completedLinePosY - 1
        for(currentYPos in startingLinePosY downTo 0) {
            val destinationYPos = currentYPos + 1
            for(currentXPos in 0 until tiles[currentYPos].size) {
                tiles[destinationYPos][currentXPos] = tiles[currentYPos][currentXPos]
            }
        }
    }

    fun getCompletedLines(
        locations: Array<Position>
    ): List<Int> {
        val completedLinesPosY = mutableListOf<Int>()
        locations.forEach { position ->
            if(completedLinesPosY.contains(position.y)) return@forEach
            var isLineCompleted = true
            run {
                for (posX in 0 until tiles[position.y].size) {
                    if (!tiles[position.y][posX]) {
                        isLineCompleted = false
                        return@run
                    }
                }
            }
            if(isLineCompleted) completedLinesPosY.add(position.y)
        }

        return completedLinesPosY.sorted()
    }
}