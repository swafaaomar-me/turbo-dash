package com.example.turbodash;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

/** Loads the game view from FXML and starts a race with the chosen vehicle. */
public final class GameScreen {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 720;

    private GameScreen() {
    }

    public static void show(Stage stage, SelectVehicle.Vehicle vehicle) {
        FXMLLoader loader = new FXMLLoader(GameScreen.class.getResource("game-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load the game view", e);
        }
        GameController controller = loader.getController();

        Assets.decorate(stage);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Ui.setScene(stage, scene);

        // Closing the window mid-race goes back to the main menu instead of quitting
        stage.setOnCloseRequest(event -> {
            event.consume();
            controller.leaveGame();
        });

        controller.startGame(stage, vehicle);
        root.requestFocus();
    }
}
