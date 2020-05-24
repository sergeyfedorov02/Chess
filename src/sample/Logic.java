package sample;

import javafx.util.Pair;
import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;
import sample.figures.black.*;
import sample.figures.white.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Logic {
    private GameStatus status = GameStatus.ok;
    private final ChessPosition chessPosition;
    private final ChessMoves moves;
    private boolean isWhiteTurn;

    public boolean isWhiteTurn() {
        return this.isWhiteTurn;
    }

    private void buildBlackTeam() {
        addFigure(new PawnBlack(Square.A7));
        addFigure(new PawnBlack(Square.B7));
        addFigure(new PawnBlack(Square.C7));
        addFigure(new PawnBlack(Square.D7));
        addFigure(new PawnBlack(Square.E7));
        addFigure(new PawnBlack(Square.F7));
        addFigure(new PawnBlack(Square.G7));
        addFigure(new PawnBlack(Square.H7));
        addFigure(new RookBlack(Square.A8));
        addFigure(new KnightBlack(Square.B8));
        addFigure(new BishopBlack(Square.C8));
        addFigure(new QueenBlack(Square.D8));
        addFigure(new KingBlack(Square.E8));
        addFigure(new BishopBlack(Square.F8));
        addFigure(new KnightBlack(Square.G8));
        addFigure(new RookBlack(Square.H8));
    }

    private void buildWhiteTeam() {
        addFigure(new PawnWhite(Square.A2));
        addFigure(new PawnWhite(Square.B2));
        addFigure(new PawnWhite(Square.C2));
        addFigure(new PawnWhite(Square.D2));
        addFigure(new PawnWhite(Square.E2));
        addFigure(new PawnWhite(Square.F2));
        addFigure(new PawnWhite(Square.G2));
        addFigure(new PawnWhite(Square.H2));
        addFigure(new RookWhite(Square.A1));
        addFigure(new KnightWhite(Square.B1));
        addFigure(new BishopWhite(Square.C1));
        addFigure(new QueenWhite(Square.D1));
        addFigure(new KingWhite(Square.E1));
        addFigure(new BishopWhite(Square.F1));
        addFigure(new KnightWhite(Square.G1));
        addFigure(new RookWhite(Square.H1));
    }

    private void addFigure(Figure figure) {
        this.chessPosition.add(figure);
    }

    private final List<Square> shortCastlingWhiteNoFigures = new ArrayList<>(List.of(Square.F1, Square.G1));
    private final List<Square> shortCastlingWhiteNoCheck = new ArrayList<>(List.of(Square.E1, Square.F1, Square.G1));
    private final List<Square> shortCastlingBlackNoFigures = new ArrayList<>(List.of(Square.F8, Square.G8));
    private final List<Square> shortCastlingBlackNoCheck = new ArrayList<>(List.of(Square.E8, Square.F8, Square.G8));

    private final List<Square> longCastlingWhiteNoFigures = new ArrayList<>(List.of(Square.B1, Square.C1, Square.D1));
    private final List<Square> longCastlingWhiteNoCheck = new ArrayList<>(List.of(Square.E1, Square.D1, Square.C1));
    private final List<Square> longCastlingBlackNoFigures = new ArrayList<>(List.of(Square.B8, Square.C8, Square.D8));
    private final List<Square> longCastlingBlackNoCheck = new ArrayList<>(List.of(Square.E8, Square.D8, Square.C8));

    private boolean noFigures(List<Square> list) {
        return list.stream().allMatch(item -> getFigure(item) == null);
    }

    /**
     * Метод для рокировки, который возвращает куда и какую ладью надо переместить
    */
    private Pair<Figure,Square> getRookMoveInCastling(
            List<Square> noFiguresSquares, List<Square> noCheckSquares,
            boolean isWhite, Square rookStart, Square rookEnd) {
        //на позициях между королем и ладьей не должно быть фигур
        if (!noFigures(noFiguresSquares)) {
            return null;
        }

        var figureStart = getFigure(rookStart);

        // На позиции, с которой происходит рокирвока, должна быть ладья
        if (figureStart == null || figureStart.isWhite() != isWhite || figureStart.getKind() != FigureKind.Rook) {
            return null;
        }

        //Ладья не должна была двигаться до совершения с ней рокировки
        var isShortCastling = noFiguresSquares.size() == 2;

        if (moves.isRookMoved(isWhite, !isShortCastling)) {
            return null;
        }

        //Клетки через которые проходит король во время рокировки не должны находиться под шахом
        if (isAnySquareUnderControl(!isWhite, noCheckSquares)) {
            return null;
        }

        return new Pair(figureStart, rookEnd);
    }

    private Pair<Figure,Square> getCastling(Figure figure, Square end) {
         // Во время рокировки взятая фигура должна быть королем

         if (figure.getKind() != FigureKind.King ) {
             return null;
         }

         // король должен находиться на начальной позиции
         if (isWhiteTurn) {
             if (figure.position() != Square.E1) {
                 return null;
             }
         }
         else {
             if (figure.position() != Square.E8) {
                 return null;
             }
         }

         // Рокирвока невозможна, если король до этого двигался
         if (moves.isKingMoved(isWhiteTurn)) {
             return null;
         }

         if (isWhiteTurn) {

             if (end == Square.G1) {
                 return getRookMoveInCastling(shortCastlingWhiteNoFigures,
                         shortCastlingWhiteNoCheck, true, Square.H1, Square.F1);
             }
             else if (end == Square.C1) {
                 return getRookMoveInCastling(longCastlingWhiteNoFigures,
                         longCastlingWhiteNoCheck, true, Square.A1, Square.D1);
             }

         }
         else {
             if (end == Square.G8) {
                 return getRookMoveInCastling(shortCastlingBlackNoFigures,
                         shortCastlingBlackNoCheck, false, Square.H8, Square.F8);
             }
             else if (end == Square.C8) {
                 return getRookMoveInCastling(longCastlingBlackNoFigures,
                         longCastlingBlackNoCheck, false, Square.A8, Square.D8);
             }
         }

         return null;
    }

    private Figure getPawnToPassTake(Figure figure, Square end) {
        var takeSquare = moves.getPawnSquareToTake();

        //Был ли последний ход соперника совершен пешкой на две клетки?
        if(takeSquare == null) {
            return null;
        }

        //Ходим ли мы сами пешкой?
        if(figure.getKind() != FigureKind.Pawn) {
            return null;
        }

        //Можем ли мы совершить ход со взятием на проходе?
        if (takeSquare != end) {
            return null;
        }

        var curSquare = figure.position();

        if (isWhiteTurn) {
            if (end == curSquare.getLeftUpSquare()) {
                return getFigure(curSquare.getLeftSquare());
            }

            if (end == curSquare.getRightUpSquare()) {
                return getFigure(curSquare.getRightSquare());
            }
        } else {
            if (end == curSquare.getLeftDownSquare()) {
                return getFigure(curSquare.getLeftSquare());
            }

            if (end == curSquare.getRightDownSquare()) {
                return getFigure(curSquare.getRightSquare());
            }
        }

        return null;
    }

    private final LogicInterface logicInterface;

    /**
     Конструктор для создания исходной позиции
     */
    public Logic(LogicInterface logicInterface) {
        this.logicInterface = logicInterface;
        this.moves = new ChessMoves();
        this.chessPosition= new ChessPosition();
        this.isWhiteTurn = true;
        buildBlackTeam();
        buildWhiteTeam();
    }

    /**
     Конструктор для тестов,чтобы начать с заданной позиции
     */
    Logic(LogicInterface logicInterface, ChessPosition position, ChessMoves moves, boolean isWhiteTurn) {
        this.logicInterface = logicInterface;
        this.chessPosition = position;
        this.moves = moves;
        this.isWhiteTurn = isWhiteTurn;
    }

    public boolean move(Square start, Square end) {

        if (status != GameStatus.ok) {
            //Ход сделать нельзя, так как игра завершена
            return false;
        }

        var figure = getFigure(start);

        if (figure == null) {
            // в начальной позиции нет фигуры. ход невозможен
            return false;
        }

        if (isWhiteTurn != figure.isWhite()) {
            // фигура не того цвета чей ход
            return false;
        }

        //  Это рокировка?
        var rookMoveAfterCastling = getCastling(figure, end);

        if (rookMoveAfterCastling != null) {
            var rookEndSquare = rookMoveAfterCastling.getValue();
            var isShortCastling = rookEndSquare == Square.H1 && isWhiteTurn ||
                    rookEndSquare == Square.H8 && !isWhiteTurn;

            // Перемещаем короля и ладью
            moves.addCasting(isWhiteTurn, isShortCastling);

            figure.move(end);
            rookMoveAfterCastling.getKey().move(rookEndSquare);
        }
        else {
            //Можно ли сделать ход со взятием на проходе?
            var pawnToPassTake = getPawnToPassTake(figure,end);

            Figure figureToTake = null;

            if (pawnToPassTake != null) {
                figureToTake = pawnToPassTake;

                // проверяем что после хода король не находится под шахом
                // если находится то ход невозможен
                if (kingUnderCheckAfterMove(isWhiteTurn, figure, end, figureToTake)) {
                    return false;
                }

            }
            else {
                if (!figure.canMoveTo(end, chessPosition)) {
                    return false;
                }

                // проверяем что после хода король не находится под шахом
                // если находится то ход невозможен
                if (kingUnderCheckAfterMove(isWhiteTurn, figure, end, null)) {
                    return false;
                }

                // ход возможен, делаем и передаем очередность хода другому цвету
                figureToTake = chessPosition.getFigure(end);
            }

            if (figureToTake != null) {
                // съели фигуру противника, невозможность съесть короля проверяем в canMoveTo у фигур
                chessPosition.removeFigure(figureToTake);
            }

            Figure newFigure = null;

            if (figure.getKind() == FigureKind.Pawn) {
                if (isWhiteTurn) {
                    if (end.y == 0) {
                        // превращение белой пешки
                        newFigure = createNewFigure(logicInterface.getFigureToReplacePawn(), true, end);
                    }

                } else {
                    if (end.y == 7) {
                        // превращение черной пешки
                        newFigure = createNewFigure(logicInterface.getFigureToReplacePawn(), false, end);
                    }
                }
            }

            moves.addMove(figure, end, figureToTake);

            if (newFigure != null) {
                // удаляем пешку и ставим новую фигуру на доску
                chessPosition.removeFigure(figure);
                chessPosition.add(newFigure);
            }
            else {
                // перемещаем фигуру, которой был сделан ход в новую позицию
                figure.move(end);
            }
        }

        isWhiteTurn = !isWhiteTurn;
        updateStatus();

        return true;

    }

    private Figure createNewFigure(FigureKind kind, boolean isWhite, Square square) {
        switch (kind) {
            case Rook:
                return isWhite ? new RookWhite(square) : new RookBlack(square);
            case Bishop:
                return isWhite ? new BishopWhite(square) : new BishopBlack(square);
            case Knight:
                return isWhite ? new KnightWhite(square) : new KnightBlack(square);
            default:
                return isWhite ? new QueenWhite(square) : new QueenBlack(square);
        }
    }

    public void clean() {
        isWhiteTurn = true;
        status = GameStatus.ok;
        chessPosition.clean();
        moves.clear();

        buildBlackTeam();
        buildWhiteTeam();
    }

    public List<Figure> getAllFigures() {
        return  Stream.of(this.chessPosition.getFigures(isWhiteTurn),
                this.chessPosition.getFigures(!isWhiteTurn))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public GameStatus getStatus(){
        return this.status;
    }

    private boolean canAnyFigureMoveWithNoCheckToKing() {
        // Пробуем найти хоть какой то ход после которого не будет шаха
        return chessPosition.getFigures(isWhiteTurn).stream().anyMatch(this::canFigureMoveWithNoCheckToKing);
    }


    private boolean canFigureMoveWithNoCheckToKing (Figure figure) {
        // перебираем все клетки доски и ищем первую клетку куда можно поставить фигуру и защититься от шаха
        return Arrays.stream(Square.getAllSquares()).anyMatch(item -> {
            // можем ли защититься от шаха взятием на проходе
            var pawnToPassTake = getPawnToPassTake(figure,item);

            if (pawnToPassTake == null) {
                // не можем защититься взятием на проходе, проверяем остальные ходы
                if (!figure.canMoveTo(item, chessPosition)) {
                    return false;
                }
            }

            return !kingUnderCheckAfterMove(isWhiteTurn, figure, item, pawnToPassTake);
        });
    }

    private boolean isAnySquareUnderControl(boolean isWhite, List<Square> squares) {
        if (squares == null || squares.size() == 0) {
            return false;
        }

        var allFigures = chessPosition.getFigures(isWhite);

        return allFigures.stream().anyMatch(item -> squares.stream()
                .anyMatch(pos -> item.controlSquare(pos, chessPosition)));
    }

    private boolean isKingUnderCheck() {
        var kingPosition = chessPosition.getKing(isWhiteTurn).position();

        return isAnySquareUnderControl(!isWhiteTurn, new ArrayList<>(List.of(kingPosition)));
    }

    private void updateStatus()  {
        status = GameStatus.ok;

        if (isKingUnderCheck()) {
            // король под шахом, проверяем поставили ли мат. мат будет если ни одна фигура не может сделать
            // допустимый ход, который приводит к позиции без шаха
            if (!canAnyFigureMoveWithNoCheckToKing()) {
                status = GameStatus.checkmate;
            }
        }
        else {
            //Проверяем. не осталось ли на доске только 2 короля(Ничья)
            if (getAllFigures().size() == 2) {
                status = GameStatus.draw;
            }
            // проверяем нет ли пата. если ни одна фигура не может сделать допустимый ход, то это пат
            if (!canAnyFigureMoveWithNoCheckToKing()) {
                status = GameStatus.stalemate;
            }
        }
    }

    private boolean kingUnderCheckAfterMove(boolean isWhiteKing, Figure figure, Square moveTo, Figure pawnToTake) {
        // создаем копию позиции и делаем ход
        var copyPosition = this.chessPosition.copy();
        var curPosition = figure.position();
        var copyFigure = copyPosition.getFigure(curPosition);

        if (pawnToTake != null) {
            var pawnPosition = pawnToTake.position();
            copyPosition.clearSquare(pawnPosition);
        }
        copyPosition.clearSquare(moveTo);
        copyFigure.move(moveTo);

        // проверяем что нет шаха королю указанного цвета
        var king = copyPosition.getKing(isWhiteKing);

        var allFiguresOpponent = copyPosition.getFigures(!isWhiteKing);

        return allFiguresOpponent.stream().anyMatch(item -> item.controlSquare(king.position(), copyPosition));
    }


    ChessMoves getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "Logic{" +
                "position=" + chessPosition.toString() +
                '}';
    }

    public Figure getFigure(Square position) {
        return this.chessPosition.getFigure(position);
    }
}
