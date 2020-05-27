package sample.figures.white;

import sample.figures.*;

public class BishopWhite extends Bishop {

    public BishopWhite(Square position) {
        super(position, true);
    }

    @Override
    public Figure copy() {
        return new BishopWhite(this.position());
    }

}
