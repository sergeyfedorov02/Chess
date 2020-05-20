package sample;

import sample.figures.ChessBoard;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessPosition implements ChessBoard {
    private final Figure[] figures = new Figure[64];
    private int index = 0;

    public void add(Figure figure) {
        this.figures[this.index++] = figure;
    }

    public ChessPosition copy() {
       var newPosition = new ChessPosition();
        Arrays.stream(this.figures).forEach(item ->
        {
            if (item != null) {
                newPosition.add(item.copy());
            }
        });
       return newPosition;
    }

    public Figure getFigure(Square square) {
        int index = this.findBy(square);

        if (index == -1) {
            return null;
        }
        return this.figures[index];
    }

    public void removeFigure(Figure figure) {
        for (int index = 0; index != this.figures.length; index++) {
            if (this.figures[index] == figure) {
                this.figures[index] = null;
                break;
            }
        }
    }

    public void clearSquare(Square square) {
        // удаляем фигуру в позиции, если она там есть
        int index = this.findBy(square);

        if (index == -1) {
            return;
        }

        this.figures[index] = null;
    }

    public Figure getKing(boolean isWhite) {
        for (int index = 0; index != this.figures.length; index++) {
            var figure = this.figures[index];
            if (figure != null && figure.getKind() == FigureKind.King && figure.isWhite() == isWhite) {
                return figure;
            }
        }
        return null;
    }

    public List<Figure> getFigures (boolean isWhite) {
        var result = new ArrayList<Figure>();

        for (var index = 0; index != this.figures.length; index++) {
            var figure = this.figures[index];
            if (figure != null && figure.isWhite() == isWhite) {
                result.add(figure);
            }
        }
        return result;
    }

    public void clean() {
        for (var position = 0; position != this.figures.length; position++) {
            this.figures[position] = null;
        }
        this.index = 0;
    }

    private int findBy(Square square) {
        int result = -1;
        for (var index = 0; index != this.figures.length; index++) {
            if (this.figures[index] != null && this.figures[index].position().equals(square)) {
                result = index;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
       return Arrays.toString(this.figures);
    }
}
