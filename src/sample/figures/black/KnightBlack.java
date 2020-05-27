package sample.figures.black;

import sample.figures.*;

public class KnightBlack extends Knight {

    public KnightBlack(Square position) {
        super(position, false);
    }

    @Override
    public Figure copy() {
        return new KnightBlack(this.position());
    }
}