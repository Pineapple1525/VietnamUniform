package com.vietnamuniform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
=======
>>>>>>> 2f4a0c42791095825a19a27b8749ee3c1c43cf49
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< HEAD
    Updated upstream:vietnamuniform/src/main/java/com/vietnamuniform/HelloApplication.java
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1038, 627);
=======
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vietnamuniform/views/pages/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
>>>>>>> 2f4a0c42791095825a19a27b8749ee3c1c43cf49
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

<<<<<<< HEAD
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vietnamuniform/views/pages/sizebar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Việt phục Việt Nam ");
        stage.setScene(scene);
        stage.show();
        
/Stashed changes:vietnamuniform/src/main/java/com/vietnamuniform/Main.java
    }


=======
    }

>>>>>>> 2f4a0c42791095825a19a27b8749ee3c1c43cf49
    public static void main(String[] args) {
        launch();
    }
}