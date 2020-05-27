package sample.figures;

import static java.lang.Math.abs;

public abstract class Queen extends FigureBase {

    public Queen(Square position, boolean isWhite) {
        super(isWhite, position, FigureKind.Queen);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidQueenMove(square, chessBoard);
    }

    protected boolean isValidQueenMove(Square end, ChessBoard chessBoard) {

        var figure = chessBoard.getFigure(end);

        if(end == position()) {
            return false;
        }

        if (end.x == position().x) {
            if (HasFiguresOnSameVertical(end.y, chessBoard)) {
                return false;
            }
            return figure == null || ((isWhite != figure.isWhite()));
        }

        if (abs(position().x - end.x)==abs(position().y - end.y)) {
            if (HasFiguresOnSameDiagonal(end, chessBoard)) {
                return false;
            }
            return figure == null || ((isWhite != figure.isWhite()));
        }

        if (end.y == position().y) {
            if (HasFiguresOnSameHorizontal(end.x, chessBoard)) {
                return false;
            }
            return figure == null || ((isWhite != figure.isWhite()));
        }

        return false;
    }
}
