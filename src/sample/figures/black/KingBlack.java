package sample.figures.black;

import sample.figures.*;

public class KingBlack extends King {

    public KingBlack(Square position) {
        super(position, false);
    }

    @Override
    public Figure copy() {
        return new KingBlack(this.position());
    }
}