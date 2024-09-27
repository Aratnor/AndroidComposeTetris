package game.fabric.blockflow.ext

import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

fun DrawScope.drawText(
    text: String,
    startPosX: Float,
    startPosY: Float,
    paint: Paint
) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            text,
            startPosX,
            startPosY,
            paint
        )
    }

}