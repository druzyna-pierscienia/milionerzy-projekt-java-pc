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
import java.util.concurrent.ExecutionException;

public class Login {

    @FXML
    private TextField username_text;
    @FXML
    private TextField password_text;

    @FXML
    protected void logIn(){
        // Pobieranie 'response' z bazy
        ApiRequest.executeRequest("http://localhost:8080/login?login=" + username_text.getText() + "&password=" + password_text.getText(), new ApiRequest.ApiCallback() {
            @Override
            public void onResponse(String result) {
                Platform.runLater(() -> {
                    if (result.equals("true")) {
                        try {

                            User user = new User();
                            user.logUserIn(username_text.getText());

                            // Ładowanie pliku FXML dla nowej sceny
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
                            Parent root = loader.load();

                            // Tworzenie sceny na podstawie załadowanego pliku FXML
                            Scene scene = new Scene(root);

                            // Pobieranie obiektu Stage z bieżącego widoku
                            Stage stage = (Stage) password_text.getScene().getWindow();

                            // Ustawianie nowej sceny na Stage
                            stage.setScene(scene);

                            // Wyświetlanie nowej sceny
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Failed to log in");
                        alert.setHeaderText(null);
                        alert.setContentText("Incorrect username or password");

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

    @FXML
    protected void registration(){
        try {
            // Ładowanie pliku FXML dla nowej sceny
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Parent root = loader.load();

            // Tworzenie sceny na podstawie załadowanego pliku FXML
            Scene scene = new Scene(root);

            // Pobieranie obiektu Stage z bieżącego widoku
            Stage stage = (Stage) password_text.getScene().getWindow();

            // Ustawianie nowej sceny na Stage
            stage.setScene(scene);

            // Wyświetlanie nowej sceny
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
