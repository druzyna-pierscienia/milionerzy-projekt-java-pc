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

    public void initialize() {
        back_button.setOnAction(this::goBackToMainMenu);

        String url = "http://localhost:8080/ranking";
        ApiRequest.executeRequest(url, new ApiRequest.ApiCallback() {
            @Override
            public void onResponse(String result) {
                // Obsługa odpowiedzi API
                if (result.equals("blad")) {
                    position1.setText(result);
                    position2.setText(result);
                    position3.setText(result);
                    position4.setText(result);
                    position5.setText(result);
                    position6.setText(result);
                    position7.setText(result);
                    position8.setText(result);
                    position9.setText(result);
                    position10.setText(result);
                } else {
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

            @Override
            public void onError(Exception e) {
                // Obsługa błędu
                position1.setText("Wystąpił błąd: " + e.getMessage());
            }
        });


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
