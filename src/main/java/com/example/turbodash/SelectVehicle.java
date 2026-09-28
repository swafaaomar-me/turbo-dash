package com.example.turbodash;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/** Lets the player pick one of three cars, then starts the race with it. */
public final class SelectVehicle {

    /** The cars the player can choose from and the image used for each. */
    public enum Vehicle {
        PORSCHE("porsche.png"),
        SPEEDSTAR("speedstar.png"),
        SUV("suv.png");

        private final String imageFile;

        Vehicle(String imageFile) {
            this.imageFile = imageFile;
        }

        public String imageFile() {
            return imageFile;
        }
    }

    private SelectVehicle() {
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800.0, 600.0);

        ImageView background = new ImageView(Assets.image("select-vehicle-screen.jpeg"));
        background.setFitWidth(800);
        background.setFitHeight(600);
        anchorPane.getChildren().add(background);

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPadding(new Insets(20));
        flowPane.setHgap(45);

        for (Vehicle vehicle : Vehicle.values()) {
            Button button = new Button(vehicle.name());
            button.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-cursor: hand;");
            button.setFont(Font.font(Ui.FONT, 16));
            button.setOnAction(e -> GameScreen.show(stage, vehicle));
            flowPane.getChildren().add(button);
        }

        Button backButton = new Button("BACK");
        backButton.setStyle("-fx-background-color: #FF0022; -fx-text-fill: white; -fx-cursor: hand;");
        backButton.setFont(Font.font(Ui.FONT, 16));
        backButton.setOnAction(e -> MainMenu.show(stage));
        AnchorPane.setLeftAnchor(backButton, 24.0);
        AnchorPane.setBottomAnchor(backButton, 24.0);

        anchorPane.getChildren().addAll(flowPane, backButton);
        AnchorPane.setTopAnchor(flowPane, 450.0);
        AnchorPane.setLeftAnchor(flowPane, 180.0);

        Ui.setScene(stage, new Scene(anchorPane, 800, 600));
    }
}
