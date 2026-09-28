package com.example.turbodash;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/** Shared styling helpers so every screen looks the same. */
final class Ui {

    static final String FONT = "Rockwell Extra Bold";

    private Ui() {
    }

    /** Pink button used on the start screen and main menu. */
    static Button menuButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font(FONT, 25));
        button.setStyle("-fx-background-color: #FF0068; -fx-text-fill: white; -fx-cursor: hand;");
        return button;
    }

    /** Red button used for NEXT / BACK on the form screens. */
    static Button formButton(String text, double x, double y) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefHeight(34.0);
        button.setPrefWidth(129.0);
        button.setStyle("-fx-background-color: #FF0022; -fx-cursor: hand;");
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font(FONT, 20.0));
        return button;
    }

    static Label formLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(FONT, 30.0));
        return label;
    }

    static void styleField(TextField field, double x, double y) {
        field.setLayoutX(x);
        field.setLayoutY(y);
        field.setPrefHeight(34.0);
        field.setPrefWidth(252.0);
        field.setStyle("-fx-text-fill: white; -fx-background-color: #E4043F;");
    }

    static Label errorLabel(double x, double y) {
        Label label = new Label();
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        label.setFont(Font.font("Arial", 13.0));
        return label;
    }

    /** Shows a new scene on the one game window and resizes the window to fit it. */
    static void setScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}
