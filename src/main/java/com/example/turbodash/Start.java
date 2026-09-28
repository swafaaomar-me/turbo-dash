package com.example.turbodash;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Entry point of Turbo Dash: the welcome screen with Login, Create Account and Quit.
 */
public class Start extends Application {

    @Override
    public void start(Stage primaryStage) {
        show(primaryStage);
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        // Insert the background image
        Image backgroundImage = Assets.image("start-screen.jpeg");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false));

        // Vertical box layout that holds the buttons
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25));
        vbox.setBackground(new Background(background));

        Button loginButton = Ui.menuButton("LOGIN");
        loginButton.setOnAction(event -> Login.show(stage));

        Button createAccountButton = Ui.menuButton("CREATE NEW ACCOUNT");
        createAccountButton.setOnAction(event -> CreateAccount.show(stage));

        Button quitButton = Ui.menuButton("QUIT");
        quitButton.setOnAction(event -> stage.close());

        vbox.getChildren().addAll(loginButton, createAccountButton, quitButton);

        Ui.setScene(stage, new Scene(vbox, 800, 600));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
