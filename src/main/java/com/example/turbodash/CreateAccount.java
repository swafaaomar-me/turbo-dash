package com.example.turbodash;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Registration screen: email, username and password, validated and saved by {@link UserDataManager}.
 */
public final class CreateAccount {

    private CreateAccount() {
    }

    public static void show(Stage stage) {
        Assets.decorate(stage);

        ImageView backgroundView = new ImageView(Assets.image("create-account-screen.jpeg"));
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800.0, 600.0);

        Label title = new Label("CREATE NEW ACCOUNT");
        title.setLayoutX(366.0);
        title.setLayoutY(110.0);
        title.setPrefHeight(50.0);
        title.setPrefWidth(388.0);
        title.setStyle("-fx-background-color: #FF0068;");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font(Ui.FONT, 27.0));

        Label emailLabel = Ui.formLabel("EMAIL", 485.0, 225.0);
        TextField emailField = new TextField();
        Ui.styleField(emailField, 486.0, 265.0);
        Label emailError = Ui.errorLabel(486.0, 301.0);

        Label usernameLabel = Ui.formLabel("USERNAME", 485.0, 318.0);
        TextField usernameField = new TextField();
        Ui.styleField(usernameField, 486.0, 358.0);
        Label usernameError = Ui.errorLabel(486.0, 394.0);

        Label passwordLabel = Ui.formLabel("PASSWORD", 485.0, 408.0);
        PasswordField passwordField = new PasswordField();
        Ui.styleField(passwordField, 486.0, 448.0);
        Label passwordError = Ui.errorLabel(486.0, 484.0);

        Button nextButton = Ui.formButton("NEXT", 547.0, 505.0);
        nextButton.setDefaultButton(true);
        nextButton.setOnAction(event -> {
            emailError.setText("");
            usernameError.setText("");
            passwordError.setText("");

            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            UserDataManager.RegistrationResult result =
                    new UserDataManager().register(email, username, password);
            switch (result) {
                case SUCCESS -> Login.show(stage);
                case EMPTY_FIELDS -> {
                    if (email.isBlank()) emailError.setText("Field is empty");
                    if (username.isBlank()) usernameError.setText("Field is empty");
                    if (password.isEmpty()) passwordError.setText("Field is empty");
                }
                case INVALID_EMAIL -> emailError.setText("Enter a valid email address");
                case USERNAME_TAKEN -> usernameError.setText("That username is already taken");
                case PASSWORD_TOO_SHORT -> passwordError.setText(
                        "Use at least " + UserDataManager.MIN_PASSWORD_LENGTH + " characters");
                case SAVE_FAILED -> passwordError.setText("Could not save your account, try again");
            }
        });

        Button backButton = Ui.formButton("BACK", 547.0, 550.0);
        backButton.setOnAction(event -> Start.show(stage));

        anchorPane.getChildren().addAll(backgroundView, title,
                emailLabel, emailField, emailError,
                usernameLabel, usernameField, usernameError,
                passwordLabel, passwordField, passwordError,
                nextButton, backButton);

        Ui.setScene(stage, new Scene(anchorPane, 800, 600));
    }
}
