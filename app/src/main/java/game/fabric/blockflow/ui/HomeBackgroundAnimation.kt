package game.fabric.blockflow.ui


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import game.fabric.blockflow.domain.HomeAnimationState

fun DrawScope.homeBackgroundAnimation(
    animationState: HomeAnimationState
) {
    if(animationState.tiles.isEmpty()) return
    val padding = 12F
    val totalPaddingWidth = padding * (animationState.tiles.first().size - 1)
    val rectWidth = (size.width - totalPaddingWidth - 40) / animationState.tiles.first().size
    val marginStart = 12

    animationState.tiles.forEachIndexed { positionY, row ->
        row.forEachIndexed { positionX, tile ->
            val isShadowed = false
            val startPositionX = (positionX) * (rectWidth + padding) + marginStart
            val startPositionY = (positionY) * (rectWidth + padding)
            val isInvisible = !tile.isOccupied
            tile(
                Offset(startPositionX,startPositionY),
                rectWidth,
                isShadowed,
                tile,
                isInvisible = isInvisible
            )
        }
    }

}
