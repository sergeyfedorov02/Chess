package sample.figures.white;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class KnightWhite extends FigureWhite {

    public KnightWhite(Square position) {
        super(position, FigureKind.Knight);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidKnightMove(square,chessBoard);
    }

    @Override
    public Figure copy() {
        return new KnightWhite(this.position());
    }
}
