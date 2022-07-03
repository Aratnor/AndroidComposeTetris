package com.example.testcomposetetris.ui


import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.testcomposetetris.MainViewModel

@Composable
fun NextPieceLayout(
    viewModel: MainViewModel
) {
    val viewState = viewModel.viewState.value

    Canvas(
        modifier = Modifier.padding(start = 200.dp, top = 40.dp)) {
        val padding = 12
        val widthOfRectangle = 40F

        repeat(4) { posY ->
            repeat(4) { posX ->
                val startPositionX = (posX) * (padding + widthOfRectangle)
                val startPositionY = (posY) * (padding + widthOfRectangle)
                val isNotEmpty =viewState
                    .nextPiecePreview
                    .firstOrNull { it.x == posX && it.y == posY } != null
                tile(Offset(startPositionX,startPositionY),widthOfRectangle,isNotEmpty)
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                viewState.currentTime,
                48F,
                4 * (padding + widthOfRectangle) + 70,
                Paint().apply {
                    textSize = 48F
                    color = Color.BLACK
                    textAlign = Paint.Align.CENTER
                }
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                viewState.score,
                48F,
                4 * (padding + widthOfRectangle) + 140,
                Paint().apply {
                    textSize = 48F
                    color = Color.BLACK
                    textAlign = Paint.Align.CENTER
                }
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                viewState.level,
                48F,
                4 * (padding + widthOfRectangle) + 220,
                Paint().apply {
                    textSize = 48F
                    color = Color.BLACK
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}