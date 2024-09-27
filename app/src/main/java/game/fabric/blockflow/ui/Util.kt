package game.fabric.blockflow.ui

import androidx.compose.ui.graphics.drawscope.DrawScope
import game.fabric.blockflow.ViewState

fun DrawScope.calculateWidthOfRect(
    padding: Float,
    startEndPadding: Float,
    viewState: ViewState
): Float {
    val paddingForTotalWidth = (viewState.tiles[0].size - 1) * padding + startEndPadding * 2
    val rectWidthByLayoutWidth = (size.width - 100 - paddingForTotalWidth) / viewState.tiles[0].size
    val totalTilesHeight = (rectWidthByLayoutWidth + padding) * viewState.tiles.size

    val marginTop = 400
    return if(totalTilesHeight > size.height - marginTop) {
        val paddingForTotalHeight = (viewState.tiles.size - 1) * padding + marginTop
        val rectWidthByLayoutHeight = (size.height - paddingForTotalHeight) / viewState.tiles.size
        rectWidthByLayoutHeight
    } else {
        rectWidthByLayoutWidth
    }
}