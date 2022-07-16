package com.example.testcomposetetris.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.toArgb

fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    isNotEmpty: Boolean = false,
    isShadowed: Boolean = false) {
    val color = when {
        isNotEmpty -> Color.Black
        isShadowed -> Color.DarkGray
        else -> Color.Gray
    }
    val alpha = when {
        isNotEmpty -> 1F
        isShadowed -> 0.5F
        else -> 0.3F
    }

    tile(topLeftPosition,width,color,alpha,isNotEmpty,isShadowed)
}


fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    color: Color = Color.Black,
    alpha: Float = 1F,
    isNotEmpty: Boolean,
    isShadowed: Boolean
) {
    if(isNotEmpty) {
        if(!isShadowed){
            drawShadowBehindTile(
                topLeftPosition = topLeftPosition,
                width = width
            )
        }
        drawRoundRect(
            color,
            topLeftPosition,Size(width,width),
            CornerRadius(12F,12F),
            Fill,
            alpha
        )
    } else {
        drawRect(
            color,
            topLeftPosition,
            Size(width,width),
            alpha,
            Fill
        )
    }
}

fun DrawScope.drawShadowBehindTile(
    alpha: Float = 0.2f,
    topLeftPosition: Offset,
    width: Float,
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
        topLeftPosition.x - width * (0.1F),
        topLeftPosition.y - width * (0.1F),
        topLeftPosition.x + width * (1.4F),
        topLeftPosition.y + width * (1.4F),
        20F,
        20F,
        paint
    )
}