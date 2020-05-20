package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class RookBlack extends FigureBlack {

    public RookBlack(Square position) {
        super(position, FigureKind.Rook);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidRookMove(square,chessBoard);
    }

    @Override
    public Figure copy() {
        return new RookBlack(this.position());
    }
}