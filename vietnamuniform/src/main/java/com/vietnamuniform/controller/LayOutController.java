package com.vietnamuniform.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LayOutController {
    @FXML
    private BorderPane borderPane;


    @FXML
    private void loadView(String file) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadProducts() {
        loadView("/com/vietnamuniform/views/pages/product.fxml");
    }
    @FXML
    public void loadCategories() {
        loadView("/com/vietnamuniform/views/pages/categories.fxml");
    }
    @FXML
    public void loadOders() {
        loadView("/com/vietnamuniform/views/pages/oder.fxml");
    }
    @FXML
    public void loadOder_Details() {
        loadView("/com/vietnamuniform/views/pages/oders-details.fxml");
    }
    @FXML
    public void loadCustomers() {
        loadView("/com/vietnamuniform/views/pages/customer.fxml");
    }
    @FXML
    public void loadReports() {
        loadView("/com/vietnamuniform/views/pages/reports.fxml");
    }
    @FXML
    public void loadPayment() {
        loadView("/com/vietnamuniform/views/pages/payments.fxml");
    }

    public void loadInvoices() {
        loadView("/com/vietnamuniform/views/pages/invoice.fxml");
    }
}
