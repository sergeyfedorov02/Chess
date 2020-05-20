package sample;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

import java.util.*;
import java.util.stream.Collectors;

public class ChessPosition implements ChessBoard {
    private final List<Figure> figures = new ArrayList<>();

    public void add(Figure figure) {
        figures.add(figure);
    }

    public ChessPosition copy() {
       var newPosition = new ChessPosition();
       figures.forEach(item -> newPosition.add(item.copy()));
       return newPosition;
    }

    public Figure getFigure(Square square) {
        return figures.stream().filter(item -> item.position() == square).findFirst().orElse(null);
    }

    public void removeFigure(Figure figure) {
        figures.remove(figure);
    }

    public void clearSquare(Square square) {
        var figure = figures.stream().filter(item -> item.position() == square).findFirst().orElse(null);

        if (figure != null) {
            figures.remove(figure);
        }
    }

    public Figure getKing(boolean isWhite) {
        return figures.stream().filter(item -> item.getKind() == FigureKind.King && item.isWhite() == isWhite)
                .findFirst().orElse(null);
    }

    public List<Figure> getFigures (boolean isWhite) {
        return figures.stream().filter(item -> item.isWhite() == isWhite).collect(Collectors.toList());
    }

    public void clean() {
        figures.clear();
    }

    @Override
    public String toString() {
       return Arrays.toString(this.figures.toArray());
    }
}
