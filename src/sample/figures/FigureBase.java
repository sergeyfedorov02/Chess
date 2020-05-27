package sample.figures;

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
        return chessBoard.HasFiguresOnSameHorizontal(position(), endX);
    }

    protected boolean HasFiguresOnSameVertical(int endY, ChessBoard chessBoard) {
        return chessBoard.HasFiguresOnSameVertical(position(), endY);
    }

    protected boolean HasFiguresOnSameDiagonal(Square end,  ChessBoard chessBoard) {
        return chessBoard.HasFiguresOnSameDiagonal(position(), end);
    }

    // Проверка, не находится ли на король той клетке, куда ходим(короля есть нельзя)
    protected boolean isKingAtPosition(Square end, boolean isWhite, ChessBoard chessBoard) {
        var figure = chessBoard.getFigure(end);
        if (figure == null) {
            return false;
        }
        return figure.getKind() == FigureKind.King && figure.isWhite() == isWhite;
    }

    @Override
    public boolean canMoveTo(Square end, ChessBoard chessBoard) {
        return controlSquare(end, chessBoard) && !isKingAtPosition(end, !isWhite, chessBoard);
    }

}
