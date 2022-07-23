package com.example.testcomposetetris.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.example.testcomposetetris.domain.models.TileColor
import com.example.testcomposetetris.ui.theme.MOVE_UP_FINAL
import com.example.testcomposetetris.ui.theme.MOVE_UP_INITIAL

fun DrawScope.moveUpEffect(
    initialOffset: Offset,
    destinationOffset: Offset,
    size: Size,
    color: TileColor
    ) {
        val linearGradient = Brush.verticalGradient(
            listOf(
                color.startColor.copy(alpha = 0.2F),
                color.endColor
            ),
            initialOffset.y,
            destinationOffset.y
        )
        drawRect(
            linearGradient,
            initialOffset,
            Size(
                size.width,
                size.height
            ),
            1F,
            Fill
        )

}