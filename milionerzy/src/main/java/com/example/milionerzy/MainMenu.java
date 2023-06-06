package com.example.milionerzy;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    public Button newGame_button;
    public Button scoreBoard_button;
    public Button aboutApp_button;

    public void initialize() {
        newGame_button.setOnAction(this::openQuestionScene);
        scoreBoard_button.setOnAction(this::openScoreboardScene);
        aboutApp_button.setOnAction(this::openAboutAppScene);
    }


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
    //TODO: PRZYCISK LOGOUT
}