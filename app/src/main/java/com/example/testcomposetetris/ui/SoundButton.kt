package com.example.testcomposetetris.ui

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.example.testcomposetetris.ui.theme.GAME_BUTTON_BACKGROUND

fun DrawScope.soundButton(
    buttonOffSet: Offset,
    isMuted: Boolean,
    muteSoundBitmap: Bitmap?,
    openSoundBitmap: Bitmap?,
    onSizeMeasured: (Size) -> Unit
) {
    if(isMuted) {
        muteSoundBitmap?.let {
            onSizeMeasured(Size(it.width.toFloat(),it.height.toFloat()))
            val radius = if(it.width > it.height) {
                it.width / 2F
            } else {
                it.height / 2F
            }

            drawCircle(
                GAME_BUTTON_BACKGROUND,
                radius,
                Offset(
                    buttonOffSet.x + it.width / 2,
                    buttonOffSet.y + it.height / 2
                ),
                1F,
                Fill
            )

            drawImage(
                it.asImageBitmap(),
                buttonOffSet
            )
        }
    } else {
        openSoundBitmap?.let {
            onSizeMeasured(Size(it.width.toFloat(),it.height.toFloat()))

            val radius = if(it.width > it.height) {
                it.width / 2 + 30F
            } else {
                it.height / 2 + 30F
            }

            drawCircle(
                GAME_BUTTON_BACKGROUND,
                radius,
                Offset(
                    buttonOffSet.x + it.width / 2,
                    buttonOffSet.y + it.height / 2
                ),
                1F,
                Fill
            )

            drawImage(
                it.asImageBitmap(),
                buttonOffSet
            )
        }

    }
}