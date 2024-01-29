package com.example.milionerzy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `MainMenu` class represents the controller for the main menu screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It handles user actions related to starting a new game, viewing the scoreboard, accessing information about the app, and logging out.
 */
public class MainMenu {

    /** Button for starting a new game. */
    public Button newGame_button;

    /** Button for accessing the scoreboard. */
    public Button scoreBoard_button;

    /** Button for accessing information about the app. */
    public Button aboutApp_button;

    /**
     * Initializes the main menu by setting up event handlers for buttons.
     */
    public void initialize() {
        newGame_button.setOnAction(this::openQuestionScene);
        scoreBoard_button.setOnAction(this::openScoreboardScene);
        aboutApp_button.setOnAction(this::openAboutAppScene);
    }

    /**
     * Opens the question scene for a new game.
     *
     * @param event The ActionEvent triggered by the newGame_button.
     */
    private void openQuestionScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("question.fxml"));
            Parent questionRoot = loader.load();
            Scene questionScene = new Scene(questionRoot);
            Stage stage = (Stage) newGame_button.getScene().getWindow();
            stage.setScene(questionScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the scoreboard scene.
     *
     * @param event The ActionEvent triggered by the scoreBoard_button.
     */
    private void openScoreboardScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("scoreboard.fxml"));
            Parent scoreboardRoot = loader.load();
            Scene scoreboardScene = new Scene(scoreboardRoot);
            Stage stage = (Stage) scoreBoard_button.getScene().getWindow();
            stage.setScene(scoreboardScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the about app scene to display information about the application.
     *
     * @param event The ActionEvent triggered by the aboutApp_button.
     */
    private void openAboutAppScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about_app.fxml"));
            Parent aboutAppRoot = loader.load();
            Scene aboutAppScene = new Scene(aboutAppRoot);
            Stage stage = (Stage) aboutApp_button.getScene().getWindow();
            stage.setScene(aboutAppScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the user out and navigates back to the login screen.
     */
    @FXML
    private void logOut() {
        try {
            User user = new User();
            user.logUserOut();

            // Load the FXML file for the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Create a scene based on the loaded FXML file
            Scene scene = new Scene(root);

            // Get the Stage object from the current view
            Stage stage = (Stage) aboutApp_button.getScene().getWindow();

            // Set the new scene on the Stage
            stage.setScene(scene);

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
