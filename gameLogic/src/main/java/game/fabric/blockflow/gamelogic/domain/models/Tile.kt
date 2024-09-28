package game.fabric.blockflow.gamelogic.domain.models

data class Tile(
    val isOccupied: Boolean,
    val hasActivePiece: Boolean,
    val color: TileColor
)