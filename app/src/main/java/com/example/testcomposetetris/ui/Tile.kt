package com.example.testcomposetetris.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    isNotEmpty: Boolean = false) {
    val color = if(isNotEmpty) {
        Color.Black
    } else {
        Color.Gray
    }
    val alpha = if(isNotEmpty) {
        1F
    } else {
        0.3F
    }

    tile(topLeftPosition,width,color,alpha,isNotEmpty)
}


fun DrawScope.tile(
    topLeftPosition: Offset,
    width: Float,
    color: Color = Color.Black,
    alpha: Float = 1F,
    isRounded: Boolean
) {
    if(isRounded) {
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