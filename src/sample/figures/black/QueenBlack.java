package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class QueenBlack extends FigureBlack {

    public QueenBlack(Square position) {
        super(position, FigureKind.Queen);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidBishopMove(square,chessBoard) || isValidRookMove(square, chessBoard);
    }

    @Override
    public Figure copy() {
        return new QueenBlack(this.position());
    }
}