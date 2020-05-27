package sample.figures;

public interface ChessBoard {

    Figure getFigure(Square position);

    boolean  HasFiguresOnSameHorizontal(Square position, int endX);

    boolean HasFiguresOnSameVertical(Square position, int endY);

    boolean HasFiguresOnSameDiagonal(Square position, Square end);

}
