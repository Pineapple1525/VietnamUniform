package com.vietnamuniform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vietnamuniform/views/pages/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Việt phục Việt Nam");
        stage.setScene(scene);
        stage.show();
        // hi

    }

    public static void main(String[] args) {
        launch();
    }
}