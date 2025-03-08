package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;


    private DBConnection dbConnection;

    public LoginController() {
        dbConnection = new DBConnection();
    }

    @FXML
    void handleSignIn(ActionEvent event) {
        String username = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        try {
            Connection connection = dbConnection.getConn();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Đăng nhập thành công!");

                if ("BOSS".equals(role)) {
                    loadPage("/com/vietnamuniform/views/pages/employees.fxml", event, role);
                } else if ("EMPLOYEE".equals(role)) {
                    loadPage("/com/vietnamuniform/views/pages/employees.fxml", event, role);
                } else if ("MANAGER".equals(role)) {
                    loadPage("/com/vietnamuniform/views/pages/employees.fxml", event, role);
                } else {
                    System.out.println("Vai trò không xác định");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        //TODO Chuyển hướng sang trang đăng ký
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng đăng ký đang được phát triển!");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    private void loadPage(String page, ActionEvent event, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
            Parent root = loader.load();
            //lấy controller
            EmployeeController employeeController = loader.getController();
            employeeController.setCurrentRole(role);
            // tạo scene
            Scene scene = new Scene(root);
            //Lấy stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            //set scene
            stage.setScene(scene);
            //show scene
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}