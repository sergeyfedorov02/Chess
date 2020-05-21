package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.figures.FigureKind;


public class FigureReplacementWindow {

    public static FigureKind newWindow(String title) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY)));

        var control = new VBox();
        control.setPrefHeight(20);
        control.setSpacing(20.0);
        control.setAlignment(Pos.CENTER);

        var mainButtonBox = new HBox();
        mainButtonBox.setSpacing(20);
        mainButtonBox.setAlignment(Pos.CENTER);

        var first = new VBox();
        first.setSpacing(20);
        first.setAlignment(Pos.CENTER_LEFT);

        var second = new VBox();
        second.setSpacing(20);
        second.setAlignment(Pos.CENTER_LEFT);

        Text text = new Text("Выберите новую фигуру");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        var group = new ToggleGroup();

        var bishop = addButton("Слон", FigureKind.Bishop, group);
        var knight = addButton("Конь", FigureKind.Knight, group);
        var queen = addButton("Ферзь", FigureKind.Queen, group);
        queen.setSelected(true);
        var rook = addButton("Ладья", FigureKind.Rook, group);

        var closeButton = new Button("Подтвердить");
        closeButton.setOnMouseClicked(
                event -> {
                    window.close();
                }
        );

        first.getChildren().addAll(queen, rook);
        second.getChildren().addAll(bishop, knight);
        mainButtonBox.getChildren().addAll(first, second);
        mainButtonBox.setScaleX(1.3);
        mainButtonBox.setScaleY(1.3);

        control.getChildren().addAll(text,mainButtonBox, closeButton);

        pane.setCenter(control);

        Scene scene = new Scene(pane, 200, 200);
        window.setScene(scene);
        window.setTitle(title);
        window.setResizable(false);
        window.showAndWait();

        return (FigureKind) group.getSelectedToggle().getUserData();
    }

    private static RadioButton addButton (String name, FigureKind figureKind, ToggleGroup group) {
        var figure = new RadioButton(name);
        figure.setUserData(figureKind);
        figure.setToggleGroup(group);
        return figure;
    }
}
