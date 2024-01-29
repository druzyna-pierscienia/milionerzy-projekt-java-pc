package com.example.milionerzy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `Confirm` class represents the controller for the "Confirm Account" screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It handles user actions related to confirming the account through an activation code.
 */
public class Confirm {

    /** Text field for entering the activation code. */
    @FXML
    private TextField code_text;

    /**
     * Resends the activation code to the user's email.
     */
    @FXML
    protected void resend() {
        String login = User.getUserLogin();

        // Sending a request to resend the activation code via email
        ApiRequest.executeRequest("http://localhost:8080/sendActivationCode?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            // Displaying an information alert indicating successful code resent
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sent");
                            alert.setHeaderText(null);
                            alert.setContentText("Resent code");
                            alert.showAndWait();
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Handles the confirmation of the account using the provided activation code.
     */
    @FXML
    protected void confirm() {
        String login = User.getUserLogin();

        ApiRequest.executeRequest("http://localhost:8080/getActivationCode?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        if (result.equals(code_text.getText())) {
                            // If the activation code is correct, navigate back to the main menu
                            backToMenu();
                        } else {
                            // Display an error alert for incorrect activation code
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Incorrect activation code");
                                alert.showAndWait();
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Navigates back to the main menu after successful account activation.
     */
    private void backToMenu() {
        String login = User.getUserLogin();

        ApiRequest.executeRequest(
                "http://localhost:8080/activateUser?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("User activated successfully")) {
                                try {
                                    // Load the FXML file for the main menu
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("main_menu.fxml"));
                                    Parent root = loader.load();

                                    // Create a scene based on the loaded FXML file
                                    Scene scene = new Scene(root);

                                    // Get the Stage object from the current view
                                    Stage stage = (Stage) code_text.getScene().getWindow();

                                    // Set the new scene on the Stage
                                    stage.setScene(scene);

                                    // Show the new scene
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Display an information alert for failed activation
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Failed");
                                alert.setHeaderText(null);
                                alert.setContentText("Didn't activate");

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
}
