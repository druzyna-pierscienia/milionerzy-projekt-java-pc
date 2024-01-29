package com.example.milionerzy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `Login` class represents the controller for the login screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It handles user actions related to logging in, checking activation status, and navigating to registration or confirmation screens.
 */
public class Login {

    /** Text field for entering the username. */
    @FXML
    private TextField username_text;

    /** Password field for entering the password. */
    @FXML
    private PasswordField password_text;

    /**
     * Attempts to log in the user by checking the activation status.
     */
    @FXML
    protected void logIn() {
        ApiRequest.executeRequest("http://localhost:8080/getActivationStatus?login=" + username_text.getText(),
                new ApiRequest.ApiCallback() {

                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("Not Activated")) {
                                try {
                                    User user = new User();
                                    user.logUserIn(username_text.getText());

                                    // Load the FXML file for the confirmation screen
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("confirm.fxml"));
                                    Parent root = loader.load();

                                    // Create a scene based on the loaded FXML file
                                    Scene scene = new Scene(root);

                                    // Get the Stage object from the current view
                                    Stage stage = (Stage) username_text.getScene().getWindow();

                                    // Set the new scene on the Stage
                                    stage.setScene(scene);

                                    // Show the new scene
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // If the account is activated, proceed with password login
                                performLogin();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Performs the actual login with the provided username and password.
     */
    private void performLogin() {
        ApiRequest.executeRequest(
                "http://localhost:8080/login?login=" + username_text.getText() + "&password="
                        + password_text.getText(),
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("true")) {
                                try {
                                    User user = new User();
                                    user.logUserIn(username_text.getText());

                                    // Load the FXML file for the main menu
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("main_menu.fxml"));
                                    Parent root = loader.load();

                                    // Create a scene based on the loaded FXML file
                                    Scene scene = new Scene(root);

                                    // Get the Stage object from the current view
                                    Stage stage = (Stage) password_text.getScene().getWindow();

                                    // Set the new scene on the Stage
                                    stage.setScene(scene);

                                    // Show the new scene
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Display an information alert for failed login attempt
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Failed to log in");
                                alert.setHeaderText(null);
                                alert.setContentText("Incorrect username or password");

                                // Show the alert window
                                alert.showAndWait();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Navigates to the registration screen.
     */
    @FXML
    protected void registration() {
        try {
            // Load the FXML file for the registration screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Parent root = loader.load();

            // Create a scene based on the loaded FXML file
            Scene scene = new Scene(root);

            // Get the Stage object from the current view
            Stage stage = (Stage) password_text.getScene().getWindow();

            // Set the new scene on the Stage
            stage.setScene(scene);

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
