package sample.figures.black;

import sample.figures.*;

public class BishopBlack extends Bishop {

    public BishopBlack(Square position) {
        super(position, false);
    }

    @Override
    public Figure copy() {
        return new BishopBlack(this.position());
    }
}