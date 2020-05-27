package sample;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

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

    @Override
    public boolean HasFiguresOnSameHorizontal(Square position, int endX) {
        var toLeft = position.x > endX;
        int number = abs(position.x - endX) - 1;

        var square = position;
        for (int i = 0; i < number ; i++) {
            square = toLeft ? square.getLeftSquare() : square.getRightSquare();
            if (getFigure(square) != null)
                return true;
        }
        return false;
    }

    @Override
    public boolean HasFiguresOnSameVertical(Square position, int endY) {
        var up = position.y > endY;
        int number = abs(position.y - endY) - 1;

        var square = position;
        for (int i = 0; i < number ; i++) {
            square = up ? square.getUpSquare() : square.getDownSquare();
            if (getFigure(square) != null)
                return true;
        }
        return false;
    }

    @Override
    public boolean HasFiguresOnSameDiagonal(Square position, Square end) {
        var endX = end.x;
        var endY = end.y;

        var up = position.y > endY;
        var right = position.x < endX;

        int number = abs(position.y - endY) - 1;

        var square = position;
        for (int i = 0; i < number ; i++) {

            if (up) {
                square = right ? square.getRightUpSquare() : square.getLeftUpSquare();
            } else square = right ? square.getRightDownSquare() : square.getLeftDownSquare();

            if (getFigure(square) != null)
                return true;
        }
        return false;
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
