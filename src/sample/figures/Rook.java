package sample.figures;

public abstract class Rook extends FigureBase{

    public Rook(Square position, boolean isWhite)
    {
        super (isWhite, position, FigureKind.Rook);
    }

    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidRookMove(square, chessBoard);
    }

    protected boolean isValidRookMove(Square end, ChessBoard chessBoard) {
        if(end == position()) {
            return false;
        }

        var figure = chessBoard.getFigure(end);

        if (end.x == position().x) {
            if (HasFiguresOnSameVertical(end.y, chessBoard)) {
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
