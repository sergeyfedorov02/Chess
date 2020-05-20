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


    public void clear() {
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteRookA1Moved = false;
        whiteRookH1Moved = false;
        blackRookA8Moved = false;
        blackRookH8Moved = false;
        pawnSquareToTake = null;
    }

    public Square getPawnSquareToTake() {
        return pawnSquareToTake;
    }

    public boolean isKingMoved(boolean isWhite) {
        return isWhite ? whiteKingMoved : blackKingMoved;
    }

    public boolean isRookMoved(boolean isWhite, boolean isA) {
        if (isWhite) {
            return isA ? whiteRookA1Moved : whiteRookH1Moved;
        }

        return isA ? blackRookA8Moved : blackRookH8Moved;
    }

    public void addCasting(boolean isWhite, boolean isShort) {
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

    public void addMove(Figure figure, Square square) {
        pawnSquareToTake = null;

        if (figure.getKind() == FigureKind.Pawn) {
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
            if (figure.isWhite()) {
                whiteKingMoved = true;
            }
            else {
                blackKingMoved = true;
            }
        }
        else if (figure.getKind() == FigureKind.Rook) {
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
