package sample.figures.white;

import sample.figures.*;

public class KingWhite extends King {

    public KingWhite(Square position) {
        super(position, true);
    }

    @Override
    public Figure copy() {
        return new KingWhite(this.position());
    }
}
