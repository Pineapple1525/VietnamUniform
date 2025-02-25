package com.vietnamuniform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
    Updated upstream:vietnamuniform/src/main/java/com/vietnamuniform/HelloApplication.java
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1038, 627);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vietnamuniform/views/pages/sizebar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Việt phục Việt Nam ");
        stage.setScene(scene);
        stage.show();
        
/Stashed changes:vietnamuniform/src/main/java/com/vietnamuniform/Main.java
    }


    public static void main(String[] args) {
        launch();
    }
}