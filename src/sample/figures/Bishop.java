package sample.figures;

import static java.lang.Math.abs;

public abstract class Bishop extends FigureBase{

    public Bishop(Square position, boolean isWhite)
    {
        super (isWhite, position, FigureKind.Bishop);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidBishopMove(square, chessBoard);
    }

    protected boolean isValidBishopMove(Square end, ChessBoard chessBoard) {
        if(end == position()) {
            return false;
        }

        var figure = chessBoard.getFigure(end);

        if (abs(position().x - end.x)==abs(position().y - end.y)) {
            if (HasFiguresOnSameDiagonal(end, chessBoard)) {
                return false;
            }
            return figure == null || ((isWhite != figure.isWhite()));
        }
        return false;
    }
}
