package sample.figures.black;

import sample.figures.FigureBase;
import sample.figures.FigureKind;
import sample.figures.Square;

public abstract class FigureBlack extends FigureBase {
    public FigureBlack(Square position, FigureKind kind)
    {
        super(false, position, kind);
    }
}
