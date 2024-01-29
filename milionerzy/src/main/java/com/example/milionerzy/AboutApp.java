package com.example.milionerzy;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The AboutApp class represents the controller for the "About App" screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It provides functionality for navigating back to the main menu.
 */
public class AboutApp {

    /** Button for navigating back to the main menu. */
    public Button back_button;

    /**
     * Initializes the AboutApp controller by setting up the event handler for the back_button.
     */
    public void initialize() {
        back_button.setOnAction(this::goBackToMainMenu);
    }

    /**
     * Event handler method for the back_button. It loads the main_menu.fxml file and switches the scene to the main menu.
     *
     * @param event The ActionEvent triggered by the back_button.
     */
    private void goBackToMainMenu(ActionEvent event) {
        try {
            // Load the main_menu.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));

            // Load the root element of the main menu
            Parent mainMenuRoot = loader.load();

            // Create a new scene using the main menu root
            Scene mainMenuScene = new Scene(mainMenuRoot);

            // Get the current stage from the back_button's scene
            Stage stage = (Stage) back_button.getScene().getWindow();

            // Set the scene of the stage to the main menu scene
            stage.setScene(mainMenuScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
