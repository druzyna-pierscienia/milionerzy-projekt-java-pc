
package com.example.milionerzy;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
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

    public void setLvl(int Lvl) { questionLvl = Lvl; }
    public void setScore(int score) {
        final_score.setText("Score: " + score);
    }

    public void setQuestion(String questionText) {
        if(questionLvl != 10){
            question.setText("Question: " + questionText);
        }
        else{
            question.setText("Winner!");
        }
    }

    public void setCorrect(String correct) {
        if(questionLvl != 10){
            correct_answer.setText("Correct: " + correct);
        }
        else {
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
}
