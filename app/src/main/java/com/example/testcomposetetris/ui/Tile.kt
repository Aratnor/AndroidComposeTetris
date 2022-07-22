package com.example.testcomposetetris.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.testcomposetetris.domain.models.Tile
import com.example.testcomposetetris.ui.theme.EMPTY_TILE_COLOR

fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    isShadowed: Boolean = false,
    tile: Tile,
    cornerRadius: Float = 12F,
    isInvisible: Boolean = false) {
    val color = when {
        tile.isOccupied -> Color.White
        isShadowed -> Color.Gray
        else -> EMPTY_TILE_COLOR
    }
    val alpha = when {
        tile.isOccupied -> 1.0F
        isShadowed -> 0.3F
        isInvisible -> 0F
        else -> 1F
    }

    tile(topLeftPosition,width,color,alpha,tile,isShadowed,cornerRadius)
}


fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    color: Color = Color.Black,
    alpha: Float = 1F,
    tile: Tile,
    isShadowed: Boolean,
    cornerRadius: Float = 12F
) {
    if(tile.isOccupied) {
        if(!isShadowed) {
            drawShadowBehindTile(
                0.18F,
                topLeftPosition,
                width,
                width,
                shadowRadius = 18F,
                color = tile.color.startColor
            )
        }
        val gradient = Brush.radialGradient(
            listOf(tile.color.endColor, tile.color.startColor),
            Offset(
                topLeftPosition.x + width / 2,
                topLeftPosition.y + width / 2
            ),
            radius = width - width * 4 / 36
        )
        drawRoundRect(
            gradient,
            topLeftPosition,
            Size(width,width),
            CornerRadius(cornerRadius,cornerRadius),
            alpha,
            Fill
        )
        drawRoundRect(
            tile.color.strokeColor,
            topLeftPosition,
            Size(width,width),
            CornerRadius(cornerRadius,cornerRadius),
            Stroke(4F),
            alpha
        )
    } else if(isShadowed) {
        drawRect(
            color,
            topLeftPosition,
            Size(width,width),
            alpha,
            Stroke(6F),
            )
    } else {
        drawRoundRect(
            color,
            topLeftPosition,
            Size(width,width),
            CornerRadius(4F,4F),
            Fill,
            alpha,
        )
    }
}

fun DrawScope.drawShadowBehindTile(
    alpha: Float = 0.2f,
    topLeftPosition: Offset,
    width: Float,
    height: Float,
    radius: Float = 20F,
    shadowRadius: Float = 30F,
    topShadowMultiplier: Float = 0.1F,
    leftShadowMultiplier: Float = 0.1F,
    rightShadowMultiplier: Float = 0.4F,
    bottomShadowMultiplier: Float = 0.4F,
    color: Color = Color.Black
) {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()

    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.color = transparent

    frameworkPaint.setShadowLayer(
        30F,
        0F,
        0F,
        shadowColor
    )

    drawContext.canvas.drawRoundRect(
        topLeftPosition.x - width * (leftShadowMultiplier),
        topLeftPosition.y - height * (topShadowMultiplier),
        topLeftPosition.x + width * (1F + rightShadowMultiplier),
        topLeftPosition.y + height * (1F +  + bottomShadowMultiplier),
        radius,
        radius,
        paint
    )
}