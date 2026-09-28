package com.example.turbodash;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/** Shows the game rules image with a BACK button to the main menu. */
public final class GameRules {

    private GameRules() {
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800.0, 600.0);

        ImageView rules = new ImageView(Assets.image("game-rules-screen.jpeg"));
        rules.setFitWidth(800);
        rules.setFitHeight(600);

        Button backButton = new Button("BACK");
        backButton.setFont(Font.font(Ui.FONT, 25));
        backButton.setStyle("-fx-background-color: #FF0022; -fx-text-fill: white; -fx-cursor: hand;");
        backButton.setOnAction(e -> MainMenu.show(stage));
        AnchorPane.setLeftAnchor(backButton, 24.0);
        AnchorPane.setBottomAnchor(backButton, 24.0);

        anchorPane.getChildren().addAll(rules, backButton);

        Ui.setScene(stage, new Scene(anchorPane, 800, 600));
    }
}
