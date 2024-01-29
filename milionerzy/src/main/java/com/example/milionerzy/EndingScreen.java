package com.example.milionerzy;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `EndingScreen` class represents the controller for the ending screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It displays the final score, question, and correct answer (if applicable) and allows the user to navigate back to the main menu.
 */
public class EndingScreen {

    /** Button for navigating back to the main menu. */
    public Button back_button;

    /** Text displaying the final score. */
    @FXML
    private Text final_score;

    /** Text displaying the final question. */
    @FXML
    private Text question;

    /** Text displaying the correct answer (if applicable). */
    @FXML
    private Text correct_answer;

    /** Variable representing the question level. */
    private int questionLvl = 0;

    /** Variable representing the score to send to the server. */
    private int scoreToSend = 0;

    /**
     * Sets the question level.
     *
     * @param Lvl The question level to set.
     */
    public void setLvl(int Lvl) {
        questionLvl = Lvl;
    }

    /**
     * Sets the final score and sends it to the server.
     *
     * @param score The final score to display and send.
     */
    public void setScore(int score) {
        final_score.setText("Score: " + score);
        scoreToSend = score;
        String login = User.getUserLogin();
        String url = "http://localhost:8080/saveScore?login="+login+"&score="+score;

        String urlParameters = "";

        ApiRequestInto.executeRequest(url, urlParameters, new ApiRequestInto.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Handle the server response
                System.out.println("Server Response: " + result + " For URL: " + url);
            }

            @Override
            public void onError(Exception e) {
                // Handle the error
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the final question text.
     *
     * @param questionText The final question text to display.
     */
    public void setQuestion(String questionText) {
        String quest = Question.getQuestion();

        if (questionLvl != 10) {
            question.setText("Question: " + quest);
        } else {
            question.setText("Winner!");
        }
    }

    /**
     * Sets the correct answer text.
     *
     * @param correct The correct answer text to display.
     */
    public void setCorrect(String correct) {
        String corr = Question.getCorrectAns();
        if (questionLvl != 10) {
            correct_answer.setText("Correct: " + corr);
        } else {
            correct_answer.setText(" ");
        }
    }

    /**
     * Initializes the ending screen by setting up the event handler for the back_button.
     */
    public void initialize() {
        back_button.setOnAction(this::goBackToMainMenu);
    }

    /**
     * Navigates back to the main menu.
     *
     * @param event The ActionEvent triggered by the back_button.
     */
    private void goBackToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Parent mainMenuRoot = loader.load();
            Scene mainMenuScene = new Scene(mainMenuRoot);
            Stage stage = (Stage) back_button.getScene().getWindow();
            stage.setScene(mainMenuScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the final score to the server and navigates back to the main menu.
     */
    public void sendScore() {
        String login = User.getUserLogin();
        ApiRequest.executeRequest(
                "http://localhost:8080/sendScore?login=" + login + "&wynik=" + scoreToSend,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("Score sent successfully")) {
                                try {
                                    // Load the FXML file for the main menu
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("main_menu.fxml"));
                                    Parent root = loader.load();

                                    // Create a scene based on the loaded FXML file
                                    Scene scene = new Scene(root);

                                    // Get the Stage object from the current view
                                    Stage stage = (Stage) final_score.getScene().getWindow();

                                    // Set the new scene on the Stage
                                    stage.setScene(scene);

                                    // Show the new scene
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Display an information alert for failed score sending
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Failed");
                                alert.setHeaderText(null);
                                alert.setContentText("Score sent unsuccessfully");

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
