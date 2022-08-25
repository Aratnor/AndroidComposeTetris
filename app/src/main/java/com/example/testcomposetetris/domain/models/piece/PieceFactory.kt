package com.example.testcomposetetris.domain.models.piece

class PieceFactory {

    companion object {
        fun generatePiece(
            piece: Piece
        ): Piece = when (piece) {
                is IPiece -> IPiece(piece.posXLimit,piece.posYLimit)
                is SquarePiece -> SquarePiece(piece.posXLimit,piece.posYLimit)
                is LPiece -> LPiece(piece.posXLimit,piece.posYLimit)
                is ReverseLPiece -> ReverseLPiece(piece.posXLimit,piece.posYLimit)
                is ZPiece -> ZPiece(piece.posXLimit,piece.posYLimit)
                is ReverseZPiece -> ReverseZPiece(piece.posXLimit,piece.posYLimit)
                is TPiece -> TPiece(piece.posXLimit,piece.posYLimit)
                else -> throw TypeCastException("Not Supported Piece Type")
            }.apply { pieceColor = piece.pieceColor }

    }
}