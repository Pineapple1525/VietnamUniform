package com.vietnamuniform.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ExitController {

    // Xử lý khi ấn nút Yes
    @FXML
    private void handleExit(ActionEvent event) {
        try {
            // 1. Đóng dialog xác nhận
            Stage currentDialog = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentDialog.close();

            // 2. Tải lại trang đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vietnamuniform/views/pages/login.fxml"));
            Parent loginRoot = loader.load();

            // 3. Lấy main stage từ dialog
            Stage mainStage = (Stage) currentDialog.getOwner(); // Giả định dialog được mở từ main stage

            // 4. Chuyển scene cho main stage
            mainStage.setScene(new Scene(loginRoot));
            mainStage.centerOnScreen(); // Căn giữa màn hình

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần (ví dụ: hiển thị thông báo)
        }
    }

    // Xử lý khi ấn nút No
    @FXML
    private void handleCancel(ActionEvent event) {
        // Chỉ đóng dialog xác nhận, không làm gì thêm
        Stage currentDialog = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentDialog.close();
    }
}