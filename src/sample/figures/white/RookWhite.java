package sample.figures.white;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class RookWhite extends FigureWhite {

    public RookWhite(Square position) {
        super(position, FigureKind.Rook);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidRookMove(square,chessBoard);
    }

    @Override
    public Figure copy() {
        return new RookWhite(this.position());
    }
}