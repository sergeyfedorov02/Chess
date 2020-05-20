package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class KingBlack extends FigureBlack {

    public KingBlack(Square position) {
        super(position, FigureKind.King);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidKingMove(square,chessBoard);
    }

    @Override
    public Figure copy() {
        return new KingBlack(this.position());
    }
}