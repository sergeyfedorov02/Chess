package sample.figures;

import static java.lang.Math.abs;

public abstract class King extends FigureBase{
    public King(Square position, boolean isWhite)
    {
        super (isWhite, position, FigureKind.King);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidKingMove(square, chessBoard);
    }

    protected boolean isValidKingMove(Square end, ChessBoard chessBoard) {
        if(end == position()) {
            return false;
        }

        var figure = chessBoard.getFigure(end);

        if ((abs(position().x - end.x)== 1 || abs(position().x - end.x)==0) &&
                (abs(position().y - end.y) == 1 || abs(position().y - end.y)==0)) {
            return figure == null || ((isWhite != figure.isWhite()));
        }

        return false;
    }

}
