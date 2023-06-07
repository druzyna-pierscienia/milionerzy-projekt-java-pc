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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        odpA.setDisable(false);
        odpB.setDisable(false);
        odpC.setDisable(false);
        odpD.setDisable(false);

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


        List<Button> wrongAnswers = new ArrayList<>();

        if (correct.equals("a")) {
            wrongAnswers.add(odpB);
            wrongAnswers.add(odpC);
            wrongAnswers.add(odpD);
        } else if (correct.equals("b")) {
            wrongAnswers.add(odpA);
            wrongAnswers.add(odpC);
            wrongAnswers.add(odpD);
        } else if (correct.equals("c")) {
            wrongAnswers.add(odpA);
            wrongAnswers.add(odpB);
            wrongAnswers.add(odpD);
        } else {
            wrongAnswers.add(odpA);
            wrongAnswers.add(odpB);
            wrongAnswers.add(odpC);
        }
        Random random = new Random();
        Button button1 = wrongAnswers.get(random.nextInt(wrongAnswers.size()));
        wrongAnswers.remove(button1);
        Button button2 = wrongAnswers.get(random.nextInt(wrongAnswers.size()));

        button1.setDisable(true);
        button2.setDisable(true);

        // Wyłączanie przycisku "hint5050"
        hint5050.setDisable(true);
        hint5050.setVisible(false);


    }

    private void useHintAudience(ActionEvent event) {
        // TODO: Implementacja metody hintAudience

        // Sprawdzenie, czy koło "50/50" zostało już użyte
        boolean is5050Used = odpA.isDisabled() || odpB.isDisabled() || odpC.isDisabled() || odpD.isDisabled();

        // Wylosuj procentowe szanse odpowiedzi
        int chanceA, chanceB, chanceC, chanceD;

        if (is5050Used) {
            // Koło "50/50" było już użyte, rozdziel procenty pomiędzy pozostałe dwie odpowiedzi
            chanceA = new Random().nextInt(71) + 15; // szansa na odpowiedź A to 15-85%
            chanceB = 100 - chanceA; // szansa na odpowiedź B to reszta
            chanceC = 0;
            chanceD = 0;
        } else {
            // Koło "50/50" jeszcze nie zostało użyte, losuj normalny rozkład procentowy
            chanceA = new Random().nextInt(41) + 50; // szansa na odpowiedź A to 50-90%
            chanceB = new Random().nextInt(100 - chanceA) + 1; // szansa na odpowiedź B to 1-80%
            chanceC = new Random().nextInt(100 - chanceA - chanceB) + 1; // szansa na odpowiedź C to 1-70%
            chanceD = 100 - chanceA - chanceB - chanceC; // szansa na odpowiedź D to reszta
        }

        // Tworzenie wiadomości dla okna dialogowego
        String message;
        if (correct.equals("a")) {
            message = "Pytanie do publiczności:\nA: " + chanceA + "%\nB: " + chanceB + "%\nC: " + chanceC + "%\nD: " + chanceD + "%";
        } else if (correct.equals("b")) {
            message = "Pytanie do publiczności:\nA: " + chanceB + "%\nB: " + chanceA + "%\nC: " + chanceC + "%\nD: " + chanceD + "%";
        } else if (correct.equals("c")) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceB + "%\nC: " + chanceA + "%\nD: " + chanceD + "%";
        } else {
            message = "Pytanie do publiczności:\nA: " + chanceD + "%\nB: " + chanceB + "%\nC: " + chanceC + "%\nD: " + chanceA + "%";
        }

        // Wyświetlanie okna dialogowego
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Pytanie do publiczności");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        dialog.showAndWait();

        hintAudience.setDisable(true);
        hintAudience.setVisible(false);
    }

    private void useHintPhone(ActionEvent event) {
        // TODO: Implementacja metody hintPhone
        // Wylosuj procentową szansę na poprawną odpowiedź przy użyciu "telefonu do przyjaciela"
        boolean isAnswerCorrect = new Random().nextInt(100) < 75;

        // Tworzenie wiadomości dla okna dialogowego
        String message;
        if (isAnswerCorrect) {
            message = "Twój przyjaciel pomógł Ci, poprawna odpowiedź to: " + correct;
        } else {
            message = "Twój przyjaciel nie jest pewien odpowiedzi, trudno powiedzieć...";
        }

        // Wyświetlanie okna dialogowego
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Telefon do przyjaciela");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        dialog.showAndWait();

        hintPhone.setDisable(true);
        hintPhone.setVisible(false);

    }
}
