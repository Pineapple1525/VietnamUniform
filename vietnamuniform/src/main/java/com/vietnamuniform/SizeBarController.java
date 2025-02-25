package com.vietnamuniform.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SizeBarController {
   @FXML
   private BorderPane borderPane;


    @FXML
    private void loadOverview(){
        loadView("/com/vietnamuniform/views/pages/sizebar.fxml");
    }

    @FXML
    private void loadView(String file){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
