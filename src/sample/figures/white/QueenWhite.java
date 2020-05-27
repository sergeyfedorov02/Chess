package sample.figures.white;

import sample.figures.*;

public class QueenWhite extends Queen {

    public QueenWhite(Square position) {
        super(position, true);
    }

    @Override
    public Figure copy() {
        return new QueenWhite(this.position());
    }
}
