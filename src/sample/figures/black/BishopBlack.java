package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class BishopBlack extends FigureBlack {

    public BishopBlack(Square position) {
        super(position, FigureKind.Bishop);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidBishopMove(square, chessBoard);
    }

    @Override
    public Figure copy() {
        return new BishopBlack(this.position());
    }
}