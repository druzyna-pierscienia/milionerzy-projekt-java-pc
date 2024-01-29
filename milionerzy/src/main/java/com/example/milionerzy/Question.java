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

/**
 * The `Question` class represents the controller for the question screen in the Milionerzy (Who Wants to Be a Millionaire) application.
 * It handles the display of questions, user responses, and the use of lifelines (hints). It also manages the transition to the next question or the ending screen.
 */
public class Question {

    /** Button for navigating back to the main menu. */
    public Button backToMenuButton;

    /** Buttons for possible answers (A, B, C, D). */
    @FXML public Button odpA;
    @FXML public Button odpB;
    @FXML public Button odpC;
    @FXML public Button odpD;

    /** Buttons for using hints (50/50, Audience, Phone). */
    public Button hint5050;
    public Button hintAudience;
    public Button hintPhone;

    /** Text displaying the current question. */
    public Text question_text;

    /** Static variable holding the correct answer (A, B, C, D). */
    private static String correct = "a";

    /** Variables holding question-related data. */
    private String question = "Pytanie za milion";
    private String questionFromDataBase;
    private String answerAFromDataBase;
    private String answerBFromDataBase;
    private String answerCFromDataBase;
    private String answerDFromDataBase;
    private String correctAns;

    /** Variables related to scoring and question level. */
    private int score = 0, scoreG = 0, questionLvl = 0;
    private static String AnsA = "", AnsB = "", AnsC = "", AnsD = "", Quest = "";

    /**
     * Sets the question level.
     *
     * @param Lvl The question level to set.
     */
    public void setLvl(int Lvl) {
        questionLvl = Lvl;
    }

    /**
     * Sets the overall score.
     *
     * @param scoreP The overall score to set.
     */
    public void setScore(int scoreP) {
        score = scoreP;
    }

    /**
     * Sets the score for the current game.
     *
     * @param scoreGP The score for the current game to set.
     */
    public void setScoreG(int scoreGP) {
        scoreG = scoreGP;
    }

    /**
     * Initializes the question screen by loading the question and setting up event handlers.
     */
    public void initialize() {
        questionLvl += 1;
        String url = "http://localhost:8080/question?roundNumber=" + questionLvl;
        ApiRequest.executeRequest(url, new ApiRequest.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Handle API response
                if (result.equals("blad")) {
                    question_text.setText(result);
                    Platform.runLater(() -> odpA.setText("Kliknij aby przejść dalej"));
                } else {
                    String[] quest = result.split("/");
                    question_text.setText(quest[0]);
                    Platform.runLater(() -> odpA.setText(quest[1]));
                    Platform.runLater(() -> odpB.setText(quest[2]));
                    Platform.runLater(() -> odpC.setText(quest[3]));
                    Platform.runLater(() -> odpD.setText(quest[4]));
                    correct = quest[5];
                    Quest = String.valueOf(quest[0]);
                    AnsA = String.valueOf(quest[1]);
                    AnsB = String.valueOf(quest[2]);
                    AnsC = String.valueOf(quest[3]);
                    AnsD = String.valueOf(quest[4]);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle error
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
        hintPhone.setOnAction(event -> {
            int questionNumber = questionLvl;
            useHintPhone(event, questionNumber);
        });
    }

    // ... (Continued on next comment due to character limit)

    // Continued...

    /**
     * Gets the correct answer for the current question.
     *
     * @return The correct answer.
     */
    public static String getCorrectAns() {
        if (correct.equals("a")) {
            return AnsA;
        } else if (correct.equals("b")) {
            return AnsB;
        } else if (correct.equals("c")) {
            return AnsC;
        } else {
            return AnsD;
        }
    }

    /**
     * Gets the current question.
     *
     * @return The current question.
     */
    public static String getQuestion() {
        return Quest;
    }

    /**
     * Handles the correct answer scenario, updates the score, and loads the next question.
     */
    private void correctAnswer() {
        questionLvl += 1;
        score = score + questionLvl * 10;

        odpA.setDisable(false);
        odpB.setDisable(false);
        odpC.setDisable(false);
        odpD.setDisable(false);

        if (questionLvl % 3 == 0) {
            scoreG = score;
        } else if (questionLvl == 10) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ending_screen.fxml"));
                Parent endingRoot = loader.load();
                EndingScreen endingController = loader.getController();
                endingController.setLvl(questionLvl);
                endingController.setScore(scoreG);
                endingController.setQuestion(Quest);
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
                // Handle API response
                if (result.equals("blad")) {
                    question_text.setText(result);
                } else {
                    String[] quest = result.split("/");
                    question_text.setText(quest[0]);
                    Platform.runLater(() -> odpA.setText(quest[1]));
                    Platform.runLater(() -> odpB.setText(quest[2]));
                    Platform.runLater(() -> odpC.setText(quest[3]));
                    Platform.runLater(() -> odpD.setText(quest[4]));
                    correct = quest[5];
                    Quest = String.valueOf(quest[0]);
                    AnsA = String.valueOf(quest[1]);
                    AnsB = String.valueOf(quest[2]);
                    AnsC = String.valueOf(quest[3]);
                    AnsD = String.valueOf(quest[4]);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle error
                question_text.setText("Wystąpił błąd: " + e.getMessage());
            }
        });
    }

    // ... (Continued on next comment due to character limit)

    // Continued...

