package sample.figures.white;

import sample.figures.FigureBase;
import sample.figures.FigureKind;
import sample.figures.Square;

public abstract class FigureWhite extends FigureBase {
    public FigureWhite(Square position, FigureKind kind)
    {
        super(true, position, kind);
    }
}
