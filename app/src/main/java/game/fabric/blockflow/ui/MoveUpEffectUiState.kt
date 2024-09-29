package game.fabric.blockflow.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import game.fabric.blockflow.gamelogic.domain.models.TileColor

data class MoveUpEffectUiState(
    val initialOffset: Offset = Offset(0f,0f),
    val destinationOffset: Offset = Offset(0f,0f),
    val size: Size = Size(0f,0f),
    val color: TileColor = TileColor.EMPTY
)