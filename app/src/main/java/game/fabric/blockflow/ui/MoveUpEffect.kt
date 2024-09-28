package game.fabric.blockflow.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import game.fabric.blockflow.gamelogic.domain.models.TileColor

fun DrawScope.moveUpEffect(
    initialOffset: Offset,
    destinationOffset: Offset,
    size: Size,
    color: TileColor
    ) {
        val linearGradient = Brush.verticalGradient(
            0.1F to Color.Transparent,
            0.8F to color.startColor.copy(0.5F),
            startY = initialOffset.y - 10,
            endY = destinationOffset.y
        )
        drawRoundRect(
            linearGradient,
            initialOffset,
            size,
            CornerRadius(12F,12F),
            1F,
            Fill
        )

}