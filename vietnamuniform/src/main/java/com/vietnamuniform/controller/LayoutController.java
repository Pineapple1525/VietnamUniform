package com.vietnamuniform.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class LayoutController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private void loadView(String file) {
        try {
           // System.out.println("Loading file: " + file); // Debug đường dẫn
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
           // System.out.println("Error loading file: " + file);
            e.printStackTrace();
        }
    }



    @FXML
    public void loadProducts() {
        loadView("/com/vietnamuniform/views/pages/product.fxml");
    }

    @FXML
    public void loadEmployees() {
        loadView("/com/vietnamuniform/views/pages/employees.fxml");
    }

    @FXML
    public void loadOder() {
        loadView("/com/vietnamuniform/views/pages/oder.fxml");
    }

    @FXML
    public void loadOder_details() {
        loadView("/com/vietnamuniform/views/pages/oders-details.fxml");
    }

    @FXML
    public void loadstatistics() {loadView("/com/vietnamuniform/views/pages/statistics.fxml");}

    @FXML
    public void loadExit() {
        loadView("/com/vietnamuniform/views/pages/exit.fxml");
    }

    @FXML
    public void loadCustomers() {
        loadView("/com/vietnamuniform/views/pages/customer.fxml");
    }
}
