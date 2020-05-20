package sample.figures.white;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class BishopWhite extends FigureWhite {

    public BishopWhite(Square position) {
        super(position, FigureKind.Bishop);
    }


    @Override
    public boolean controlSquare(Square square, ChessBoard chessBoard) {
        return isValidBishopMove(square, chessBoard);
    }

    @Override
    public Figure copy() {
        return new BishopWhite(this.position());
    }

}
