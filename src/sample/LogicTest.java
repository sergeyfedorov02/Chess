package sample;

import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.figures.FigureKind;
import sample.figures.Square;
import sample.figures.black.*;
import sample.figures.white.*;

class LogicTest {

    private LogicInterface createDefaultMock() {
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Queen);
        return li;
    }

    @Test
    void initialPosition() {

        var logic = new Logic(createDefaultMock());
        Assertions.assertTrue(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);

        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 32);
        var moves = logic.getMoves();
        Assertions.assertFalse(moves.isKingMoved(true));
        Assertions.assertFalse(moves.isKingMoved(false));
    }

    @Test
    void validPawnMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.A8));
        position.add(new KingWhite(Square.H1));
        position.add(new PawnBlack(Square.E7));
        position.add(new PawnBlack(Square.G7));
        position.add(new PawnWhite(Square.D2));
        position.add(new PawnWhite(Square.B2));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим, что пешка может ходить только вперед на 1 клетку и на 2, если находиться на начальной позиции
        Assertions.assertFalse(logic.move(Square.B2, Square.C2));//для белой
        Assertions.assertFalse(logic.move(Square.B2, Square.A2));
        Assertions.assertFalse(logic.move(Square.B2, Square.A1));
        Assertions.assertFalse(logic.move(Square.B2, Square.B1));
        Assertions.assertFalse(logic.move(Square.B2, Square.C1));
        Assertions.assertFalse(logic.move(Square.B2, Square.A3));
        Assertions.assertFalse(logic.move(Square.B2, Square.C3));
        Assertions.assertTrue(logic.move(Square.D2, Square.D3));//Вперед на одну

        //Для черной пешки
        Assertions.assertFalse(logic.move(Square.G7, Square.F8));
        Assertions.assertFalse(logic.move(Square.G7, Square.G8));
        Assertions.assertFalse(logic.move(Square.G7, Square.H8));
        Assertions.assertFalse(logic.move(Square.G7, Square.F7));
        Assertions.assertFalse(logic.move(Square.G7, Square.H7));
        Assertions.assertFalse(logic.move(Square.G7, Square.F6));
        Assertions.assertFalse(logic.move(Square.G7, Square.H6));
        Assertions.assertTrue(logic.move(Square.E7, Square.E6));//Вперед на одну

        //Попытаемся пойти на 2 клетки вперед
        Assertions.assertFalse(logic.move(Square.D3, Square.D5));//для белой не из начальной позиции
        Assertions.assertTrue(logic.move(Square.B2, Square.B4));//из начальной
        Assertions.assertFalse(logic.move(Square.E6, Square.E4));//теперь для черной
        Assertions.assertTrue(logic.move(Square.G7, Square.G5));

        //Проверим возможность взятия фигуры соперника,которая стоит на 1 клетку впереди наискосок(влево и врпаво)
        position.add(new PawnBlack(Square.B5));
        position.add(new PawnBlack(Square.C5));
        position.add(new PawnBlack(Square.A4));
        position.add(new PawnBlack(Square.C4));
        position.add(new PawnBlack(Square.A3));
        position.add(new PawnBlack(Square.B3));
        position.add(new PawnBlack(Square.C3));
        position.add(new PawnBlack(Square.B6));

        position.add(new PawnWhite(Square.F4));
        position.add(new PawnWhite(Square.G4));
        position.add(new PawnWhite(Square.F5));
        position.add(new PawnWhite(Square.H5));
        position.add(new PawnWhite(Square.F6));
        position.add(new PawnWhite(Square.G6));
        position.add(new PawnWhite(Square.H6));
        position.add(new PawnWhite(Square.G3));

        Assertions.assertFalse(logic.move(Square.B4, Square.B5));//для белой
        Assertions.assertFalse(logic.move(Square.B4, Square.A4));
        Assertions.assertFalse(logic.move(Square.B4, Square.C4));
        Assertions.assertFalse(logic.move(Square.B4, Square.A3));
        Assertions.assertFalse(logic.move(Square.B4, Square.B3));
        Assertions.assertFalse(logic.move(Square.B4, Square.C3));
        Assertions.assertFalse(logic.move(Square.B4, Square.A5));//не можем походить налево вверх, так как клетка пуста
        Assertions.assertTrue(logic.move(Square.B4, Square.C5));//можем взять пешку соперника справа сверху

        Assertions.assertFalse(logic.move(Square.G5, Square.F6));//для черной
        Assertions.assertFalse(logic.move(Square.G5, Square.G6));
        Assertions.assertFalse(logic.move(Square.G5, Square.H6));
        Assertions.assertFalse(logic.move(Square.G5, Square.F5));
        Assertions.assertFalse(logic.move(Square.G5, Square.H5));
        Assertions.assertFalse(logic.move(Square.G5, Square.G4));
        Assertions.assertFalse(logic.move(Square.G5, Square.H4));//не можем походить направо вниз, так как клетка пуста
        Assertions.assertTrue(logic.move(Square.G5, Square.F4));//можем взять пешку соперника слева снизу

        //Теперь проверим вторые позиции для взятия фигуры
        Assertions.assertFalse(logic.move(Square.C5, Square.D6));//для белой справа сверху нельзя, так как клетка пуста
        Assertions.assertTrue(logic.move(Square.C5, Square.B6));//наверх влево можем
        Assertions.assertFalse(logic.move(Square.F4, Square.E3));//для черной снизу слева нельзя, так как клетка пуста
        Assertions.assertTrue(logic.move(Square.F4, Square.G3));//вниз и вправо можем

        //Проверим, что взятие было успешно(осталось 2 короля и 18 пешек)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 18);

        //Проверим невозможность хода вперед, если перед нам стоит пешка соперника
        position.add(new PawnBlack(Square.G7));
        position.add(new PawnBlack(Square.C3));
        position.add(new PawnWhite(Square.C2));
        position.add(new PawnWhite(Square.G6));

        Assertions.assertFalse(logic.move(Square.C2, Square.C4));// для белой на 2 клетки
        Assertions.assertFalse(logic.move(Square.C2, Square.C3));//на одну
        Assertions.assertTrue(logic.move(Square.H1, Square.G1));
        Assertions.assertFalse(logic.move(Square.G7, Square.G5));//для черной
        Assertions.assertFalse(logic.move(Square.G7, Square.G6));
    }

    @Test
    void validRookMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.H1));
        position.add(new RookBlack(Square.D4));
        position.add(new RookWhite(Square.E5));
        position.add(new PawnWhite(Square.D2));
        position.add(new PawnWhite(Square.D6));
        position.add(new PawnWhite(Square.B4));
        position.add(new PawnWhite(Square.F4));
        position.add(new PawnBlack(Square.E3));
        position.add(new PawnBlack(Square.E7));
        position.add(new PawnBlack(Square.C5));
        position.add(new PawnBlack(Square.G5));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможные ходы для белой ладьи(диагонали и через фигуры)
        Assertions.assertFalse(logic.move(Square.E5, Square.F6));
        Assertions.assertFalse(logic.move(Square.E5, Square.D6));
        Assertions.assertFalse(logic.move(Square.E5, Square.D4));
        Assertions.assertFalse(logic.move(Square.E5, Square.F4));
        Assertions.assertFalse(logic.move(Square.E5, Square.E8));
        Assertions.assertFalse(logic.move(Square.E5, Square.E1));
        Assertions.assertFalse(logic.move(Square.E5, Square.A5));
        Assertions.assertFalse(logic.move(Square.E5, Square.H5));
        Assertions.assertTrue(logic.move(Square.E5, Square.E6));//валидный ход вперед

        //Теперь невозможные для черной
        Assertions.assertFalse(logic.move(Square.D4, Square.E5));
        Assertions.assertFalse(logic.move(Square.D4, Square.C5));
        Assertions.assertFalse(logic.move(Square.D4, Square.E3));
        Assertions.assertFalse(logic.move(Square.D4, Square.C3));
        Assertions.assertFalse(logic.move(Square.D4, Square.D8));
        Assertions.assertFalse(logic.move(Square.D4, Square.D1));
        Assertions.assertFalse(logic.move(Square.D4, Square.A4));
        Assertions.assertFalse(logic.move(Square.D4, Square.H4));
        Assertions.assertTrue(logic.move(Square.D4, Square.D5));//валидный ход вперед

        //теперь проверим всевозможные ходы + взятие фигур соперника
        Assertions.assertTrue(logic.move(Square.E6, Square.E5));//вниз
        Assertions.assertTrue(logic.move(Square.D5, Square.D4));
        Assertions.assertTrue(logic.move(Square.E5, Square.F5));//вправо
        Assertions.assertTrue(logic.move(Square.D4, Square.E4));
        Assertions.assertTrue(logic.move(Square.F5, Square.D5));//влево
        Assertions.assertTrue(logic.move(Square.E4, Square.C4));
        Assertions.assertTrue(logic.move(Square.D5, Square.G5));//вправо со взятием
        Assertions.assertTrue(logic.move(Square.C4, Square.F4));
        Assertions.assertTrue(logic.move(Square.G5, Square.C5));//влево со взятием
        Assertions.assertTrue(logic.move(Square.F4, Square.B4));
        Assertions.assertTrue(logic.move(Square.C5, Square.E5));// возвращаемся
        Assertions.assertTrue(logic.move(Square.B4, Square.D4));
        Assertions.assertTrue(logic.move(Square.E5, Square.E7));// вверх со взятием
        Assertions.assertTrue(logic.move(Square.D4, Square.D6));
        Assertions.assertTrue(logic.move(Square.E7, Square.E3));// ввниз со взятием
        Assertions.assertTrue(logic.move(Square.D6, Square.D2));

        //Проверим, что взятие было успешно(осталось 2 короля и 2 ладьи)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);

    }

    @Test
    void validWhiteBishopMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.H7));
        position.add(new KingWhite(Square.H3));
        position.add(new BishopWhite(Square.E5));
        position.add(new PawnBlack(Square.G3));
        position.add(new PawnBlack(Square.G7));
        position.add(new PawnBlack(Square.C7));
        position.add(new PawnBlack(Square.C3));
        position.add(new RookBlack(Square.A8));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможные ходы для белого слона(вертикали/горизонтали и через фигуры)
        Assertions.assertFalse(logic.move(Square.E5, Square.F5));
        Assertions.assertFalse(logic.move(Square.E5, Square.D5));
        Assertions.assertFalse(logic.move(Square.E5, Square.E4));
        Assertions.assertFalse(logic.move(Square.E5, Square.E6));
        Assertions.assertFalse(logic.move(Square.E5, Square.H8));
        Assertions.assertFalse(logic.move(Square.E5, Square.H2));
        Assertions.assertFalse(logic.move(Square.E5, Square.B2));
        Assertions.assertFalse(logic.move(Square.E5, Square.B8));
        Assertions.assertTrue(logic.move(Square.E5, Square.F6));//валидный ход вправо вверх

        //Теперь будем проверять возможные ходы
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));//нейтральный ход ладье,чтобы передать очередность хода
        Assertions.assertTrue(logic.move(Square.F6, Square.E5));//валидный ход влево вниз
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.E5, Square.F4));//валидный ход вправо вниз
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.F4, Square.D6));//валидный ход влево вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));

        //Ходы со взятием
        Assertions.assertTrue(logic.move(Square.D6, Square.G3));//вправо вниз
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.G3, Square.C7));//влево вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.C7, Square.E5));//возвращаемся
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.E5, Square.G7));//вправо вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.G7, Square.C3));//влево вниз

        //Проверим, что взятие было успешно(осталось 2 короля, ладья и слон)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);

    }

    @Test
    void validBlackBishopMove() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.H6));
        position.add(new KingWhite(Square.H3));
        position.add(new BishopBlack(Square.D4));
        position.add(new PawnWhite(Square.F2));
        position.add(new PawnWhite(Square.F6));
        position.add(new PawnWhite(Square.B2));
        position.add(new PawnWhite(Square.B6));
        position.add(new RookWhite(Square.A8));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, false);

        //Проверим невозможные ходы для белого слона(вертикали/горизонтали и через фигуры)
        Assertions.assertFalse(logic.move(Square.D4, Square.C4));
        Assertions.assertFalse(logic.move(Square.D4, Square.E4));
        Assertions.assertFalse(logic.move(Square.D4, Square.D3));
        Assertions.assertFalse(logic.move(Square.D4, Square.D5));
        Assertions.assertFalse(logic.move(Square.D4, Square.G1));
        Assertions.assertFalse(logic.move(Square.D4, Square.G7));
        Assertions.assertFalse(logic.move(Square.D4, Square.A1));
        Assertions.assertFalse(logic.move(Square.D4, Square.A7));
        Assertions.assertTrue(logic.move(Square.D4, Square.E5));//валидный ход вправо вверх

        //Теперь будем проверять возможные ходы
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));//нейтральный ход ладье,чтобы передать очередность хода
        Assertions.assertTrue(logic.move(Square.E5, Square.D4));//валидный ход влево вниз
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.D4, Square.E3));//валидный ход вправо вниз
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.E3, Square.C5));//валидный ход влево вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));

        //Ходы со взятием
        Assertions.assertTrue(logic.move(Square.C5, Square.F2));//вправо вниз
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.F2, Square.B6));//влево вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.B6, Square.D4));//возвращаемся
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));
        Assertions.assertTrue(logic.move(Square.D4, Square.F6));//вправо вверх
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));
        Assertions.assertTrue(logic.move(Square.F6, Square.B2));//влево вниз

        //Проверим, что взятие было успешно(осталось 2 короля, ладья и слон)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);
    }

    @Test
    void validKnightMove() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.H7));
        position.add(new KingWhite(Square.H2));
        position.add(new KnightWhite(Square.D2));
        position.add(new KnightBlack(Square.D7));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможные ходы для белого коня(горизонтали/вертикали и диагонали)
        Assertions.assertFalse(logic.move(Square.D2, Square.C2));
        Assertions.assertFalse(logic.move(Square.D2, Square.E2));
        Assertions.assertFalse(logic.move(Square.D2, Square.D1));
        Assertions.assertFalse(logic.move(Square.D2, Square.D3));
        Assertions.assertFalse(logic.move(Square.D2, Square.E1));
        Assertions.assertFalse(logic.move(Square.D2, Square.E3));
        Assertions.assertFalse(logic.move(Square.D2, Square.C1));
        Assertions.assertFalse(logic.move(Square.D2, Square.C3));
        Assertions.assertTrue(logic.move(Square.H2, Square.H1));//Перадаем очередность хода

        //Для черного
        Assertions.assertFalse(logic.move(Square.D7, Square.C7));
        Assertions.assertFalse(logic.move(Square.D7, Square.E7));
        Assertions.assertFalse(logic.move(Square.D7, Square.D8));
        Assertions.assertFalse(logic.move(Square.D7, Square.D6));
        Assertions.assertFalse(logic.move(Square.D7, Square.E6));
        Assertions.assertFalse(logic.move(Square.D7, Square.E8));
        Assertions.assertFalse(logic.move(Square.D7, Square.C6));
        Assertions.assertFalse(logic.move(Square.D7, Square.C8));
        Assertions.assertTrue(logic.move(Square.H7, Square.H8));//Перадаем очередность хода

        //Проверим все возмодные ходы без взятия фигур соперника
        Assertions.assertTrue(logic.move(Square.D2, Square.E4));//белый вверх и вправо
        Assertions.assertTrue(logic.move(Square.D7, Square.E5));//черный вниз и вправо
        Assertions.assertTrue(logic.move(Square.E4, Square.D6));//белый вверх и влево
        Assertions.assertTrue(logic.move(Square.E5, Square.D3));//черный вниз и влево
        Assertions.assertTrue(logic.move(Square.D6, Square.E4));//белый вниз и вправо
        Assertions.assertTrue(logic.move(Square.D3, Square.E5));//черный вверх и вправо
        Assertions.assertTrue(logic.move(Square.E4, Square.D2));//белый вниз и влево
        Assertions.assertTrue(logic.move(Square.E5, Square.D7));//черный вверх и влево

        // Теперь по очереди(вправо вверх и возвращаемся влево вниз, а потом влево вверх ит обратно вправо вниз)
        Assertions.assertTrue(logic.move(Square.D2, Square.F3));//белый
        Assertions.assertTrue(logic.move(Square.D7, Square.F8));//черный
        Assertions.assertTrue(logic.move(Square.F3, Square.D2));//белый
        Assertions.assertTrue(logic.move(Square.F8, Square.D7));//черный
        Assertions.assertTrue(logic.move(Square.D2, Square.B3));//белый
        Assertions.assertTrue(logic.move(Square.D7, Square.B8));//черный
        Assertions.assertTrue(logic.move(Square.B3, Square.D2));//белый
        Assertions.assertTrue(logic.move(Square.B8, Square.D7));//черный

        //Случаи со взятием
        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.E4));
        position.add(new PawnWhite(Square.D3));
        position.add(new PawnBlack(Square.D6));
        Assertions.assertTrue(logic.move(Square.D2, Square.E4));
        Assertions.assertTrue(logic.move(Square.D7, Square.E5));
        Assertions.assertTrue(logic.move(Square.E4, Square.D6));
        Assertions.assertTrue(logic.move(Square.E5, Square.D3));

        position.add(new PawnWhite(Square.D7));
        position.add(new PawnBlack(Square.E4));
        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.D2));
        Assertions.assertTrue(logic.move(Square.D6, Square.E4));
        Assertions.assertTrue(logic.move(Square.D3, Square.E5));
        Assertions.assertTrue(logic.move(Square.E4, Square.D2));
        Assertions.assertTrue(logic.move(Square.E5, Square.D7));

        position.add(new PawnWhite(Square.F8));
        position.add(new PawnBlack(Square.F3));
        Assertions.assertTrue(logic.move(Square.D2, Square.F3));
        Assertions.assertTrue(logic.move(Square.D7, Square.F8));


        position.add(new PawnWhite(Square.D7));
        position.add(new PawnBlack(Square.D2));
        Assertions.assertTrue(logic.move(Square.F3, Square.D2));
        Assertions.assertTrue(logic.move(Square.F8, Square.D7));

        position.add(new PawnWhite(Square.B8));
        position.add(new PawnBlack(Square.B3));
        Assertions.assertTrue(logic.move(Square.D2, Square.B3));//белый
        Assertions.assertTrue(logic.move(Square.D7, Square.B8));//черный

        position.add(new PawnWhite(Square.D7));
        position.add(new PawnBlack(Square.D2));
        Assertions.assertTrue(logic.move(Square.B3, Square.D2));//белый
        Assertions.assertTrue(logic.move(Square.B8, Square.D7));//черный

        //Проверим, что взятие было успешно(осталось 2 короля и 2 коня)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);
    }

    @Test
    void isValidQueenMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.H1));
        position.add(new QueenWhite(Square.D4));
        position.add(new QueenBlack(Square.E5));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //рассмотри все возможные ходы (горизонтали/вертикали и диагонали)
        Assertions.assertTrue(logic.move(Square.D4, Square.B2));
        Assertions.assertTrue(logic.move(Square.E5, Square.G7));
        Assertions.assertTrue(logic.move(Square.B2, Square.D4));
        Assertions.assertTrue(logic.move(Square.G7, Square.E5));

        Assertions.assertTrue(logic.move(Square.D4, Square.D1));
        Assertions.assertTrue(logic.move(Square.E5, Square.E7));
        Assertions.assertTrue(logic.move(Square.D1, Square.D3));
        Assertions.assertTrue(logic.move(Square.E7, Square.E5));

        Assertions.assertTrue(logic.move(Square.D3, Square.B3));
        Assertions.assertTrue(logic.move(Square.E5, Square.G5));
        Assertions.assertTrue(logic.move(Square.B3, Square.D3));
        Assertions.assertTrue(logic.move(Square.G5, Square.E5));

        Assertions.assertTrue(logic.move(Square.D3, Square.B5));
        Assertions.assertTrue(logic.move(Square.E5, Square.C7));
        Assertions.assertTrue(logic.move(Square.B5, Square.D3));
        Assertions.assertTrue(logic.move(Square.C7, Square.E5));

        //Теперь со взятием и убедимся, что не можем передвигаться через фигуру
        position.add(new PawnWhite(Square.G7));
        position.add(new PawnBlack(Square.F5));
        Assertions.assertFalse(logic.move(Square.D3, Square.G6));
        Assertions.assertTrue(logic.move(Square.D3, Square.F5));
        Assertions.assertFalse(logic.move(Square.E5, Square.H8));
        Assertions.assertTrue(logic.move(Square.E5, Square.G7));

        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.D3));
        Assertions.assertTrue(logic.move(Square.F5, Square.D3));
        Assertions.assertTrue(logic.move(Square.G7, Square.E5));

        position.add(new PawnWhite(Square.E7));
        position.add(new PawnBlack(Square.D1));
        Assertions.assertTrue(logic.move(Square.D3, Square.D1));
        Assertions.assertTrue(logic.move(Square.E5, Square.E7));

        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.D3));
        Assertions.assertTrue(logic.move(Square.D1, Square.D3));
        Assertions.assertTrue(logic.move(Square.E7, Square.E5));

        position.add(new PawnWhite(Square.G5));
        position.add(new PawnBlack(Square.F3));
        Assertions.assertTrue(logic.move(Square.D3, Square.F3));
        Assertions.assertTrue(logic.move(Square.E5, Square.G5));

        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.D3));
        Assertions.assertTrue(logic.move(Square.F3, Square.D3));
        Assertions.assertTrue(logic.move(Square.G5, Square.E5));

        position.add(new PawnWhite(Square.C7));
        position.add(new PawnBlack(Square.B5));
        Assertions.assertTrue(logic.move(Square.D3, Square.B5));
        Assertions.assertTrue(logic.move(Square.E5, Square.C7));

        position.add(new PawnWhite(Square.E5));
        position.add(new PawnBlack(Square.D3));
        Assertions.assertTrue(logic.move(Square.B5, Square.D3));
        Assertions.assertTrue(logic.move(Square.C7, Square.E5));

        //Проверим, что взятие было успешно(осталось 2 короля и 2 ферзя)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);
    }

    @Test
    void validKingMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.F6));
        position.add(new KingWhite(Square.C3));
        position.add(new PawnWhite(Square.H2));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Возможность хода на 1 клетку во все стороны
        Assertions.assertTrue(logic.move(Square.C3, Square.D4));
        Assertions.assertTrue(logic.move(Square.F6, Square.G7));
        Assertions.assertTrue(logic.move(Square.D4, Square.C3));
        Assertions.assertTrue(logic.move(Square.G7, Square.F6));

        Assertions.assertTrue(logic.move(Square.C3, Square.C4));
        Assertions.assertTrue(logic.move(Square.F6, Square.F7));
        Assertions.assertTrue(logic.move(Square.C4, Square.C3));
        Assertions.assertTrue(logic.move(Square.F7, Square.F6));

        Assertions.assertTrue(logic.move(Square.C3, Square.D3));
        Assertions.assertTrue(logic.move(Square.F6, Square.G6));
        Assertions.assertTrue(logic.move(Square.D3, Square.C3));
        Assertions.assertTrue(logic.move(Square.G6, Square.F6));

        Assertions.assertTrue(logic.move(Square.C3, Square.B4));
        Assertions.assertTrue(logic.move(Square.F6, Square.E7));
        Assertions.assertTrue(logic.move(Square.B4, Square.C3));
        Assertions.assertTrue(logic.move(Square.E7, Square.F6));

        //Возможность взятия фигуры
        position.add(new PawnWhite(Square.G7));
        position.add(new PawnBlack(Square.D4));
        Assertions.assertTrue(logic.move(Square.C3, Square.D4));
        Assertions.assertTrue(logic.move(Square.F6, Square.G7));

        position.add(new PawnWhite(Square.F6));
        position.add(new PawnBlack(Square.C3));
        Assertions.assertTrue(logic.move(Square.D4, Square.C3));
        Assertions.assertTrue(logic.move(Square.G7, Square.F6));

        position.add(new PawnWhite(Square.F7));
        position.add(new PawnBlack(Square.C4));
        Assertions.assertTrue(logic.move(Square.C3, Square.C4));
        Assertions.assertTrue(logic.move(Square.F6, Square.F7));

        position.add(new PawnWhite(Square.F6));
        position.add(new PawnBlack(Square.C3));
        Assertions.assertTrue(logic.move(Square.C4, Square.C3));
        Assertions.assertTrue(logic.move(Square.F7, Square.F6));

        position.add(new PawnWhite(Square.G6));
        position.add(new PawnBlack(Square.D3));
        Assertions.assertTrue(logic.move(Square.C3, Square.D3));
        Assertions.assertTrue(logic.move(Square.F6, Square.G6));

        position.add(new PawnWhite(Square.F6));
        position.add(new PawnBlack(Square.C3));
        Assertions.assertTrue(logic.move(Square.D3, Square.C3));
        Assertions.assertTrue(logic.move(Square.G6, Square.F6));

        position.add(new PawnWhite(Square.E7));
        position.add(new PawnBlack(Square.B4));
        Assertions.assertTrue(logic.move(Square.C3, Square.B4));
        Assertions.assertTrue(logic.move(Square.F6, Square.E7));

        position.add(new PawnWhite(Square.F6));
        position.add(new PawnBlack(Square.C3));
        Assertions.assertTrue(logic.move(Square.B4, Square.C3));
        Assertions.assertTrue(logic.move(Square.E7, Square.F6));

        //Проверим, что взятие было успешно(осталось 2 короля и одна пешка, чтобы не было ничьи)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 3);
    }

    @Test
    void notValidKingMove() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.F6));
        position.add(new KingWhite(Square.C3));
        position.add(new PawnWhite(Square.H1));//добавляем пешку. чтобы игровой статус не стал "Ничья"

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Король не может ходить более чем на 1 клетку
        Assertions.assertFalse(logic.move(Square.C3, Square.E5));
        Assertions.assertFalse(logic.move(Square.C3, Square.E3));
        Assertions.assertFalse(logic.move(Square.C3, Square.E1));
        Assertions.assertFalse(logic.move(Square.C3, Square.C1));
        Assertions.assertFalse(logic.move(Square.C3, Square.A1));
        Assertions.assertFalse(logic.move(Square.C3, Square.A3));
        Assertions.assertFalse(logic.move(Square.C3, Square.A5));
        Assertions.assertFalse(logic.move(Square.C3, Square.C5));
        Assertions.assertTrue(logic.move(Square.C3, Square.C2));//передадим очередность

        // проверим для черного короля
        Assertions.assertFalse(logic.move(Square.F6, Square.H8));
        Assertions.assertFalse(logic.move(Square.F6, Square.H6));
        Assertions.assertFalse(logic.move(Square.F6, Square.H4));
        Assertions.assertFalse(logic.move(Square.F6, Square.F4));
        Assertions.assertFalse(logic.move(Square.F6, Square.D4));
        Assertions.assertFalse(logic.move(Square.F6, Square.D6));
        Assertions.assertFalse(logic.move(Square.F6, Square.D8));
        Assertions.assertFalse(logic.move(Square.F6, Square.F8));
        Assertions.assertTrue(logic.move(Square.F6, Square.F7));

        //Король не может ходить под шах и в даной ситуации спасение от шаха, это ход королем
        position.add(new QueenBlack(Square.E4));
        position.add(new RookBlack(Square.A1));
        position.add(new RookBlack(Square.B4));
        position.add(new KnightBlack(Square.B5));
        position.add(new QueenWhite(Square.H5));
        position.add(new RookWhite(Square.H8));
        position.add(new RookWhite(Square.C6));
        position.add(new PawnWhite(Square.H6));

        //Проверим невозможность для белого короля
        Assertions.assertFalse(logic.move(Square.C2, Square.D3));
        Assertions.assertFalse(logic.move(Square.C2, Square.C3));
        Assertions.assertFalse(logic.move(Square.C2, Square.B3));
        Assertions.assertFalse(logic.move(Square.C2, Square.B2));
        Assertions.assertFalse(logic.move(Square.C2, Square.B1));
        Assertions.assertFalse(logic.move(Square.C2, Square.C1));
        Assertions.assertFalse(logic.move(Square.C2, Square.D1));
        Assertions.assertTrue(logic.move(Square.C2, Square.D2));//сделаем идинственный доступный ход

        //Проверим невозможность для черного короля
        Assertions.assertFalse(logic.move(Square.F7, Square.G8));
        Assertions.assertFalse(logic.move(Square.F7, Square.F8));
        Assertions.assertFalse(logic.move(Square.F7, Square.E8));
        Assertions.assertFalse(logic.move(Square.F7, Square.G7));
        Assertions.assertFalse(logic.move(Square.F7, Square.G6));
        Assertions.assertFalse(logic.move(Square.F7, Square.F6));
        Assertions.assertFalse(logic.move(Square.F7, Square.E6));
        Assertions.assertTrue(logic.move(Square.F7, Square.E7));//сделаем единственный доступный ход
    }

    @Test
    void emptySquareOrIsWhiteTurn() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.E1));
        position.add(new QueenBlack(Square.A8));
        position.add(new KnightBlack(Square.B8));
        position.add(new QueenWhite(Square.A1));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);
        //Проверим, что ход невозможен, когда на клетке нет фигуры
        Assertions.assertFalse(logic.move(Square.C2, Square.C3));
        Assertions.assertFalse(logic.move(Square.C2, Square.D3));
        Assertions.assertFalse(logic.move(Square.C2, Square.D4));

        //Проверим, что ход невозможен. когда не наша очередь
        Assertions.assertTrue(logic.isWhiteTurn());
        Assertions.assertFalse(logic.move(Square.A8, Square.A7));
        Assertions.assertFalse(logic.move(Square.A8, Square.B7));
        Assertions.assertFalse(logic.move(Square.B8, Square.A6));

        //Проверим, что нельзя походить два раза за один тот же цвет фигур
        Assertions.assertTrue(logic.move(Square.A1, Square.A2));//Для белых
        Assertions.assertFalse(logic.move(Square.A2, Square.A3));
        Assertions.assertTrue(logic.move(Square.A8, Square.A7));//Для черных
        Assertions.assertFalse(logic.move(Square.A7, Square.A6));

    }

    @Test
    void cantTakeFriendFigure() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.G3));
        position.add(new KingWhite(Square.C3));
        position.add(new QueenBlack(Square.G4));
        position.add(new QueenWhite(Square.C4));

        //Добавим всезомжное белые фигуры
        position.add(new PawnWhite(Square.D5));
        position.add(new RookWhite(Square.B5));
        position.add(new BishopWhite(Square.C5));
        position.add(new KnightWhite(Square.D4));
        position.add(new QueenWhite(Square.B4));

        //Черный фигуры
        position.add(new PawnBlack(Square.F6));
        position.add(new RookBlack(Square.G6));
        position.add(new BishopBlack(Square.H6));
        position.add(new KnightBlack(Square.F5));
        position.add(new QueenBlack(Square.H5));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим, что нельзя взять фигуру того же цвета
        Assertions.assertFalse(logic.move(Square.C4, Square.C3));//Для белых
        Assertions.assertFalse(logic.move(Square.C4, Square.D4));
        Assertions.assertFalse(logic.move(Square.C4, Square.B4));
        Assertions.assertFalse(logic.move(Square.C4, Square.D5));
        Assertions.assertFalse(logic.move(Square.C4, Square.B5));
        Assertions.assertFalse(logic.move(Square.C4, Square.C5));
        Assertions.assertTrue(logic.move(Square.C3, Square.B3));//передадим очередность хода

        Assertions.assertFalse(logic.move(Square.G4, Square.F6));//Для черных
        Assertions.assertFalse(logic.move(Square.G4, Square.G6));
        Assertions.assertFalse(logic.move(Square.G4, Square.H6));
        Assertions.assertFalse(logic.move(Square.G4, Square.F5));
        Assertions.assertFalse(logic.move(Square.G4, Square.H5));
        Assertions.assertFalse(logic.move(Square.G4, Square.G3));

        //Проверим, что не было успешного взятия(остались все фигуры)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 14);
    }

    @Test
    void cantTakeKing() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.A8));
        position.add(new KingWhite(Square.A1));
        position.add(new QueenWhite(Square.B7));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим, что нельязя сделать взятие короля соперника
        Assertions.assertFalse(logic.move(Square.B7, Square.A8));//Взять черного короля белым ферзем
        Assertions.assertTrue(logic.move(Square.B7, Square.H7));//уберем фигуру. чтобы не было шаха

        position.add(new QueenBlack(Square.B2));
        Assertions.assertFalse(logic.move(Square.B2, Square.A1));//Взять белого короля черным ферзем
        Assertions.assertTrue(logic.move(Square.B2, Square.H2));

        //Проверим, что не было успешного взятия(остались все фигуры)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 4);
    }

    @Test
    void cantMoveWhenKingUnderCheck() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.H1));

        position.add(new QueenWhite(Square.D1));
        position.add(new PawnWhite(Square.E1));
        position.add(new BishopWhite(Square.C1));
        position.add(new KnightWhite(Square.B1));
        position.add(new RookWhite(Square.A1));
        position.add(new PawnWhite(Square.G7));

        position.add(new QueenBlack(Square.D8));
        position.add(new PawnBlack(Square.E8));
        position.add(new BishopBlack(Square.C8));
        position.add(new KnightBlack(Square.B8));
        position.add(new RookBlack(Square.A8));
        position.add(new PawnBlack(Square.G2));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим, что ни одна фигура не может походить, когда король под шахом
        Assertions.assertFalse(logic.move(Square.A1, Square.A3));//белый король код шахом
        Assertions.assertFalse(logic.move(Square.B1, Square.C3));
        Assertions.assertFalse(logic.move(Square.C1, Square.E3));
        Assertions.assertFalse(logic.move(Square.E1, Square.E2));
        Assertions.assertFalse(logic.move(Square.D1, Square.D4));
        Assertions.assertTrue(logic.move(Square.H1, Square.G2));//ухожим из под шаха, путем взятия королем пешки, которая дает шах

        Assertions.assertFalse(logic.move(Square.A8, Square.A7));//черный король код шахом
        Assertions.assertFalse(logic.move(Square.B8, Square.C6));
        Assertions.assertFalse(logic.move(Square.C8, Square.E6));
        Assertions.assertFalse(logic.move(Square.D8, Square.D5));
        Assertions.assertFalse(logic.move(Square.E8, Square.E7));
        Assertions.assertTrue(logic.move(Square.H8, Square.G7));//ухожим из под шаха

        //Проверим, что было успешное взятие пешек(осталось по одной фигуре каждого типа и цвета)
        var figures = logic.getAllFigures();
        Assertions.assertEquals(figures.size(), 12);
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);//Проверим, что сейчас можно играть дальше
    }

    @Test
    void cantMoveToWhiteKingUnderCheck() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.D3));

        position.add(new QueenWhite(Square.C4));
        position.add(new PawnWhite(Square.D4));
        position.add(new BishopWhite(Square.E4));
        position.add(new KnightWhite(Square.E3));
        position.add(new RookWhite(Square.C3));
        position.add(new PawnWhite(Square.G7));

        position.add(new PawnBlack(Square.E5));
        position.add(new BishopBlack(Square.F5));
        position.add(new BishopBlack(Square.B5));
        position.add(new BishopBlack(Square.C1));
        position.add(new RookBlack(Square.D6));
        position.add(new RookBlack(Square.A3));
        position.add(new RookBlack(Square.G3));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим, что мы не можем походить фигурами так, что король после хода будет под шахом
        Assertions.assertFalse(logic.move(Square.D4, Square.E5));// Король будет под ударом ладьи на D6
        Assertions.assertFalse(logic.move(Square.C3, Square.C2));//ладьи на A3
        Assertions.assertFalse(logic.move(Square.E3, Square.F1));//ладьи на G3
        Assertions.assertFalse(logic.move(Square.E4, Square.F3));//Слона на F5
        Assertions.assertFalse(logic.move(Square.C4, Square.B3));//Слона на B5
        Assertions.assertFalse(logic.move(Square.D3, Square.D2));//Слона на C1

        //проверим, что сейчас нет пата/мата
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);
        Assertions.assertTrue(logic.move(Square.D3, Square.E2));//совершим допустимый ход
    }

    @Test
    void cantMoveToBlackKingUnderCheck() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.C4));
        position.add(new KingWhite(Square.H8));

        position.add(new QueenBlack(Square.B3));
        position.add(new PawnBlack(Square.C3));
        position.add(new BishopBlack(Square.D3));
        position.add(new KnightBlack(Square.D4));
        position.add(new RookBlack(Square.B4));

        position.add(new PawnWhite(Square.D2));
        position.add(new BishopWhite(Square.D6));
        position.add(new BishopWhite(Square.E2));
        position.add(new BishopWhite(Square.A2));
        position.add(new RookWhite(Square.A4));
        position.add(new RookWhite(Square.F4));
        position.add(new RookWhite(Square.C1));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, false);

        //Проверим, что мы не можем походить фигурами так, что король после хода будет под шахом
        Assertions.assertFalse(logic.move(Square.B3, Square.C2));// Король будет под ударом слона на A2
        Assertions.assertFalse(logic.move(Square.C3, Square.D2));//ладьи на C1
        Assertions.assertFalse(logic.move(Square.D3, Square.E4));//слона на E2
        Assertions.assertFalse(logic.move(Square.D4, Square.C6));//ладьи на F4
        Assertions.assertFalse(logic.move(Square.B4, Square.B5));//ладьи на A4
        Assertions.assertFalse(logic.move(Square.C4, Square.C5));//Слона на D6

        //проверим, что сейчас нет пата/мата
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);
        Assertions.assertTrue(logic.move(Square.C4, Square.D5));//совершим допустимый ход

    }

    @Test
    void quickCheckMate() {

        var logic = new Logic(createDefaultMock());
        Assertions.assertTrue(logic.move(Square.F2, Square.F4));
        Assertions.assertTrue(logic.move(Square.E7, Square.E5));
        Assertions.assertTrue(logic.move(Square.G2, Square.G4));
        Assertions.assertTrue(logic.move(Square.D8, Square.H4));
        Assertions.assertTrue(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
    }

    @Test
    void shortCastling() {
        var logic = new Logic(createDefaultMock());

        //Рокировка невозможна(есть фигуры между королем и ладьей)
        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertFalse(logic.move(Square.E8, Square.G8)); // для черных

        //теперь будем убирать фигуры между и проверять возможность рокировки
        //Когда только слоны
        Assertions.assertTrue(logic.move(Square.E2, Square.E4));
        Assertions.assertTrue(logic.move(Square.E7, Square.E5));
        Assertions.assertTrue(logic.move(Square.G1, Square.F3));//убираем белого коня
        Assertions.assertTrue(logic.move(Square.G8, Square.F6));//убираем черного коня
        Assertions.assertFalse(logic.move(Square.E1, Square.G1));//проверяем невозможность для белых
        Assertions.assertTrue(logic.move(Square.A2, Square.A3));//нейтральныый ход пешкой
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));//проверяем невозможность для черных

        //Проверим случай, когда только кони(уберем слонов и добавим коней)
        Assertions.assertTrue(logic.move(Square.F8, Square.C5));//убираем черного слона
        Assertions.assertTrue(logic.move(Square.F1, Square.C4));//убираем белого слона
        Assertions.assertTrue(logic.move(Square.F6, Square.G8));
        Assertions.assertTrue(logic.move(Square.F3, Square.G1));
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));
        Assertions.assertTrue(logic.move(Square.A7, Square.A6));
        Assertions.assertFalse(logic.move(Square.E1, Square.G1));

        //уберем все фигуры между и првоерим возможость рокировки
        Assertions.assertTrue(logic.move(Square.G1, Square.F3));
        Assertions.assertTrue(logic.move(Square.G8, Square.F6));
        Assertions.assertTrue(logic.move(Square.E1, Square.G1)); // рокировка белых
        Assertions.assertTrue(logic.move(Square.E8, Square.G8)); // рокировка черных

        Assertions.assertTrue(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);

        //Проверим позиции ладьи и короля после рокировки
        var kingWhite = logic.getFigure(Square.G1);
        Assertions.assertEquals(kingWhite.getKind(), FigureKind.King);
        Assertions.assertTrue(kingWhite.isWhite());

        var kingBlack = logic.getFigure(Square.G8);
        Assertions.assertEquals(kingBlack.getKind(), FigureKind.King);
        Assertions.assertFalse(kingBlack.isWhite());

        var rookWhite = logic.getFigure(Square.F1);
        Assertions.assertEquals(rookWhite.getKind(), FigureKind.Rook);
        Assertions.assertTrue(rookWhite.isWhite());

        var rookBlack = logic.getFigure(Square.F8);
        Assertions.assertEquals(rookBlack.getKind(), FigureKind.Rook);
        Assertions.assertFalse(rookBlack.isWhite());
    }

    @Test
    void shortCastlingUnderCheck() {

        // На доске 2 короля, 2 ладьи и 2 ферзя и 2 слона
        var position = new ChessPosition();
        position.add(new BishopWhite(Square.D1));
        position.add(new BishopBlack(Square.D8));
        position.add(new KingBlack(Square.E8));
        position.add(new KingWhite(Square.E1));
        position.add(new RookBlack(Square.H8));
        position.add(new RookWhite(Square.H1));
        position.add(new QueenWhite(Square.E5));
        position.add(new QueenBlack(Square.E4));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможность рокировки, если король находится под шахом
        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertTrue(logic.move(Square.D1, Square.E2)); //передадим очередность хода черным, защитившись от шаха
        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertFalse(logic.move(Square.E8, Square.G8)); //для черных
        Assertions.assertTrue(logic.move(Square.D8, Square.E7));

        //Проверим невозможность рокировки через поля под шахом(сначала G1/G8, потом F1/F8)
        Assertions.assertTrue(logic.move(Square.E5, Square.G5));
        Assertions.assertTrue(logic.move(Square.E4, Square.G4));

        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertTrue(logic.move(Square.E2, Square.D3));
        Assertions.assertFalse(logic.move(Square.E8, Square.G8)); //для черных

        //Поменяем позиции ферзей и проверим невозможность рокировки
        Assertions.assertTrue(logic.move(Square.G4, Square.F4));
        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertTrue(logic.move(Square.G5, Square.F5));
        Assertions.assertFalse(logic.move(Square.E8, Square.G8)); //для черных

        //Переместим ферзей, чтобы они контролировали поля, на которых стоят ладьи и проверим возможость рокировки
        Assertions.assertTrue(logic.move(Square.F4, Square.H6));
        Assertions.assertTrue(logic.move(Square.E1, Square.G1));// для белых
        Assertions.assertTrue(logic.move(Square.H6, Square.F4));
        Assertions.assertTrue(logic.move(Square.F5, Square.H3));
        Assertions.assertTrue(logic.move(Square.E8, Square.G8));// для черных

        //Проверим, что зафиксировалось движение короля
        Assertions.assertTrue(moves.isKingMoved(true));//белого цвета
        Assertions.assertTrue(moves.isKingMoved(false));//черного цвета
    }

    @Test
    void castlingAfterShortCastling() {

        // На доске 2 короля, 4 ладьи и 2 пешки
        var position = new ChessPosition();
        position.add(new KingBlack(Square.E8));
        position.add(new KingWhite(Square.E1));
        position.add(new RookBlack(Square.H8));
        position.add(new RookWhite(Square.H1));
        position.add(new RookBlack(Square.A8));
        position.add(new RookWhite(Square.A1));
        position.add(new PawnWhite(Square.H3));
        position.add(new PawnBlack(Square.A6));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Совершим короткую рокировку и попытаемся сделать рокировку ещё раз
        Assertions.assertTrue(logic.move(Square.E1, Square.G1));//Рокировка белых
        Assertions.assertTrue(logic.move(Square.A6, Square.A5));
        Assertions.assertTrue(logic.move(Square.F1, Square.D1));//Убираем ладью, что совершить рокировку черных
        Assertions.assertTrue(logic.move(Square.E8, Square.G8));//черных

        //Вернем ладьи на линию H
        Assertions.assertTrue(logic.move(Square.D1, Square.D2));
        Assertions.assertTrue(logic.move(Square.F8, Square.F7));
        Assertions.assertTrue(logic.move(Square.D2, Square.H2));
        Assertions.assertTrue(logic.move(Square.F7, Square.H7));
        Assertions.assertTrue(logic.move(Square.H2, Square.H1));
        Assertions.assertTrue(logic.move(Square.H7, Square.H8));

        //Вернем королей на линию E
        Assertions.assertTrue(logic.move(Square.G1, Square.F1));
        Assertions.assertTrue(logic.move(Square.G8, Square.F8));
        Assertions.assertTrue(logic.move(Square.F1, Square.E1));
        Assertions.assertTrue(logic.move(Square.F8, Square.E8));

        //Проверим невозможность повторной рокировки
        Assertions.assertFalse(logic.move(Square.E1, Square.G1));//длинной для белых
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//короткой для белых
        Assertions.assertTrue(logic.move(Square.H3, Square.H4));//передадим очередность хода черным
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));//для черных
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));
    }

    @Test
    void castlingAfterLongCastling() {

        // На доске 2 короля, 4 ладьи и 2 пешки
        var position = new ChessPosition();
        position.add(new KingWhite(Square.E1));
        position.add(new KingBlack(Square.E8));
        position.add(new RookWhite(Square.H1));
        position.add(new RookBlack(Square.H8));
        position.add(new RookBlack(Square.A8));
        position.add(new PawnBlack(Square.A6));
        position.add(new RookWhite(Square.A1));
        position.add(new PawnWhite(Square.H3));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Совершим длинную рокировку и попытаемся сделать рокировку ещё раз
        Assertions.assertTrue(logic.move(Square.E1, Square.C1));//Рокировка белых
        Assertions.assertTrue(logic.move(Square.A6, Square.A5));
        Assertions.assertTrue(logic.move(Square.D1, Square.F1));//Убираем ладью, что совершить рокировку черных
        Assertions.assertTrue(logic.move(Square.E8, Square.C8));//черных

        //Вернем ладьи на линию A
        Assertions.assertTrue(logic.move(Square.F1, Square.F2));
        Assertions.assertTrue(logic.move(Square.D8, Square.D7));
        Assertions.assertTrue(logic.move(Square.F2, Square.A2));
        Assertions.assertTrue(logic.move(Square.D7, Square.A7));
        Assertions.assertTrue(logic.move(Square.A2, Square.A1));
        Assertions.assertTrue(logic.move(Square.A7, Square.A8));

        //Вернем королей на линию E
        Assertions.assertTrue(logic.move(Square.C1, Square.D1));
        Assertions.assertTrue(logic.move(Square.C8, Square.D8));
        Assertions.assertTrue(logic.move(Square.D1, Square.E1));
        Assertions.assertTrue(logic.move(Square.D8, Square.E8));

        //Проверим невозможность повторной рокировки
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//короткой для белых
        Assertions.assertFalse(logic.move(Square.E1, Square.G1));//длинной для белых
        Assertions.assertTrue(logic.move(Square.H3, Square.H4));//передадим очередность хода черным
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));//для черных
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));
    }

    @Test
    void longCastling() {
        var logic = new Logic(createDefaultMock());

        //Рокировка невозможна(есть фигуры между королем и ладьей)
        Assertions.assertFalse(logic.move(Square.E1, Square.C1)); // для белых
        Assertions.assertFalse(logic.move(Square.E8, Square.C8)); // для черных

        //теперь будем убирать фигуры между и проверять вохможность рокировки
        //Когда нет коней
        Assertions.assertTrue(logic.move(Square.D2, Square.D4));
        Assertions.assertTrue(logic.move(Square.D7, Square.D5));
        Assertions.assertTrue(logic.move(Square.B1, Square.C3));//убираем белого коня
        Assertions.assertTrue(logic.move(Square.B8, Square.C6));//убираем черного коня
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//проверяем невозможность для белых

        //Возвращаем коней
        Assertions.assertTrue(logic.move(Square.C3, Square.B1));
        Assertions.assertTrue(logic.move(Square.C6, Square.B8));

        //Когда нет слонов
        Assertions.assertTrue(logic.move(Square.C1, Square.F4));//убираем белого слона
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));//проверяем невозможность для черных
        Assertions.assertTrue(logic.move(Square.C8, Square.F5));//убираем черного слона
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//проверяем невозможность для белых

        //Возвращаем слонов
        Assertions.assertTrue(logic.move(Square.F4, Square.C1));
        Assertions.assertTrue(logic.move(Square.F5, Square.C8));

        //Когда нет ферзей
        Assertions.assertTrue(logic.move(Square.D1, Square.D3));//убираем белого ферзя
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));//проверяем невозможность для черных
        Assertions.assertTrue(logic.move(Square.D8, Square.D6));//убираем черного ферзя
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//проверяем невозможность для белых

        //Когда есть только конь(убираем слонов и делаем нейтральный ход пешкой)
        Assertions.assertTrue(logic.move(Square.C1, Square.F4));
        Assertions.assertTrue(logic.move(Square.C8, Square.F5));
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));
        Assertions.assertTrue(logic.move(Square.B2, Square.B3));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));

        //Когда есть только слон(убираем коней, добавляем слонов и делаем нейтральный ход пешкой)
        Assertions.assertTrue(logic.move(Square.B8, Square.C6));
        Assertions.assertTrue(logic.move(Square.B1, Square.C3));
        Assertions.assertTrue(logic.move(Square.F5, Square.C8));
        Assertions.assertTrue(logic.move(Square.F4, Square.C1));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));
        Assertions.assertTrue(logic.move(Square.A7, Square.A6));
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));

        //Когда есть только ферзь(убираем слонов, добавляем ферзей и делаем нейтральный ход пешкой)
        Assertions.assertTrue(logic.move(Square.C1, Square.F4));
        Assertions.assertTrue(logic.move(Square.C8, Square.F5));
        Assertions.assertTrue(logic.move(Square.D3, Square.D1));
        Assertions.assertTrue(logic.move(Square.D6, Square.D8));
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));
        Assertions.assertTrue(logic.move(Square.A2, Square.A3));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));

        //Убираем все фигуры между и проверяем возможность рокировку
        Assertions.assertTrue(logic.move(Square.D8, Square.D6));
        Assertions.assertTrue(logic.move(Square.D1, Square.D3));
        Assertions.assertTrue(logic.move(Square.E8, Square.C8)); // для черных
        Assertions.assertTrue(logic.move(Square.E1, Square.C1)); // для белых

        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.ok);

        //Проверим позиции ладьи и короля после рокировки
        var kingWhite = logic.getFigure(Square.C1);
        Assertions.assertEquals(kingWhite.getKind(), FigureKind.King);
        Assertions.assertTrue(kingWhite.isWhite());

        var kingBlack = logic.getFigure(Square.C8);
        Assertions.assertEquals(kingBlack.getKind(), FigureKind.King);
        Assertions.assertFalse(kingBlack.isWhite());

        var rookWhite = logic.getFigure(Square.D1);
        Assertions.assertEquals(rookWhite.getKind(), FigureKind.Rook);
        Assertions.assertTrue(rookWhite.isWhite());

        var rookBlack = logic.getFigure(Square.D8);
        Assertions.assertEquals(rookBlack.getKind(), FigureKind.Rook);
        Assertions.assertFalse(rookBlack.isWhite());
    }

    @Test
    void longCastingUnderCheck() {

        // На доске 2 короля, 2 ладьи и 2 ферзя и 2 слона
        var position = new ChessPosition();
        position.add(new BishopWhite(Square.F1));
        position.add(new BishopBlack(Square.F8));
        position.add(new KingBlack(Square.E8));
        position.add(new KingWhite(Square.E1));
        position.add(new RookBlack(Square.A8));
        position.add(new RookWhite(Square.A1));
        position.add(new QueenWhite(Square.E5));
        position.add(new QueenBlack(Square.E4));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможность рокировки, если король находится под шахом
        Assertions.assertFalse(logic.move(Square.E1, Square.C1)); // для белых
        Assertions.assertTrue(logic.move(Square.F1, Square.E2)); //передадим очередность хода черным, защитившись от шаха
        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertFalse(logic.move(Square.E8, Square.C8)); //для черных
        Assertions.assertTrue(logic.move(Square.F8, Square.E7));

        //Проверим невозможность рокировки через поля под шахом(сначала D1/D8, потом C1/C8 )
        Assertions.assertTrue(logic.move(Square.E5, Square.D5));
        Assertions.assertTrue(logic.move(Square.E4, Square.D4));

        Assertions.assertFalse(logic.move(Square.E1, Square.C1)); // для белых
        Assertions.assertTrue(logic.move(Square.E2, Square.D3));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8)); //для черных

        //Поменяем позиции ферзей и проверим невозможность рокировки
        Assertions.assertTrue(logic.move(Square.D4, Square.C4));
        Assertions.assertTrue(logic.move(Square.D5, Square.C5));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8)); //для черных
        Assertions.assertTrue(logic.move(Square.E7, Square.F8));
        Assertions.assertFalse(logic.move(Square.E1, Square.C1)); // для белых

        //Проверим поле, через которое проходит ладья(B2/B8)
        //Рокировка должна пройти
        Assertions.assertTrue(logic.move(Square.C5, Square.B4));
        Assertions.assertTrue(logic.move(Square.E8, Square.C8));//для черных
        Assertions.assertTrue(logic.move(Square.B4, Square.A3));
        Assertions.assertTrue(logic.move(Square.C4, Square.B5));
        Assertions.assertTrue(logic.move(Square.E1, Square.C1));//для белых

        //Проверим, что зафиксировалось движение короля
        Assertions.assertTrue(moves.isKingMoved(true));//белого цвета
        Assertions.assertTrue(moves.isKingMoved(false));//черного цвета

    }

    @Test
    void castlingWhenFigureMoved() {

        var position = new ChessPosition();
        position.add(new RookBlack(Square.H8));
        position.add(new RookWhite(Square.H1));
        position.add(new KingBlack(Square.E8));
        position.add(new KingWhite(Square.E1));
        position.add(new RookBlack(Square.A8));
        position.add(new RookWhite(Square.A1));
        position.add(new PawnWhite(Square.A2));
        position.add(new PawnBlack(Square.A7));

        //Когда короли двигались
        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        var logic = new Logic(createDefaultMock(), position, moves, true);
        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));
        Assertions.assertTrue(logic.move(Square.A2, Square.A3));
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));//для черных
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));

        //Когда ладьи двигались
        moves = new ChessMoves().whiteRookA1Moved().whiteRookH1Moved().blackRookA8Moved().blackRookH8Moved();
        logic = new Logic(createDefaultMock(), position, moves, false);
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));//для черных
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));
        Assertions.assertTrue(logic.move(Square.A7, Square.A6));
        Assertions.assertFalse(logic.move(Square.E1, Square.G1)); // для белых
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));

    }

    @Test
    void castlingWhenPawnReplaceToRook() {

        var position = new ChessPosition();
        position.add(new KingBlack(Square.E8));
        position.add(new KingWhite(Square.E1));
        position.add(new RookBlack(Square.A8));
        position.add(new RookWhite(Square.A1));
        position.add(new PawnBlack(Square.C2));
        position.add(new PawnWhite(Square.C7));
        position.add(new BishopWhite(Square.B7));
        position.add(new BishopBlack(Square.B2));
        position.add(new QueenWhite(Square.D1));
        position.add(new QueenBlack(Square.D8));

        var moves = new ChessMoves();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Rook);
        var logic = new Logic(li, position, moves, true);

        //Проверим случай, что невозможно сделать рокировку с ладьей
        //В которую была превращена пешка и потом ладья встала на позицию съеденной ладьи для рокировки

        //Проверим невозможность длинной рокировки
        Assertions.assertTrue(logic.move(Square.B7, Square.A8));
        Assertions.assertTrue(logic.move(Square.B2, Square.A1));
        Assertions.assertTrue(logic.move(Square.C7, Square.C8));
        Assertions.assertTrue(logic.move(Square.C2, Square.C1));

        //Проверим правильность превращения
        var rookWhite = logic.getFigure(Square.C8);
        Assertions.assertTrue(rookWhite.isWhite() && rookWhite.getKind() == FigureKind.Rook);

        var rookBlack = logic.getFigure(Square.C1);
        Assertions.assertTrue(!rookBlack.isWhite() && rookBlack.getKind() == FigureKind.Rook);

        //займем созданными ладьями позиции для длинной рокировки
        Assertions.assertTrue(logic.move(Square.C8, Square.B8));
        Assertions.assertTrue(logic.move(Square.C1, Square.C8));
        Assertions.assertTrue(logic.move(Square.B8, Square.B1));
        Assertions.assertTrue(logic.move(Square.C8, Square.A8));
        Assertions.assertTrue(logic.move(Square.B1, Square.A1));

        //Проверим невозможность рокировки
        Assertions.assertTrue(logic.move(Square.D8, Square.E7));//Убираем ферзей
        Assertions.assertTrue(logic.move(Square.D1, Square.E2));
        Assertions.assertFalse(logic.move(Square.E8, Square.C8));//рокировка черных
        Assertions.assertTrue(logic.move(Square.E7, Square.E6));
        Assertions.assertFalse(logic.move(Square.E1, Square.C1));//белых

        var kingWhite = logic.getFigure(Square.E1);
        var kingBlack = logic.getFigure(Square.E8);

        //Сверим позиции ладей и королей
        Assertions.assertTrue(!kingBlack.isWhite() && kingBlack.getKind() == FigureKind.King);
        Assertions.assertTrue(kingWhite.isWhite() && kingWhite.getKind() == FigureKind.King);
        Assertions.assertTrue(!rookBlack.isWhite() && rookBlack.getKind() == FigureKind.Rook);
        Assertions.assertTrue(rookWhite.isWhite() && rookWhite.getKind() == FigureKind.Rook);

        //Проверим невозможность короткой рокировки
        position.add(new RookBlack(Square.H8));
        position.add(new RookWhite(Square.H1));
        position.add(new PawnBlack(Square.G2));
        position.add(new PawnWhite(Square.G7));
        position.add(new RookBlack(Square.F8));
        position.add(new RookWhite(Square.F1));
        position.add(new RookBlack(Square.H2));
        position.add(new RookWhite(Square.H7));

        Assertions.assertTrue(logic.move(Square.G7, Square.G8));
        Assertions.assertTrue(logic.move(Square.G2, Square.G1));
        Assertions.assertTrue(logic.move(Square.H7, Square.H8));
        Assertions.assertTrue(logic.move(Square.H2, Square.H1));

        //Проверим правильность превращения
        rookWhite = logic.getFigure(Square.G8);
        Assertions.assertTrue(rookWhite.isWhite() && rookWhite.getKind() == FigureKind.Rook);

        rookBlack = logic.getFigure(Square.G1);
        Assertions.assertTrue(!rookBlack.isWhite() && rookBlack.getKind() == FigureKind.Rook);

        //займем созданными ладьями позиции для длинной рокировки
        Assertions.assertTrue(logic.move(Square.G8, Square.G5));
        Assertions.assertTrue(logic.move(Square.G1, Square.G4));
        Assertions.assertTrue(logic.move(Square.G5, Square.H5));
        Assertions.assertTrue(logic.move(Square.G4, Square.G8));
        Assertions.assertTrue(logic.move(Square.H5, Square.H1));
        Assertions.assertTrue(logic.move(Square.G8, Square.H8));

        //Проверим невозможность рокировки
        Assertions.assertTrue(logic.move(Square.F1, Square.F3));//Убираем ладей с линии F
        Assertions.assertTrue(logic.move(Square.F8, Square.F6));
        Assertions.assertFalse(logic.move(Square.E1, Square.G2));//рокировка белых
        Assertions.assertTrue(logic.move(Square.F3, Square.F4));
        Assertions.assertFalse(logic.move(Square.E8, Square.G8));// черных

        //Сверим позиции ладей и королей
        Assertions.assertTrue(kingWhite.isWhite() && kingWhite.getKind() == FigureKind.King);
        Assertions.assertTrue(!kingBlack.isWhite() && kingBlack.getKind() == FigureKind.King);
        Assertions.assertTrue(rookWhite.isWhite() && rookWhite.getKind() == FigureKind.Rook);
        Assertions.assertTrue(!rookBlack.isWhite() && rookBlack.getKind() == FigureKind.Rook);
    }

    @Test
    void takePawn() {

        var logic = new Logic(createDefaultMock());
        Assertions.assertTrue(logic.move(Square.E2, Square.E4));
        Assertions.assertTrue(logic.move(Square.D7, Square.D5));
        Assertions.assertTrue(logic.move(Square.E4, Square.E5));
        Assertions.assertTrue(logic.move(Square.F7, Square.F5));
        var square = logic.getMoves().getPawnSquareToTake();
        Assertions.assertEquals(square, Square.F6);
        Assertions.assertFalse(logic.move(Square.E5, Square.D6));// не можем, так как ход прошел
        Assertions.assertTrue(logic.move(Square.E5, Square.F6)); // взятие пешки на проходе

        //Проверим, было ли успешное взятие на проходе(очередность поменялась и фигур стало 31)
        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getAllFigures().size(), 31);

        //Проверим, что не можем взять на проходе пешку. которая сходила не сразу на 2 клетки вперед
        Assertions.assertTrue(logic.move(Square.C7, Square.C6));
        Assertions.assertTrue(logic.move(Square.B2, Square.B4));
        Assertions.assertTrue(logic.move(Square.C6, Square.C5));
        Assertions.assertTrue(logic.move(Square.B4, Square.B5));
        Assertions.assertTrue(logic.move(Square.H7, Square.H6));
        Assertions.assertFalse(logic.move(Square.B5, Square.C6));

        //Проверим очередность хода(должны ходить белые и что кол-во фигур не изменилось)
        Assertions.assertTrue(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getAllFigures().size(), 31);
    }

    @Test
    void staleMate() {

        // На доске 2 короля и ферзь.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.A8));
        position.add(new KingWhite(Square.A1));
        position.add(new QueenWhite(Square.D7));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();


        var logic = new Logic(createDefaultMock(), position, moves, true);
        Assertions.assertTrue(logic.move(Square.D7, Square.C7)); // черным пат после хода ферзя
        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.stalemate);
    }

    @Test
    void checkMate() {

        // На доске 2 короля и ферзь.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.H8));
        position.add(new KingWhite(Square.E1));
        position.add(new QueenWhite(Square.G5));
        position.add(new RookWhite(Square.G1));
        position.add(new QueenBlack(Square.A8));
        position.add(new KingBlack(Square.B8));

        var moves = new ChessMoves();

        var logic = new Logic(createDefaultMock(), position, moves, true);
        Assertions.assertTrue(logic.move(Square.G5, Square.H5)); // черным мат после хода ферзя
        Assertions.assertFalse(logic.isWhiteTurn());
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        //Проверим невозможность хода
        Assertions.assertFalse(logic.move(Square.A8, Square.A7));
        Assertions.assertFalse(logic.move(Square.A8, Square.B7));
        Assertions.assertFalse(logic.move(Square.B8, Square.A6));
    }

    @Test
    void draw() {
        var position = new ChessPosition();
        position.add(new KingBlack(Square.F6));
        position.add(new KingWhite(Square.C3));
        position.add(new PawnBlack(Square.C4));

        var moves = new ChessMoves();
        var logic = new Logic(createDefaultMock(), position, moves, true);

        //Проверим невозможность хода после взятия последней фигуру, не являющейся королеми текущий статус
        Assertions.assertTrue(logic.move(Square.C3, Square.C4));
        Assertions.assertFalse(logic.isWhiteTurn());//проверяем, что сейчас ход черных
        Assertions.assertFalse(logic.move(Square.F6, Square.F5));
        Assertions.assertEquals(logic.getStatus(), GameStatus.draw);
    }

    @Test
    void pawnToWhiteQueen() {

        // На доске 2 короля и пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.A8));
        position.add(new KingWhite(Square.B6));
        position.add(new PawnWhite(Square.C7));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();

        var logic = new Logic(createDefaultMock(), position, moves, true);
        Assertions.assertTrue(logic.move(Square.C7, Square.C8)); // превращаем пешку в ферзя
        Assertions.assertFalse(logic.isWhiteTurn());
        //Если создался ферзь, то из данной позиции -> мат черным
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var queenWhite = logic.getFigure(Square.C8);
        Assertions.assertTrue(queenWhite.isWhite() && queenWhite.getKind() == FigureKind.Queen);
    }

    @Test
    void pawnToBlackQueen() {

        // На доске 2 короля и пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.B3));
        position.add(new KingWhite(Square.A1));
        position.add(new PawnBlack(Square.C2));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();

        var logic = new Logic(createDefaultMock(), position, moves, false);
        Assertions.assertTrue(logic.move(Square.C2, Square.C1)); // превращаем пешку в ферзя
        Assertions.assertTrue(logic.isWhiteTurn());
        //Если создался ферзь, то из данной позиции -> мат белым
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var queenBlack = logic.getFigure(Square.C1);
        Assertions.assertTrue(!queenBlack.isWhite() && queenBlack.getKind() == FigureKind.Queen);
    }

    @Test
    void pawnToWhiteRook() {

        // На доске 2 короля и белая пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.A8));
        position.add(new KingWhite(Square.B6));
        position.add(new PawnWhite(Square.C7));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Rook);
        var logic = new Logic(li, position, moves, true);

        Assertions.assertTrue(logic.move(Square.C7, Square.C8)); // превращаем пешку в ладью
        Assertions.assertFalse(logic.isWhiteTurn());
        //Если создалась ладья, то из данной позиции -> мат черным
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var rookWhite = logic.getFigure(Square.C8);
        Assertions.assertTrue(rookWhite.isWhite() && rookWhite.getKind() == FigureKind.Rook);
    }

    @Test
    void pawnToBlackRook() {

        // На доске 2 короля и черная пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.G3));
        position.add(new KingWhite(Square.H1));
        position.add(new PawnBlack(Square.F2));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Rook);
        var logic = new Logic(li, position, moves, false);

        Assertions.assertTrue(logic.move(Square.F2, Square.F1)); // превращаем пешку в ладью
        Assertions.assertTrue(logic.isWhiteTurn());
        //Если создалась ладья, то из данной позиции -> мат белым
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var rookBlack = logic.getFigure(Square.F1);
        Assertions.assertTrue(!rookBlack.isWhite() && rookBlack.getKind() == FigureKind.Rook);
    }

    @Test
    void pawnToWhiteBishop() {

        // На доске 2 короляи три белых фигуры: слон, ладья и пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.A7));
        position.add(new KingWhite(Square.B5));
        position.add(new PawnWhite(Square.B7));
        position.add(new BishopWhite(Square.C6));
        position.add(new RookWhite(Square.C8));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Bishop);
        var logic = new Logic(li, position, moves, true);

        Assertions.assertTrue(logic.move(Square.B7, Square.B8)); // превращаем пешку в слона
        Assertions.assertFalse(logic.isWhiteTurn());
        //Если создался слон, то из данной позиции -> мат черным
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var bishopWhite = logic.getFigure(Square.B8);
        Assertions.assertTrue(bishopWhite.isWhite() && bishopWhite.getKind() == FigureKind.Bishop);
    }

    @Test
    void pawnToBlackBishop() {

        // На доске 2 короля и три черных фигуры: слон, ладья и пешка.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.G4));
        position.add(new KingWhite(Square.H2));
        position.add(new PawnBlack(Square.G2));
        position.add(new BishopBlack(Square.F3));
        position.add(new RookBlack(Square.F1));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Bishop);
        var logic = new Logic(li, position, moves, false);

        Assertions.assertTrue(logic.move(Square.G2, Square.G1)); // превращаем пешку в слона
        Assertions.assertTrue(logic.isWhiteTurn());
        //Если создался слон, то из данной позиции -> мат белым
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var bishopBlack = logic.getFigure(Square.G1);
        Assertions.assertTrue(!bishopBlack.isWhite() && bishopBlack.getKind() == FigureKind.Bishop);
    }

    @Test
    void pawnToWhiteKnight() {

        // На доске 2 короля, 3 черные пешки и две белые фигуры: пешка и слон.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.G7));
        position.add(new KingWhite(Square.G5));
        position.add(new PawnWhite(Square.E7));
        position.add(new PawnBlack(Square.H8));
        position.add(new PawnBlack(Square.F8));
        position.add(new PawnBlack(Square.H7));
        position.add(new BishopWhite(Square.E6));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Knight);
        var logic = new Logic(li, position, moves, true);

        Assertions.assertTrue(logic.move(Square.E7, Square.E8)); // превращаем пешку в слона
        Assertions.assertFalse(logic.isWhiteTurn());
        //Если создался конь, то из данной позиции -> мат черным
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var knightWhite = logic.getFigure(Square.E8);
        Assertions.assertTrue(knightWhite.isWhite() && knightWhite.getKind() == FigureKind.Knight);
    }

    @Test
    void pawnToBlackKnight() {

        // На доске 2 короля, 3 белые пешки и две черные фигуры: пешка и слон.
        var position = new ChessPosition();
        position.add(new KingBlack(Square.B4));
        position.add(new KingWhite(Square.B2));
        position.add(new PawnBlack(Square.D2));
        position.add(new PawnWhite(Square.C1));
        position.add(new PawnWhite(Square.A1));
        position.add(new PawnWhite(Square.A2));
        position.add(new BishopBlack(Square.D3));

        var moves = new ChessMoves().whiteKingMoved().blackKingMoved();
        LogicInterface li = Mockito.mock(LogicInterface.class);
        Mockito.when(li.getFigureToReplacePawn()).thenReturn(FigureKind.Knight);
        var logic = new Logic(li, position, moves, false);

        Assertions.assertTrue(logic.move(Square.D2, Square.D1)); // превращаем пешку в коня
        Assertions.assertTrue(logic.isWhiteTurn());
        //Если создался конь, то из данной позиции -> мат белым
        Assertions.assertEquals(logic.getStatus(), GameStatus.checkmate);
        var knightBlack = logic.getFigure(Square.D1);
        Assertions.assertTrue(!knightBlack.isWhite() && knightBlack.getKind() == FigureKind.Knight);
    }
}