package com.example.testcomposetetris.ui

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
    drawRect(
        color,
        topLeftPosition,
        Size(width,width),
        alpha,
        Stroke(width = 2F)
    )
    val innerOffset = 8
    val innerTopLeftPosition = Offset(
        topLeftPosition.x + innerOffset,
        topLeftPosition.y + innerOffset
    )
    val innerWidth = width - 2 * innerOffset
    drawRect(
        color,
        innerTopLeftPosition,
        Size(innerWidth,innerWidth),
        alpha,
        Fill
    )
}