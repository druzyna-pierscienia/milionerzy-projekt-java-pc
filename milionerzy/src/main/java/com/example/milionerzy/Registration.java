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

/**
 * Controller class for user registration functionality.
 * Allows users to register by providing a unique username, a valid email address, and a matching password.
 */
public class Registration {

    @FXML
    private PasswordField registration_password_text;
    @FXML
    private PasswordField registration_repeatPassword_text;
    @FXML
    private TextField registration_email_text;
    @FXML
    private TextField registration_username_text;

    /**
     * Handles the user registration process.
     * Validates the provided email address, checks if passwords match, and communicates with the server to register the user.
     * Displays appropriate alerts based on the registration result.
     */
    @FXML
    protected void register() {
        // Validation pattern for a valid email address
        String regexMail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        Pattern patternMail = Pattern.compile(regexMail);
        Matcher matcher = patternMail.matcher(registration_email_text.getText().toString().trim());

        if (matcher.matches()) {
            if (registration_password_text.getText().toString().trim().equals(registration_repeatPassword_text.getText().toString().trim())) {
                // Send a registration request to the server
                String url = "http://localhost:8080/register?login=" + registration_username_text.getText().toString().trim() +
                        "&password=" + registration_password_text.getText().toString().trim() +
                        "&mail=" + registration_email_text.getText().toString().trim();
                ApiRequestInto.executeRequest(url, "", new ApiRequestInto.ApiCallback() {
                    @Override
                    public void onResponse(String result) {
                        Platform.runLater(() -> {
                            try {
                                if (result.equals("420")) {
                                    // Loading the FXML file for the login scene
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                                    Parent root = loader.load();

                                    // Creating a scene based on the loaded FXML file
                                    Scene scene = new Scene(root);

                                    // Getting the Stage object from the current view
                                    Stage stage = (Stage) registration_email_text.getScene().getWindow();

                                    // Setting the new scene on Stage
                                    stage.setScene(scene);

                                    // Displaying the new scene
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

    /**
     * Navigates back to the login screen.
     *
     * @throws IOException If an error occurs while loading the login.fxml file.
     */
    @FXML
    protected void goBack() throws IOException {
        // Loading the FXML file for the login scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Creating a scene based on the loaded FXML file
        Scene scene = new Scene(root);

        // Getting the Stage object from the current view
        Stage stage = (Stage) registration_email_text.getScene().getWindow();

        // Setting the new scene on Stage
        stage.setScene(scene);

        // Displaying the new scene
        stage.show();
    }

    /**
     * Displays an alert dialog with the specified title and content.
     *
     * @param title   The title of the alert.
     * @param content The content text of the alert.
     */
    void throwAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Displaying the alert window
        alert.showAndWait();
    }
}

