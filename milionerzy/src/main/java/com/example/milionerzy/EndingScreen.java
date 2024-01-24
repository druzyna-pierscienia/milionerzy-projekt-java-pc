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

public class EndingScreen {

    public Button back_button;

    @FXML
    private Text final_score;

    @FXML
    private Text question;

    @FXML
    private Text correct_answer;
    private int questionLvl = 0;
    private int scoreToSend = 0;

    public void setLvl(int Lvl) {
        questionLvl = Lvl;
    }

    public void setScore(int score) {
        final_score.setText("Score: " + score);
        scoreToSend = score;
        String login = User.getUserLogin();
        String url = "http://localhost:8080/saveScore?login="+login+"&score="+score;

        String urlParameters = "";

        ApiRequestInto.executeRequest(url, urlParameters, new ApiRequestInto.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Obsługa odpowiedzi z serwera
                System.out.println("Odpowiedź serwera: " + result + " Dla url: " + url);
            }

            @Override
            public void onError(Exception e) {
                // Obsługa błędu
                e.printStackTrace();
            }
        });
    }

    public void setQuestion(String questionText) {

        String quest = Question.getQuestion();

        if (questionLvl != 10) {
            question.setText("Question: " + quest);
        } else {
            question.setText("Winner!");
        }
    }

    public void setCorrect(String correct) {
        String corr = Question.getCorrectAns();
        if (questionLvl != 10) {
            correct_answer.setText("Correct: " + corr);
        } else {
            correct_answer.setText(" ");
        }
    }

    public void initialize() {

        back_button.setOnAction(this::goBackToMainMenu);
    }

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
    public void sendScore(){
        String login = User.getUserLogin();
        ApiRequest.executeRequest(
                "http://localhost:8080/sendScore?login=" + login + "&wynik=" + scoreToSend,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("Score sent successfully")) {
                                try {
                                    // Ładowanie pliku FXML dla nowej sceny
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("main_menu.fxml"));
                                    Parent root = loader.load();

                                    // Tworzenie sceny na podstawie załadowanego pliku FXML
                                    Scene scene = new Scene(root);

                                    // Pobieranie obiektu Stage z bieżącego widoku
                                    Stage stage = (Stage) final_score.getScene().getWindow();

                                    // Ustawianie nowej sceny na Stage
                                    stage.setScene(scene);

                                    // Wyświetlanie nowej sceny
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Failed");
                                alert.setHeaderText(null);
                                alert.setContentText("Score sent unsuccessfully");

                                // Wyświetlanie okna alertu
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
