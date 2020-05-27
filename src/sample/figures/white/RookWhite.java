package sample.figures.white;

import sample.figures.*;

public class RookWhite extends Rook {

    public RookWhite(Square position) {
        super(position, true);
    }

    @Override
    public Figure copy() {
        return new RookWhite(this.position());
    }
}