    /**
     * Handles the wrong answer scenario and navigates to the ending screen.
     */
    private void wrongAnswer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ending_screen.fxml"));
            Parent endingRoot = loader.load();
            EndingScreen endingController = loader.getController();
            endingController.setScore(scoreG);
            endingController.setQuestion(Quest);
            endingController.setCorrect(correct);
            Scene endingScene = new Scene(endingRoot);
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(endingScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (Continued on next comment due to character limit)

    // Continued...

    /**
     * Navigates back to the main menu.
     *
     * @param event The ActionEvent triggered by the backToMenuButton.
     */
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

    /**
     * Handles the user selecting answer A.
     *
     * @param event The ActionEvent triggered by odpA.
     */
    private void answerA(ActionEvent event) {
        if (correct.equals("a")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * Handles the user selecting answer B.
     *
     * @param event The ActionEvent triggered by odpB.
     */
    private void answerB(ActionEvent event) {
        if (correct.equals("b")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * Handles the user selecting answer C.
     *
     * @param event The ActionEvent triggered by odpC.
     */
    private void answerC(ActionEvent event) {
        if (correct.equals("c")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * Handles the user selecting answer D.
     *
     * @param event The ActionEvent triggered by odpD.
     */
    private void answerD(ActionEvent event) {
        if (correct.equals("d")) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * Handles the user using the 50/50 hint.
     *
     * @param event The ActionEvent triggered by hint5050.
     */
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

    // ... (Continued on next comment due to character limit)

    // Continued...

    /**
     * Handles the user using the Audience hint.
     *
     * @param event The ActionEvent triggered by hintAudience.
     */
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
        if (correct.equals("a") && odpB.isDisabled() && odpC.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceA + "%\nB: " + chanceD + "%\nC: " + chanceC + "%\nD: " + chanceB + "%";
        } else if (correct.equals("a") && odpB.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceA + "%\nB: " + chanceC + "%\nC: " + chanceB + "%\nD: " + chanceD + "%";
        } else if (correct.equals("a") && odpC.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceA + "%\nB: " + chanceC + "%\nC: " + chanceB + "%\nD: " + chanceD + "%";
        } else if (correct.equals("b") && odpA.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceA + "%\nC: " + chanceB + "%\nD: " + chanceD + "%";
        } else if (correct.equals("b") && odpC.isDisabled() && odpA.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceA + "%\nC: " + chanceD + "%\nD: " + chanceB + "%";
        } else if (correct.equals("b") && odpC.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceB + "%\nB: " + chanceA + "%\nC: " + chanceC + "%\nD: " + chanceD + "%";
        } else if (correct.equals("c") && odpA.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceB + "%\nC: " + chanceA + "%\nD: " + chanceD + "%";
        } else if (correct.equals("c") && odpB.isDisabled() && odpA.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceD + "%\nC: " + chanceA + "%\nD: " + chanceB + "%";
        } else if (correct.equals("c") && odpB.isDisabled() && odpD.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceB + "%\nB: " + chanceC + "%\nC: " + chanceA + "%\nD: " + chanceD + "%";
        } else if (correct.equals("d") && odpA.isDisabled() && odpB.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceD + "%\nC: " + chanceB + "%\nD: " + chanceA + "%";
        } else if (correct.equals("d") && odpC.isDisabled() && odpA.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceC + "%\nB: " + chanceB + "%\nC: " + chanceD + "%\nD: " + chanceA + "%";
        } else if (correct.equals("d") && odpB.isDisabled() && odpC.isDisabled()) {
            message = "Pytanie do publiczności:\nA: " + chanceB + "%\nB: " + chanceC + "%\nC: " + chanceD + "%\nD: " + chanceA + "%";
        } else if (correct.equals("a")) {
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

    // ... (Continued on next comment due to character limit)

    // Continued...

    /**
     * Handles the user using the Phone hint.
     *
     * @param event        The ActionEvent triggered by hintPhone.
     * @param questionLvl  The current question level.
     */
    private void useHintPhone(ActionEvent event, Integer questionLvl) {
        // Wylosuj procentową szansę na poprawną odpowiedź przy użyciu "telefonu do przyjaciela"
        int chance = 0;
        String message;

        // Określenie szansy w zależności od numeru pytania
        if (questionLvl >= 1 && questionLvl <= 3) {
            // Dla pytań 1-3 zawsze poprawna odpowiedź
            chance = 100;
        } else if (questionLvl >= 4 && questionLvl <= 6) {
            // Dla pytań 4-6 szansa na poprawną odpowiedź to 80-100%
            chance = new Random().nextInt(21) + 80;
        } else if (questionLvl >= 7 && questionLvl <= 9) {
            // Dla pytań 7-9 szansa na poprawną odpowiedź to 60-80%
            chance = new Random().nextInt(21) + 60;
        } else if (questionLvl == 10) {
            // Dla pytania 10 szansa na poprawną odpowiedź to 50%
            chance = 50;
        }

        // Sprawdzenie, czy poprawna odpowiedź jest dostępna
        boolean correctAnswerAvailable = chance == 100 || new Random().nextInt(101) <= chance;

        // Tworzenie wiadomości dla okna dialogowego
        if (correctAnswerAvailable) {
            message = "Zadzwońmy do przyjaciela...\nPrzyjaciel jest pewny, że poprawna odpowiedź to:\n" + getCorrectAns() + "!";
        } else {
            message = "Zadzwońmy do przyjaciela...\nPrzyjaciel ma pewne wątpliwości, ale stawia na odpowiedź: "
                    + getRandomWrongAnswer();
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

    /**
     * Gets a random wrong answer (used in the Phone hint).
     *
     * @return A random wrong answer.
     */
    private String getRandomWrongAnswer() {
        List<String> wrongAnswers = new ArrayList<>(List.of(AnsA, AnsB, AnsC, AnsD));
        wrongAnswers.remove(getCorrectAns());
        return wrongAnswers.get(new Random().nextInt(wrongAnswers.size()));
    }
}

