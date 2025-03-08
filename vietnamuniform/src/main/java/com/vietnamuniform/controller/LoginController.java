package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;


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
                // Đăng nhập thành công
                String role = resultSet.getString("role");
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Đăng nhập thành công!");
                //Chuyển đến trang tiếp theo dựa vào role

                if ("admin".equals(role)) {
                    // Chuyển đến trang admin (Ví dụ)
                    System.out.println("Đang chuyển đến trang admin");
                } else if ("employee".equals(role)) {
                    // Chuyển đến trang employee (Ví dụ)
                    System.out.println("Đang chuyển đến trang nhân viên");
                } else {
                    System.out.println("Vai trò không xác định");
                }
            } else {
                // Đăng nhập thất bại
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
    //close connection
    public void closeConnection(){
        dbConnection.close();
    }
}