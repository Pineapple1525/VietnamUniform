package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;


public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label ErrorField;

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (authenticateUser(email, password)) {
            showSuccess("Đăng nhập thành công!");
        } else {
            showError("Bạn đã nhập sai tài khoản hoặc mật khẩu!");
        }
    }

    private boolean authenticateUser(String email, String password) {
        String query = "SELECT password FROM Users WHERE email = ?";
        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
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