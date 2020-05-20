package sample.figures.black;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class PawnBlack extends FigureBlack {

    public PawnBlack(Square position) {
        super(position, FigureKind.Pawn);
    }

    @Override
    public Figure copy() {
        return new PawnBlack(this.position());
    }

    @Override
    public boolean canMoveTo(Square end, ChessBoard chessBoard) {
        var downer = position().getDownSquare();
        if (end == downer) {
            return chessBoard.getFigure(downer) == null;
        }

        if (isInitialPosition()) {
            if (end == downer.getDownSquare()) {
                return chessBoard.getFigure(downer) == null && chessBoard.getFigure(end) == null;
            }
        }

        return canTakeAt(end, chessBoard) && !isKingAtPosition(end, true, chessBoard);
    }

    @Override
    public boolean controlSquare(Square end, ChessBoard chessBoard) {
        return canTakeAt(end, chessBoard);
    }

    private boolean isInitialPosition() {
        return position().y == 1;
    }
    private boolean canTakeAt(Square square, ChessBoard chessBoard) {

        var figure = chessBoard.getFigure(square);
        if (figure == null || !figure.isWhite()) {
            return false;
        }

        return position().getRightDownSquare() == square || position().getLeftDownSquare() == square;
    }

}
