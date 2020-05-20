package sample.figures;


public interface Figure {
    Square position();

    boolean canMoveTo(Square end, ChessBoard chessBoard);

    boolean controlSquare(Square square, ChessBoard chessBoard);

    boolean isWhite();

    Figure copy();

    default String icon() {
        return String.format(
                "%s.png", this.getClass().getSimpleName()
        );
    }

    void move(Square end);

    FigureKind getKind();

}

