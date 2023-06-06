package com.example.milionerzy;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Question {
    public Button backToMenuButton;
    public Button odpA;
    public Button odpB;
    public Button odpC;
    public Button odpD;
    public Button hint5050;
    public Button hintAudience;
    public Button hintPhone;
    public Text question_text;
    private String correct = "A", question = "Pytanie za milion",questionFromDataBase,answerAFromDataBase,answerBFromDataBase,answerCFromDataBase,answerDFromDataBase,correctAns;
    private int score = 0, scoreG = 0, questionLvl = 0;

    public void setScore(int scoreP) {
        score = scoreP;
    }

    public void setScoreG(int scoreGP) {
        scoreG = scoreGP;
    }

    public void setLvl(int Lvl) {
        questionLvl = Lvl;
    }

    public void initialize() {


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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("question.fxml"));
            Parent questionRoot = loader.load();
            Question questionController = loader.getController();
            questionController.setScoreG(scoreG);
            questionController.setScore(score);
            questionController.setLvl(questionLvl);
            Scene questionScene = new Scene(questionRoot);
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(questionScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void wrongAnswer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ending_screen.fxml"));
            Parent endingRoot = loader.load();
            EndingScreen endingController = loader.getController();
            endingController.setScore(scoreG);
            endingController.setQuestion(question);
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
        if (correct.equals("A")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerB(ActionEvent event) {
        if (correct.equals("B")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerC(ActionEvent event) {
        if (correct.equals("C")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void answerD(ActionEvent event) {
        if (correct.equals("D")) {
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
