package com.example.milionerzy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
    @FXML
    private PasswordField registration_password_text;
    @FXML
    private PasswordField registration_repeatPassword_text;
    @FXML
    private TextField registration_email_text;
    @FXML
    private TextField registration_username_text;

    @FXML
    protected void register() {
        String regexMail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        Pattern patternMail = Pattern.compile(regexMail);
        Matcher matcher = patternMail.matcher(registration_email_text.getText().toString().trim());

        if (matcher.matches()) {
            if (registration_password_text.getText().toString().trim().equals(registration_repeatPassword_text.getText().toString().trim())) {
                // Pobieranie 'response' z bazy
                String url = "http://localhost:8080/register?login="+registration_username_text.getText().toString().trim()+"&password="+registration_password_text.getText().toString().trim()+"&mail="+registration_email_text.getText().toString().trim();
                ApiRequestInto.executeRequest(url, "", new ApiRequestInto.ApiCallback() {
                        @Override
                        public void onResponse(String result) {
                            Platform.runLater(() -> {
                                try {

                                    if (result.equals("420")) {
                                        // Ładowanie pliku FXML dla nowej sceny
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                                        Parent root = loader.load();

                                        // Tworzenie sceny na podstawie załadowanego pliku FXML
                                        Scene scene = new Scene(root);

                                        // Pobieranie obiektu Stage z bieżącego widoku
                                        Stage stage = (Stage) registration_email_text.getScene().getWindow();

                                        // Ustawianie nowej sceny na Stage
                                        stage.setScene(scene);

                                        // Wyświetlanie nowej sceny
                                        stage.show();
                                    } else if (result.equals("69")) {
                                        throwAlert("Same login", "Account with this username already exists");
                                    } else if (result.equals("0")) {
                                        throwAlert("Error", "There was an unexpected error during registration");
                                    }

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                throwAlert("Error in password", "Passwords are not the same");
            }
        } else {
            throwAlert("Bad mail", "Please write correct email address");
        }
    }
    @FXML
    protected void goBack() throws IOException {
        // Ładowanie pliku FXML dla nowej sceny
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Tworzenie sceny na podstawie załadowanego pliku FXML
        Scene scene = new Scene(root);

        // Pobieranie obiektu Stage z bieżącego widoku
        Stage stage = (Stage) registration_email_text.getScene().getWindow();

        // Ustawianie nowej sceny na Stage
        stage.setScene(scene);

        // Wyświetlanie nowej sceny
        stage.show();
    }


    void throwAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Wyświetlanie okna alertu
        alert.showAndWait();
    }
}
