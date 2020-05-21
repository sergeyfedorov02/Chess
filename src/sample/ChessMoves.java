package sample;

import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

public class ChessMoves {
    private boolean whiteKingMoved;
    private boolean blackKingMoved;
    private boolean whiteRookA1Moved;
    private boolean whiteRookH1Moved;
    private boolean blackRookA8Moved;
    private boolean blackRookH8Moved;
    private  Square pawnSquareToTake; // поле для взятия на проходе


    /**
     Сбросить все ходы (начата новая партия)
     */
    public void clear() {
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteRookA1Moved = false;
        whiteRookH1Moved = false;
        blackRookA8Moved = false;
        blackRookH8Moved = false;
        pawnSquareToTake = null;
    }

    /**
     Получить поле на котором можно взять на проходе
     */
    public Square getPawnSquareToTake() {
        return pawnSquareToTake;
    }

    /**
     Ходил король указанного цвета или нет
     */
    public boolean isKingMoved(boolean isWhite) {
        return isWhite ? whiteKingMoved : blackKingMoved;
    }

    /**
     Ходила ладья указанного цвета или нет (isA = true если ладья по вртикали A, иначе по вертикали H)
     */
    public boolean isRookMoved(boolean isWhite, boolean isA) {
        if (isWhite) {
            return isA ? whiteRookA1Moved : whiteRookH1Moved;
        }

        return isA ? blackRookA8Moved : blackRookH8Moved;
    }

    /**
     Сделана рокировка
     */
    public void addCasting(boolean isWhite, boolean isShort) {
         // рокировка. больше одного раза рокироваться нельзя.
        // запоминаем что король и ладья сделали ход
         pawnSquareToTake = null;

         if (isWhite) {
             whiteKingMoved = true;
             if (isShort) {
                 whiteRookH1Moved = true;
             } else {
                 whiteRookA1Moved = true;
             }
         } else {
             blackKingMoved = true;
             if (isShort) {
                 blackRookH8Moved = true;
             } else {
                 blackRookA8Moved = true;
             }
         }
    }

    /**
     Сделан очередной ход (removeFigure != null если при этом была съедена фигура)
     */
    public void addMove(Figure figure, Square square, Figure removeFigure) {
        pawnSquareToTake = null;

        if(removeFigure != null && removeFigure.getKind() == FigureKind.Rook) {
            // Если съедаем ладью, то рокировка невозможна в эту сторону даже если там будет другая ладья
            if (removeFigure.isWhite()) {
                if (removeFigure.position() == Square.A1) {
                    whiteRookA1Moved = true;
                } else if(removeFigure.position() == Square.H1) {
                    whiteRookH1Moved = true;
                }
            }
            else {
                if (removeFigure.position() == Square.A8) {
                    blackRookA8Moved = true;
                } else if(removeFigure.position() == Square.H8) {
                    blackRookH8Moved = true;
                }
            }
        }

        if (figure.getKind() == FigureKind.Pawn) {
            // Если пешка пошла на две клетки, то запоминаем поле через которое
            // она перескочила. На следующем ходе на это поле можно сделать взятие на проходе
            if (figure.isWhite()) {
                if (figure.position().y == 6 && square.y == 4) {
                    pawnSquareToTake = square.getDownSquare();
                }
            } else {
                if (figure.position().y == 1 && square.y == 3) {
                    pawnSquareToTake = square.getUpSquare();
                }
            }
        }
        else if (figure.getKind() == FigureKind.King) {
            // Если король сделал ход,то рокировка невозможна в обе стороны
            if (figure.isWhite()) {
                whiteKingMoved = true;
            }
            else {
                blackKingMoved = true;
            }
        }
        else if (figure.getKind() == FigureKind.Rook) {
            // Если ладья сделала ход,то рокировка с ней невозможна
            if (figure.isWhite()) {
                if (square == Square.H1) {
                    whiteRookH1Moved = true;
                } else if(square == Square.A1) {
                    whiteRookA1Moved = true;
                }
            } else {
                if (square == Square.H8) {
                    blackRookH8Moved = true;
                } else if(square == Square.A1) {
                    blackRookA8Moved = true;
                }
            }
        }
    }
}
