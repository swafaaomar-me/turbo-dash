package com.example.turbodash;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main menu shown after logging in: the player's high score, Select Vehicle, Game Rules,
 * Log Out and Quit.
 */
public final class MainMenu {

    private MainMenu() {
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800.0, 600.0);

        ImageView background = new ImageView(Assets.image("main-menu-screen.jpeg"));
        background.setFitWidth(800);
        background.setFitHeight(600);
        anchorPane.getChildren().add(background);

        VBox vbox = new VBox(16);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20));

        String player = Session.currentUsername();
        int highScore = new ScoreManager().getHighScore(player);
        Label highScoreLabel = new Label((player == null ? "" : player.toUpperCase() + "  |  ")
                + "HIGH SCORE: " + highScore);
        highScoreLabel.setFont(Font.font(Ui.FONT, 22.0));
        highScoreLabel.setTextFill(Color.WHITE);
        highScoreLabel.setPadding(new Insets(4, 12, 4, 12));
        highScoreLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.45);");

        Button selectVehicleButton = Ui.menuButton("SELECT VEHICLE");
        selectVehicleButton.setOnAction(e -> SelectVehicle.show(stage));

        Button gameRulesButton = Ui.menuButton("GAME RULES");
        gameRulesButton.setOnAction(e -> GameRules.show(stage));

        Button logOutButton = Ui.menuButton("LOG OUT");
        logOutButton.setOnAction(e -> {
            Session.logOut();
            Start.show(stage);
        });

        Button quitButton = Ui.menuButton("QUIT GAME");
        quitButton.setOnAction(e -> stage.close());

        vbox.getChildren().addAll(highScoreLabel, selectVehicleButton, gameRulesButton, logOutButton, quitButton);

        anchorPane.getChildren().add(vbox);
        AnchorPane.setTopAnchor(vbox, 165.0); // below the MAIN MENU title in the background image
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

        Ui.setScene(stage, new Scene(anchorPane, 800, 600));
    }
}
