package sample.figures.black;

import sample.figures.*;

public class RookBlack extends Rook {

    public RookBlack(Square position) {
        super(position, false);
    }

    @Override
    public Figure copy() {
        return new RookBlack(this.position());
    }
}