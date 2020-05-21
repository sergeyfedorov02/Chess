package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import sample.figures.Figure;
import sample.figures.FigureKind;
import sample.figures.Square;

import java.util.HashMap;
import java.util.stream.Collectors;


public class Chess extends Application implements LogicInterface {
    private static final String MyGame = "Шахматы";
    private final int size = 8;
    private Logic logic;
    private Text status;
    private BorderPane borderPane;

    private final HashMap<Figure,Rectangle> figuresToRectangles = new HashMap<Figure, Rectangle>();

    private Rectangle buildRectangle(int x, int y, int size, boolean white) {
        Rectangle rect = new Rectangle();
        rect.setX(x * size);
        rect.setY(y * size);
        rect.setHeight(size);
        rect.setWidth(size);
        if (white) {
            rect.setFill(Color.WHITE);
        } else {
            rect.setFill(Color.GRAY);
        }
        rect.setStroke(Color.BLACK);
        return rect;
    }

    private Rectangle buildFigure(int x, int y, int size, String image) {
        Rectangle rect = new Rectangle();
        rect.setX(x);
        rect.setY(y);
        rect.setHeight(size);
        rect.setWidth(size);
        Image img = new Image(this.getClass().getClassLoader().getResource("resources/" + image).toString());
        rect.setFill(new ImagePattern(img));
        final Rectangle currentPoint = new Rectangle(x, y);
        rect.setOnDragDetected(
                event -> {
                    currentPoint.setX(event.getX());
                    currentPoint.setY(event.getY());
                }
        );
        rect.setOnMouseDragged(
                event -> {
                    var xPos = event.getX();
                    var yPos = event.getY();

                    var newX =  xPos - size / 2;
                    var newY = yPos - size / 2;

                    // Проверка, чтобы мышкой нельзя было перетащить фигуру за край игральной доски
                    if (xPos > 320 || xPos < 0 || yPos < 14 || yPos > 320)  {
                        return;
                    }
                    rect.setX(newX);
                    rect.setY(newY);
                }
        );
        rect.setOnMouseReleased(
                event -> {
                    var endSquare = this.findBy(event.getX(), event.getY());
                    logic.move(this.findBy(currentPoint.getX(), currentPoint.getY()), endSquare);
                    updateStatus();
                }
        );
        return rect;
    }

    private void removeFigureRectangle(Figure figure) {
        var shape = figuresToRectangles.get(figure);
        if (shape == null) {
            return;
        }
        var group = (Group) shape.getParent();
        group.getChildren().remove(shape);

        figuresToRectangles.remove(figure);
    }

    private void updateStatus() {
        var gameStatus = this.logic.getStatus();
        String text;

        if (gameStatus == GameStatus.ok) {
           text = String.format("Ход %s", this.logic.isWhiteTurn() ? "белых" : "черных");
        }
        else if (gameStatus == GameStatus.checkmate){
            text = String.format("Мат %s. Игра закончена", this.logic.isWhiteTurn() ? "белым" : "черным");
        } else text = String.format("Последний ход %s привел к пату. Ничья.  Игра закончена.", !this.logic.isWhiteTurn() ? "белых" : "черных");

        // обновляем позицию фигур

        // получаем все фигуры на доске
         var allFigures = this.logic.getAllFigures();

         // удаляем исчезнувшие с доски фигуры (или съеденные или пешка после превращения)
         var figuresToRemove = this.figuresToRectangles.keySet().stream().filter(item -> !allFigures.contains(item))
                 .collect(Collectors.toList());

         figuresToRemove.forEach(this::removeFigureRectangle);

         // перемещаем фигуры на новые позиции (после хода может переместиться две фигуры, если это рокировка)
         allFigures.forEach(item -> {
             var rect = this.figuresToRectangles.get(item);
             if (rect != null) {
                 rect.setX((item.position().x*40+5));
                 rect.setY((item.position().y*40+5));
             }
         });

        // ищем добавленные (пешка может превратиться в фигуру), создаем для них графический элемент
        var newFigures = allFigures.stream().filter(item -> !figuresToRectangles.containsKey(item));

        var grid = (Group)borderPane.getCenter();
        newFigures.forEach(item -> add(item, grid));

        status.setText(text);
    }

    private Group buildGrid() {
        Group panel = new Group();
        for (int y = 0; y != this.size; y++) {
            for (int x = 0; x != this.size; x++) {
                panel.getChildren().add(
                        this.buildRectangle(x, y, 40, (x + y) % 2 == 0)
                );
            }
        }


        // Делаем буквы у доски
        Text letters = new Text("A       B       C       D       E       F       G       H");
        letters.setX(17);
        letters.setY(340);
        letters.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        letters.setFill(Color.SANDYBROWN);
        letters.setWrappingWidth(320);
        panel.getChildren().add(letters);

        // Делаем цифры у доски
        Text digits = new Text("8\n\n7\n\n6\n\n5\n\n4\n\n3\n\n2\n\n1");
        digits.setX(-20);
        digits.setY(20);
        digits.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        digits.setFill(Color.SANDYBROWN);
        digits.setWrappingWidth(320);
        panel.getChildren().add(digits);

        return panel;
    }

    @Override
    public void start(Stage stage) {
        logic = new Logic(this);
        borderPane = new BorderPane();

        //Установка изображения на задний фон сцены
        BackgroundImage myBI= new BackgroundImage(new Image("resources/WoodTwo.jpg",500,470,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0,0,false,false,true,true));
        borderPane.setBackground(new Background(myBI));

        var control = new VBox();

        control.setPrefHeight(40);
        control.setSpacing(10.0);
        control.setAlignment(Pos.BOTTOM_CENTER);
        Button start = new Button("Начать заново");
        start.setOnMouseClicked(
                event -> {
                    this.refresh(borderPane);
                }
        );

        //Добавляем текст, передающий текущий статус игры
        status = new Text();
        status.setFill(Color.WHITE);
        status.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        control.getChildren().addAll(status, start);

        borderPane.setBottom(control);
        borderPane.setCenter(this.buildGrid());
        BorderPane.setMargin(control, new Insets(10,10,30,10));

        //Делаем иконку приложения
        stage.getIcons().add(new Image("resources/GameIcon.jpg"));

        stage.setScene(new Scene(borderPane, 500, 470));
        stage.setMinHeight(510);
        stage.setMinWidth(515);
        stage.setTitle(MyGame);
        stage.setResizable(true);
        stage.show();
        this.refresh(borderPane);

        updateStatus();
    }

    private void refresh(final BorderPane border) {
        figuresToRectangles.clear();
        Group grid = this.buildGrid();
        this.logic.clean();
        border.setCenter(grid);
        updateStatus();
    }


    public void add(Figure figure, Group grid) {
        Square position = figure.position();

        var shape = this.buildFigure(
                position.x * 40 + 5,
                position.y * 40 + 5,
                30,
                figure.icon()
        );
        grid.getChildren().add(shape);
        figuresToRectangles.put(figure,shape);
    }

    private Square findBy(double graphX, double graphY) {
        Square result = Square.A1;
        int x = (int) graphX / 40;
        int y = (int) graphY / 40;
        for (Square square : Square.values()) {
            if (square.x == x && square.y == y) {
                result = square;
                break;
            }
        }
        return result;
    }

    @Override
    public FigureKind getFigureToReplacePawn() {
        return FigureReplacementWindow.newWindow("Замена");
    }
}


