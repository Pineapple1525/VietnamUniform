package com.vietnamuniform.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExitController {

    @FXML
    private Button btnYes;

    @FXML
    private Button btnNo;

    @FXML
    public void handleExit() {
        try {
            // Tải lại giao diện đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vietnamuniform/views/pages/login.fxml"));
            Parent loginView = loader.load();
            Scene loginScene = new Scene(loginView);

            // Lấy cửa sổ hiện tại và chuyển sang giao diện đăng nhập
            Stage stage = (Stage) btnYes.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu cần
        }
    }

    @FXML
    public void handleCancel() {
        // Đóng hộp thoại xác nhận mà không thoát
        Stage stage = (Stage) btnNo.getScene().getWindow();
       // stage.close(); // Đóng hộp thoại xác nhận
        // Không thực hiện hành động nào khác, giữ nguyên trang hiện tại
    }
}