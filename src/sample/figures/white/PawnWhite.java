package sample.figures.white;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class PawnWhite extends FigureWhite {

    public PawnWhite(Square position) {
        super(position, FigureKind.Pawn);
    }

    @Override
    public Figure copy() {
        return new PawnWhite(this.position());
    }

    @Override
    public boolean canMoveTo(Square end, ChessBoard chessBoard) {
        var upper = position().getUpSquare();
        if (end == upper) {
            return chessBoard.getFigure(upper) == null;
        }

        if (isInitialPosition()) {
            if (end == upper.getUpSquare()) {
                return chessBoard.getFigure(upper) == null && chessBoard.getFigure(end) == null;
            }
        }

        return canTakeAt(end, chessBoard) && !isKingAtPosition(end, false, chessBoard);
    }

    @Override
    public boolean controlSquare(Square end, ChessBoard chessBoard) {
        return canTakeAt(end, chessBoard);
    }

    private boolean isInitialPosition() {
        return position().y == 6;
    }
    private boolean canTakeAt(Square square, ChessBoard chessBoard) {

        var figure = chessBoard.getFigure(square);
        if (figure == null || figure.isWhite()) {
            return false;
        }

        return position().getRightUpSquare() == square || position().getLeftUpSquare() == square;
    }

}