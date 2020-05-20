package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class KnightBlack extends FigureBlack {

    public KnightBlack(Square position) {
        super(position, FigureKind.Knight);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidKnightMove(square,chessBoard);
    }

    @Override
    public Figure copy() {
        return new KnightBlack(this.position());
    }
}