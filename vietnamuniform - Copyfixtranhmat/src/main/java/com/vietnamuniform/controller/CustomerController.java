package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class CustomerController {
    @FXML
    private TextField txtCodeCustomer, txtFullname, txtAddress, txtPhone, searchField;
    @FXML
    private ComboBox<String> cmbGender, cmbCustomerType;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> colCustomerId;
    @FXML
    private TableColumn<Customer, String> colCodeCustomer, colFullname, colAddress, colGender, colPhone, colCustomerType;
    @FXML
    private Button btnAdd, btnUpdate, btnDelete, searchButton;

    private Connection conn;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void initialize() {
        conn = new DBConnection().getConn();
        loadData();

        // Cài đặt dữ liệu cho ComboBox
        cmbGender.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        cmbCustomerType.setItems(FXCollections.observableArrayList("Sinh viên", "VIP", "Thường"));

        // Thiết lập các cột cho TableView
        colCodeCustomer.setCellValueFactory(new PropertyValueFactory<>("codeCustomer"));
        colFullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCustomerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setCustomerFields(newSelection);
            }
        });

        searchButton.setOnAction(event -> searchCustomer());
    }

    private void loadData() {
        customerList.clear();
        try {
            String sql = "SELECT * FROM customers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("code_customer"),
                        rs.getString("fullname"),
                        rs.getString("address"),
                        convertGenderFromDB(rs.getString("gender")),
                        rs.getString("phone"),
                        convertCustomerTypeFromDB(rs.getString("customer_type"))
                ));
            }
            customerTable.setItems(customerList);
            System.out.println("Total Customers Loaded: " + customerList.size()); // Debug line
        } catch (SQLException e) {
            showError("Lỗi tải dữ liệu!", e.getMessage());
        }
    }


    @FXML
    private void addCustomer() {
        if (!validateInput()) return;

        String codeCustomer = txtCodeCustomer.getText();
        String fullname = txtFullname.getText();
        String address = txtAddress.getText();
        String gender = convertGenderToDB(cmbGender.getValue());
        String phone = txtPhone.getText();
        String customerType = convertCustomerTypeToDB(cmbCustomerType.getValue());

        try {
            String sql = "INSERT INTO customers (code_customer, fullname, address, gender, phone, customer_type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codeCustomer);
            stmt.setString(2, fullname);
            stmt.setString(3, address);
            stmt.setString(4, gender);
            stmt.setString(5, phone);
            stmt.setString(6, customerType);
            stmt.executeUpdate();
            loadData();
            clearFields();
            showInfo("Thêm khách hàng thành công!");
        } catch (SQLException e) {
            showError("Lỗi khi thêm khách hàng!", e.getMessage());
        }
    }

    @FXML
    private void updateCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn khách hàng để cập nhật!");
            return;
        }
        if (!validateInput()) return;

        try {
            String sql = "UPDATE customers SET code_customer=?, fullname=?, address=?, gender=?, phone=?, customer_type=? WHERE customer_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtCodeCustomer.getText());
            stmt.setString(2, txtFullname.getText());
            stmt.setString(3, txtAddress.getText());
            stmt.setString(4, convertGenderToDB(cmbGender.getValue()));
            stmt.setString(5, txtPhone.getText());
            stmt.setString(6, convertCustomerTypeToDB(cmbCustomerType.getValue()));
            stmt.setInt(7, selected.getCustomerId());
            stmt.executeUpdate();
            loadData();
            clearFields();
            showInfo("Cập nhật khách hàng thành công!");
        } catch (SQLException e) {
            showError("Lỗi khi cập nhật khách hàng!", e.getMessage());
        }
    }

    @FXML
    private void deleteCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn khách hàng để xóa!");
            return;
        }

        try {
            String sql = "DELETE FROM customers WHERE customer_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getCustomerId());
            stmt.executeUpdate();
            loadData();
            clearFields();
            showInfo("Xóa khách hàng thành công!");
        } catch (SQLException e) {
            showError("Lỗi khi xóa khách hàng!", e.getMessage());
        }
    }

    private void searchCustomer() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        customerList.clear();
        try {
            String sql = "SELECT * FROM customers WHERE fullname LIKE ? OR phone LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("code_customer"),
                        rs.getString("fullname"),
                        rs.getString("address"),
                        convertGenderFromDB(rs.getString("gender")),
                        rs.getString("phone"),
                        convertCustomerTypeFromDB(rs.getString("customer_type"))
                ));
            }
            customerTable.setItems(customerList);
        } catch (SQLException e) {
            showError("Lỗi khi tìm kiếm khách hàng!", e.getMessage());
        }
    }

    private void setCustomerFields(Customer customer) {
        txtCodeCustomer.setText(customer.getCodeCustomer());
        txtFullname.setText(customer.getFullname());
        txtAddress.setText(customer.getAddress());
        cmbGender.setValue(customer.getGender());
        txtPhone.setText(customer.getPhone());
        cmbCustomerType.setValue(customer.getCustomerType());
    }

    private boolean validateInput() {
        if (txtCodeCustomer.getText().isEmpty() || txtFullname.getText().isEmpty() || txtPhone.getText().isEmpty() || cmbGender.getValue() == null || cmbCustomerType.getValue() == null) {
            showWarning("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtCodeCustomer.clear();
        txtFullname.clear();
        txtAddress.clear();
        txtPhone.clear();
        cmbGender.setValue(null);
        cmbCustomerType.setValue(null);
    }

    private void showError(String title, String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }

    private void showWarning(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).show();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).show();
    }

    private String convertGenderFromDB(String gender) {
        return switch (gender) {
            case "MEN" -> "Nam";
            case "WOMEN" -> "Nữ";
            case "OTH" -> "Khác";
            default -> "Không xác định";
        };
    }

    private String convertGenderToDB(String gender) {
        return switch (gender) {
            case "Nam" -> "MEN";
            case "Nữ" -> "WOMEN";
            case "Khác" -> "OTH";
            default -> null;
        };
    }

    private String convertCustomerTypeFromDB(String customerType) {
        return switch (customerType) {
            case "Student" -> "Sinh viên";
            case "VIP" -> "VIP";
            case "NOR" -> "Thường";
            default -> "Không xác định";
        };
    }

    private String convertCustomerTypeToDB(String customerType) {
        return switch (customerType) {
            case "Sinh viên" -> "Student";
            case "VIP" -> "VIP";
            case "Thường" -> "NOR";
            default -> null;
        };
    }
}