package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Timestamp;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


import java.sql.*;
import java.time.LocalDate;

public class OrderController {
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> orderIdColumn, employeeCodeColumn, employeeNameColumn, customerCodeColumn, orderDateColumn;
    @FXML private TextField searchField, orderIdField, employeeNameField;
    @FXML private DatePicker orderDatePicker;
    @FXML private ComboBox<String> employeeIdComboBox, customerIdComboBox;
    @FXML private Button addButton, editButton, deleteButton;

    private Connection conn;

    @FXML
    public void initialize() {
        conn = new DBConnection().getConn();
        setupTableColumns();
        loadOrders();
        loadEmployeeIds();
        loadCustomerIds();
        orderTable.setOnMouseClicked(event -> fillOrderFields());
        orderDatePicker.setValue(LocalDate.now());
        customerIdComboBox.setOnMouseClicked(event -> fillOrderFields());

        // Kiểm tra xem có gọi được phương thức khi chọn mã nhân viên không
        employeeIdComboBox.setOnAction(event -> {
            System.out.println("Chọn mã nhân viên: " + employeeIdComboBox.getValue());
            fillEmployeeName();
        });
    }


    private void setupTableColumns() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("codeOrder"));
        employeeCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        customerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codeCustomer"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    }

    private void loadOrders() {
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orderList.add(new Order(
                        rs.getString("code_order"),
                        rs.getString("code_employee"),
                        rs.getString("employee_name"),
                        rs.getString("code_customer"),
                        rs.getString("order_date")
                ));
            }
            orderTable.setItems(orderList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể tải đơn hàng: " + e.getMessage());
        }
    }

    private void loadEmployeeIds() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT code_employee FROM employees";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String employeeCode = rs.getString(1);
                System.out.println("Tìm thấy mã nhân viên: " + employeeCode);
                items.add(employeeCode);
            }
            employeeIdComboBox.setItems(items);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }


    private void loadCustomerIds() {
        loadComboBox("SELECT DISTINCT code_customer FROM customers", customerIdComboBox);
    }

    private void loadComboBox(String query, ComboBox<String> comboBox) {
        ObservableList<String> items = FXCollections.observableArrayList();
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(rs.getString(1));
            }
            comboBox.setItems(items);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void fillEmployeeName() {
        String employeeCode = employeeIdComboBox.getValue();

        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            System.out.println("Không có mã nhân viên được chọn.");
            employeeNameField.setText("");  // Xóa ô nhập liệu nếu không có dữ liệu
            return;
        }

        System.out.println("Mã nhân viên được chọn: " + employeeCode);

        // Nếu employeeCode là mã nhân viên, sửa lại truy vấn này
        String query = "SELECT fullname FROM employees WHERE code_employee = ?";


        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String employeeName = rs.getString("fullname"); // Lấy tên từ cột fullname
                System.out.println("Tên nhân viên lấy được từ DB: " + employeeName);
                employeeNameField.setText(employeeName);
            } else {
                System.out.println("Không tìm thấy nhân viên có mã: " + employeeCode);
                employeeNameField.setText("");  // Nếu không tìm thấy, làm trống ô nhập
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể lấy tên nhân viên: " + e.getMessage());
        }
    }
    private String getEmployeeName(String employeeCode) {
        String query = "SELECT fullname FROM employees WHERE     code_employee  = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("fullname");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể lấy tên nhân viên: " + e.getMessage());
        }
        return null;
    }

    private Timestamp getCurrentOrderDate(String orderId) {
        String sql = "SELECT order_date FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("order_date");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể lấy ngày cũ: " + e.getMessage());
        }
        return null;
    }





    @FXML
    private void addOrder() {
        // Execute the order update query
        executeOrderUpdate("INSERT INTO orders (code_order, code_employee, employee_name, code_customer, order_date) VALUES (?, ?, ?, ?, ?)", "Đơn hàng đã được thêm.");
    }


    @FXML
    private void editOrder() {
        executeOrderUpdate("UPDATE orders SET code_employee = ?, employee_name = ?, code_customer = ?, order_date = ? WHERE code_order = ?", "Đã cập nhật đơn hàng.");
    }


    @FXML
    private void executeOrderUpdate(String query, String successMessage) {
        String orderId = orderIdField.getText().trim();
        String employeeCode = employeeIdComboBox.getValue();
        String customerCode = customerIdComboBox.getValue();

        if (orderId.isEmpty() || employeeCode == null || customerCode == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Lấy employee_name từ bảng employees
        String employeeName = getEmployeeName(employeeCode);
        if (employeeName == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy tên nhân viên.");
            return;
        }

        Timestamp orderTimestamp;

        // Nếu là UPDATE, kiểm tra xem người dùng có chọn ngày mới không
        if (query.startsWith("UPDATE")) {
            LocalDate selectedDate = orderDatePicker.getValue();
            if (selectedDate != null) {
                orderTimestamp = Timestamp.valueOf(selectedDate.atStartOfDay()); // Ngày người dùng chọn
            } else {
                orderTimestamp = getCurrentOrderDate(orderId); // Lấy ngày cũ từ CSDL
            }
        } else {
            orderTimestamp = new Timestamp(System.currentTimeMillis()); // Ngày hiện tại khi thêm mới
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (query.startsWith("UPDATE")) {
                stmt.setString(1, employeeCode);
                stmt.setString(2, employeeName);
                stmt.setString(3, customerCode);
                stmt.setTimestamp(4, orderTimestamp); // Sử dụng ngày hợp lệ
                stmt.setString(5, orderId);
            } else { // INSERT
                stmt.setString(1, orderId);
                stmt.setString(2, employeeCode);
                stmt.setString(3, employeeName);
                stmt.setString(4, customerCode);
                stmt.setTimestamp(5, orderTimestamp);
            }

            if (stmt.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", successMessage);
                loadOrders();
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể cập nhật dữ liệu: " + e.getMessage());
        }
    }



    @FXML
    private void deleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn đơn hàng để xóa.");
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders WHERE code_order = ?")) {
            stmt.setString(1, selectedOrder.getCodeOrder());
            if (stmt.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đơn hàng đã được xóa.");
                loadOrders();
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể xóa đơn hàng: " + e.getMessage());
        }
    }
    @FXML
    private void fillOrderFields() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        orderIdField.setText(selectedOrder.getCodeOrder());
        employeeIdComboBox.setValue(selectedOrder.getCodeEmployee());
        employeeNameField.setText(selectedOrder.getEmployeeName());
        customerIdComboBox.setValue(selectedOrder.getCodeCustomer());

        // Xử lý ngày hợp lệ trước khi gán vào DatePicker
        try {
            if (selectedOrder.getOrderDate() != null && !selectedOrder.getOrderDate().isEmpty()) {
                // Chỉ lấy phần ngày (yyyy-MM-dd) từ chuỗi ngày
                String datePart = selectedOrder.getOrderDate().split(" ")[0]; // Lấy phần yyyy-MM-dd
                orderDatePicker.setValue(LocalDate.parse(datePart));
            } else {
                orderDatePicker.setValue(null);
            }
        } catch (Exception e) {
            orderDatePicker.setValue(null);
            System.out.println("Lỗi khi chuyển đổi ngày: " + e.getMessage());
        }
    }

    @FXML
    private void clearFields() {
        orderIdField.clear();
        employeeIdComboBox.setValue(null);
        employeeNameField.clear();
        customerIdComboBox.setValue(null);
        orderDatePicker.setValue(null);
    }
    @FXML
    private void searchOrder() {
        String searchText = searchField.getText().trim();
        ObservableList<Order> filteredList = FXCollections.observableArrayList();
        String searchQuery = "SELECT * FROM orders WHERE code_order LIKE ? OR code_employee LIKE ? OR code_customer LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            stmt.setString(3, "%" + searchText + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filteredList.add(new Order(rs.getString("code_order"), rs.getString("code_employee"), rs.getString("employee_name"), rs.getString("code_customer"), rs.getString("order_date")));
            }
            orderTable.setItems(filteredList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi SQL", "Không thể tìm kiếm đơn hàng: " + e.getMessage());
        }
    }





    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}