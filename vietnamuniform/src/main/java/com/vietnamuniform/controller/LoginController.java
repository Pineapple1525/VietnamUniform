package com.vietnamuniform.controller;

import com.vietnamuniform.connect.ConnectDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            ErrorField.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (authenticateUser(email, password)) {
            ErrorField.setText("Đăng nhập thành công!");
        } else {
            ErrorField.setText("Bạn đã nhập sai tài khoản hoặc mật khẩu!");
        }
    }

    private boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
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
}

