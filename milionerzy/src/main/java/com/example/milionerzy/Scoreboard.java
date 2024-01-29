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

/**
 * Controller class for the scoreboard functionality.
 * Displays the top 10 scores retrieved from the server.
 */
public class Scoreboard {

    public Button back_button;
    @FXML
    private Text position1;
    @FXML
    private Text position2;
    @FXML
    private Text position3;
    @FXML
    private Text position4;
    @FXML
    private Text position5;
    @FXML
    private Text position6;
    @FXML
    private Text position7;
    @FXML
    private Text position8;
    @FXML
    private Text position9;
    @FXML
    private Text position10;

    /**
     * Initializes the Scoreboard controller.
     * Fetches the top 10 scores from the server and displays them in the corresponding Text elements.
     */
    public void initialize() {
        back_button.setOnAction(this::goBackToMainMenu);

        String url = "http://localhost:8080/ranking";
        ApiRequest.executeRequest(url, new ApiRequest.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Handles API response
                if (result.equals("blad")) {
                    setEmptyResults();
                } else {
                    processScoreboardResults(result);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handles error
                position1.setText("An error occurred: " + e.getMessage());
            }
        });
    }

    /**
     * Navigates back to the main menu screen.
     *
     * @param event The triggering ActionEvent.
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
     * Sets the Text elements to display empty results.
     */
    private void setEmptyResults() {
        position1.setText("No results available");
        position2.setText("");
        position3.setText("");
        position4.setText("");
        position5.setText("");
        position6.setText("");
        position7.setText("");
        position8.setText("");
        position9.setText("");
        position10.setText("");
    }

    /**
     * Processes the scoreboard results and sets the Text elements accordingly.
     *
     * @param result The raw result string from the server.
     */
    private void processScoreboardResults(String result) {
        String[] pairs = result.split(";");
        String[] results = new String[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            String[] parts = pairs[i].split("/");
            if (parts.length == 2) {
                String name = parts[0];
                String points = parts[1];
                results[i] = name + " - " + points;
            }
        }

        position1.setText(results[0]);
        position2.setText(results[1]);
        position3.setText(results[2]);
        position4.setText(results[3]);
        position5.setText(results[4]);
        position6.setText(results[5]);
        position7.setText(results[6]);
        position8.setText(results[7]);
        position9.setText(results[8]);
        position10.setText(results[9]);
    }
}
