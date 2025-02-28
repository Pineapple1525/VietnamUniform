package com.vietnamuniform.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import com.vietnamuniform.connect.ConnectDatabase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField reEnterPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorField;

    @FXML
    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String rePassword = reEnterPasswordField.getText();


        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!password.equals(rePassword)) {
            showError("Mật khẩu nhập lại không khớp.");
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showError("Email không hợp lệ.");
            return;
        }

        if (isEmailExists(email)) {
            showError("Email đã được sử dụng. Vui lòng chọn email khác.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (registerUser(email, hashedPassword)) {
            showSuccess("Đăng ký thành công!");
        } else {
            showError("Đăng ký thất bại. Vui lòng thử lại.");
        }
    }

    private boolean isEmailExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String email, String hashedPassword) {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showError(String message) {
        errorField.setText(message);
        errorField.setTextFill(Color.RED);
    }

    private void showSuccess(String message) {
        errorField.setText(message);
        errorField.setTextFill(Color.GREEN);
    }
}