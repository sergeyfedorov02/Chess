package sample.figures;

import static java.lang.Math.abs;

public abstract class Knight extends FigureBase{

    public Knight(Square position, boolean isWhite)
    {
        super (isWhite, position, FigureKind.Knight);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidKnightMove(square, chessBoard);
    }

    protected boolean isValidKnightMove(Square end, ChessBoard chessBoard) {
        if(end == position()) {
            return false;
        }

        var figure = chessBoard.getFigure(end);

        if (abs(position().x - end.x)== 2 && abs(position().y - end.y) == 1 ||
                abs(position().x - end.x)== 1 && abs(position().y - end.y) == 2) {
            return figure == null || ((isWhite != figure.isWhite()));
        }
        return false;
    }
}
