package com.example.testcomposetetris.ui

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.testcomposetetris.ViewState

fun DrawScope.calculateWidthOfRect(
    padding: Float,
    startEndPadding: Float,
    nextPieceSizeX: Float,
    viewState: ViewState
): Float {
    val paddingForTotalWidth = (viewState.tiles[0].size - 1) * padding + startEndPadding * 2
    val rectWidthByLayoutWidth = (size.width - 100 - paddingForTotalWidth - nextPieceSizeX) / viewState.tiles[0].size
    val totalTilesHeight = (rectWidthByLayoutWidth + padding) * viewState.tiles.size
    return if(totalTilesHeight > size.height - 100) {
        val paddingForTotalHeight = (viewState.tiles.size - 1) * padding
        val rectWidthByLayoutHeight = (size.height - paddingForTotalHeight) / viewState.tiles.size
        rectWidthByLayoutHeight
    } else {
        rectWidthByLayoutWidth
    }
}