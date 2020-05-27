package sample.figures.white;

import sample.figures.*;

public class KnightWhite extends Knight {

    public KnightWhite(Square position) {
        super(position,true);
    }

    @Override
    public Figure copy() {
        return new KnightWhite(this.position());
    }
}
