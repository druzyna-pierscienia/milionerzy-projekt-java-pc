package com.example.milionerzy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Confirm {

    @FXML
    private TextField code_text;

    @FXML
    protected void resend() {
        String login = User.getUserLogin();
        // Wysyłanie zapytania wysyłającego ponownie maila z kodem aktywacyjnym
        ApiRequest.executeRequest("http://localhost:8080/sendActivationCode?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sent");
                            alert.setHeaderText(null);
                            alert.setContentText("Resent code");
                            alert.showAndWait(); // Wyświetlanie okna alertu
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @FXML
    protected void confirm() {
        String login = User.getUserLogin();
        ApiRequest.executeRequest("http://localhost:8080/getActivationCode?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        if (result.equals(code_text.getText())) {
                            backToMenu();
                        } else {
                            // Dodanie obsługi błędu w przypadku niepoprawnego kodu aktywacyjnego
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Incorrect activation code");
                                alert.showAndWait();
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void backToMenu() {
        String login = User.getUserLogin();
        ApiRequest.executeRequest(
                "http://localhost:8080/activateUser?login=" + login,
                new ApiRequest.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            if (result.equals("User activated successfully")) {
                                try {
                                    // Ładowanie pliku FXML dla nowej sceny
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("main_menu.fxml"));
                                    Parent root = loader.load();

                                    // Tworzenie sceny na podstawie załadowanego pliku FXML
                                    Scene scene = new Scene(root);

                                    // Pobieranie obiektu Stage z bieżącego widoku
                                    Stage stage = (Stage) code_text.getScene().getWindow();

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
                                alert.setContentText("Didn't activated");

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
