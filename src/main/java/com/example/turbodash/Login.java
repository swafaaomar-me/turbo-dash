package com.example.turbodash;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Login screen. Checks the username and password against the saved accounts.
 */
public final class Login {

    private Login() {
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800.0, 600.0);

        ImageView background = new ImageView(Assets.image("login-screen.jpeg"));
        background.setFitWidth(800);
        background.setFitHeight(600);

        Label usernameLabel = Ui.formLabel("USERNAME", 482.0, 219.0);
        TextField usernameField = new TextField();
        Ui.styleField(usernameField, 484.0, 263.0);

        Label passwordLabel = Ui.formLabel("PASSWORD", 486.0, 322.0);
        PasswordField passwordField = new PasswordField();
        Ui.styleField(passwordField, 486.0, 373.0);

        Label errorLabel = Ui.errorLabel(486.0, 412.0);

        Button loginButton = Ui.formButton("NEXT", 547.0, 498.0);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isBlank() || password.isEmpty()) {
                errorLabel.setText("Enter your username and password");
                return;
            }
            // One message for both cases, so the screen doesn't reveal which usernames exist
            new UserDataManager().authenticate(username, password).ifPresentOrElse(user -> {
                Session.logIn(user.getUsername());
                MainMenu.show(stage);
            }, () -> {
                errorLabel.setText("Invalid username or password");
                passwordField.clear();
            });
        });

        Button backButton = Ui.formButton("BACK", 547.0, 545.0);
        backButton.setOnAction(event -> Start.show(stage));

        anchorPane.getChildren().addAll(background, usernameLabel, usernameField,
                passwordLabel, passwordField, errorLabel, loginButton, backButton);

        Ui.setScene(stage, new Scene(anchorPane, 800, 600));
    }
}
