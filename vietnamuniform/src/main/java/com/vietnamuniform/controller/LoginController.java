package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label ErrorField;
    @FXML
    private AnchorPane root;

    private Connection conn;

    @FXML
    public void initialize() {
        conn = new DBConnection().getConn();
        passwordField.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (authenticateUser(username, password)) {
            showSuccess("Đăng nhập thành công!");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("/com/vietnamuniform/views/pages/main.fxml"));
                Parent newContent = fxmlLoader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene newScene = new Scene(newContent);
                stage.setScene(newScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Lỗi khi chuyển trang: " + e.getMessage());
            }
        } else {
            showError("Bạn đã nhập sai tài khoản hoặc mật khẩu!");
        }
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM Users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return password.equals(storedPassword); // So sánh trực tiếp
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private void showError(String message) {
        ErrorField.setText(message);
        ErrorField.setTextFill(Color.RED);
    }

    private void showSuccess(String message) {
        ErrorField.setText(message);
        ErrorField.setTextFill(Color.GREEN);
    }
}