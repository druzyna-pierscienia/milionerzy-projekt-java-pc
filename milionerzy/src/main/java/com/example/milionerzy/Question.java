package com.example.milionerzy;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Question {
    public Button backToMenuButton;
    @FXML
    public Button odpA;
    @FXML
    public Button odpB;
    @FXML
    public Button odpC;
    @FXML
    public Button odpD;
    public Button hint5050;
    public Button hintAudience;
    public Button hintPhone;
    public Text question_text;
    private String correct = "a", question = "Pytanie za milion",questionFromDataBase,answerAFromDataBase,answerBFromDataBase,answerCFromDataBase,answerDFromDataBase,correctAns;
    private int score = 0, scoreG = 0, questionLvl = 0;
    public void setLvl(int Lvl) {
        questionLvl = Lvl;
    }

    public void setScore(int scoreP) {
        score = scoreP;
    }

    public void setScoreG(int scoreGP) {
        scoreG = scoreGP;
    }
    public void initialize() {
        questionLvl += 1;
    String url = "http://localhost:8080/question?roundNumber=" + questionLvl;
    ApiRequest.executeRequest(url, new ApiRequest.ApiCallback() {
        @Override
        public void onResponse(String result) {
            // Obsługa odpowiedzi API
            if (result.equals("blad")) {
                question_text.setText(result);
                Platform.runLater(() -> odpA.setText("Kliknij aby przejść dalej"));
            } else {
                String[] quest = result.split("/");

                question_text.setText(quest[0] + " " + questionLvl);
                Platform.runLater(() -> odpA.setText(quest[1]));
                Platform.runLater(() -> odpB.setText(quest[2]));
                Platform.runLater(() -> odpC.setText(quest[3]));
                Platform.runLater(() -> odpD.setText(quest[4]));
                correct = quest[5];
            }
        }

        @Override
        public void onError(Exception e) {
            // Obsługa błędu
            question_text.setText("Wystąpił błąd: " + e.getMessage());
        }
    });


        backToMenuButton.setOnAction(this::goBackToMainMenu);
        odpA.setOnAction(this::answerA);
        odpB.setOnAction(this::answerB);
        odpC.setOnAction(this::answerC);
        odpD.setOnAction(this::answerD);
        hint5050.setOnAction(this::useHint5050);
        hintAudience.setOnAction(this::useHintAudience);
        hintPhone.setOnAction(this::useHintPhone);

    }


    private void correctAnswer() {
        questionLvl += 1;
        score = score + questionLvl * 10;
        if (questionLvl % 3 == 0) {
            scoreG = score;
        }
        else if(questionLvl == 10){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ending_screen.fxml"));
                Parent endingRoot = loader.load();
                EndingScreen endingController = loader.getController();
                endingController.setLvl(questionLvl);
                endingController.setScore(scoreG);
                endingController.setQuestion(questionFromDataBase);
                endingController.setCorrect(correct);
                Scene endingScene = new Scene(endingRoot);
                Stage stage = (Stage) backToMenuButton.getScene().getWindow();
                stage.setScene(endingScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String url = "http://localhost:8080/question?roundNumber=" + questionLvl;
        ApiRequest.executeRequest(url, new ApiRequest.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Obsługa odpowiedzi API
                if (result.equals("blad")) {
                    question_text.setText(result);
                } else {
                    String[] quest = result.split("/");

                    question_text.setText(quest[0] + " " + questionLvl);
                    Platform.runLater(() -> odpA.setText(quest[1]));
                    Platform.runLater(() -> odpB.setText(quest[2]));
                    Platform.runLater(() -> odpC.setText(quest[3]));
                    Platform.runLater(() -> odpD.setText(quest[4]));
                    correct = quest[5];
                }
            }

            @Override
            public void onError(Exception e) {
                // Obsługa błędu
                question_text.setText("Wystąpił błąd: " + e.getMessage());
            }
        });
    }

    private void wrongAnswer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ending_screen.fxml"));
            Parent endingRoot = loader.load();
            EndingScreen endingController = loader.getController();
            endingController.setScore(scoreG);
            endingController.setQuestion(questionFromDataBase);
            endingController.setCorrect(correct);
            Scene endingScene = new Scene(endingRoot);
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(endingScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goBackToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Parent mainMenuRoot = loader.load();
            Scene mainMenuScene = new Scene(mainMenuRoot);
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(mainMenuScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answerA(ActionEvent event) {
        if (correct.equals("a")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerB(ActionEvent event) {
        if (correct.equals("b")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerC(ActionEvent event) {
        if (correct.equals("c")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerD(ActionEvent event) {
        if (correct.equals("d")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }
    private void useHint5050(ActionEvent event) {
        // TODO: Implementacja metody hint5050
    }

    private void useHintAudience(ActionEvent event) {
        // TODO: Implementacja metody hintAudience
    }

    private void useHintPhone(ActionEvent event) {
        // TODO: Implementacja metody hintPhone
    }
}
