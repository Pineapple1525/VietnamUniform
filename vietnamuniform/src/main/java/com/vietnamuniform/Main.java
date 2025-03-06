package com.vietnamuniform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vietnamuniform/views/pages/main.fxml"));
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