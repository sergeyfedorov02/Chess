package sample.figures.black;

import sample.figures.*;

public class QueenBlack extends Queen {

    public QueenBlack(Square position) {
        super(position, false);
    }

    @Override
    public Figure copy() {
        return new QueenBlack(this.position());
    }
}