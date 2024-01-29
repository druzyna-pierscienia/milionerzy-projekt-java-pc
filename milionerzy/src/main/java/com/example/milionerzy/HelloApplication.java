package com.example.milionerzy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `HelloApplication` class serves as the entry point for the Milionerzy (Who Wants to Be a Millionaire) application.
 * It extends the `Application` class and is responsible for initializing and displaying the login screen.
 */
public class HelloApplication extends Application {

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method is called when the JavaFX application is launched.
     * It loads the login screen and sets up the initial stage.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If there is an error loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Milionerzy");
        stage.setScene(scene);
        stage.show();
    }
}
