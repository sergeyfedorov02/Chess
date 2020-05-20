package sample.figures;

import static java.lang.Math.abs;

public abstract class FigureBase implements Figure {
    final boolean isWhite;
    private Square position;
    private final FigureKind figureKind;
    public  FigureBase(boolean isWhite, Square position, FigureKind kind) {
        this.isWhite = isWhite;
        this.position = position;
        this.figureKind = kind;
    }

    @Override
    public Square position() {
        return this.position;
    }

    @Override
    public void move(Square newPosition) {
        this.position = newPosition;
    }

    @Override
    public boolean isWhite() {
        return this.isWhite;
    }

    @Override
    public FigureKind getKind() {
        return figureKind;
    }

    protected boolean  HasFiguresOnSameHorizontal(int endX, ChessBoard chessBoard) {

        var toLeft = position().x > endX;
        int number = abs(position().x - endX) - 1;

        var square = position();
        for (int i = 0; i < number ; i++) {
            square = toLeft ? square.getLeftSquare() : square.getRightSquare();
            if (chessBoard.getFigure(square) != null)
                return true;
        }
        return false;
    }

    protected boolean HasFiguresOnSameVertical(int endY, ChessBoard chessBoard) {

        var up = position().y > endY;
        int number = abs(position().y - endY) - 1;

        var square = position();
        for (int i = 0; i < number ; i++) {
            square = up ? square.getUpSquare() : square.getDownSquare();
            if (chessBoard.getFigure(square) != null)
                return true;
        }
        return false;
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

    protected boolean HasFiguresOnSameDiagonal(Square end,  ChessBoard chessBoard) {

        var endX = end.x;
        var endY = end.y;

        var up = position().y > endY;
        var right = position().x < endX;

        int number = abs(position().y - endY) - 1;

        var square = position();
        for (int i = 0; i < number ; i++) {

            if (up) {
                square = right ? square.getRightUpSquare() : square.getLeftUpSquare();
            } else square = right ? square.getRightDownSquare() : square.getLeftDownSquare();

            if (chessBoard.getFigure(square) != null)
                return true;
        }
        return false;
    }

    @Override
    public boolean canMoveTo(Square end, ChessBoard chessBoard) {
        return controlSquare(end, chessBoard) && !isKingAtPosition(end, !isWhite, chessBoard);
    }

    // Проверка, не находится ли на король той клетке, куда ходим(короля есть нельзя)
    protected boolean isKingAtPosition(Square end, boolean isWhite, ChessBoard chessBoard) {
        var figure = chessBoard.getFigure(end);
        if (figure == null) {
            return false;
        }
        return figure.getKind() == FigureKind.King && figure.isWhite() == isWhite;
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
