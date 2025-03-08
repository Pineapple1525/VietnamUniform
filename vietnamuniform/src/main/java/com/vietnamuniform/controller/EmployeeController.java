package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Employee, String> colAddress;

    @FXML
    private TableColumn<Employee, LocalDate> colBirthdate;

    @FXML
    private TableColumn<Employee, String> colCodeEmployee;

    @FXML
    private TableColumn<Employee, Integer> colEmployeeId;

    @FXML
    private TableColumn<Employee, String> colFullname;

    @FXML
    private TableColumn<Employee, String> colGender;

    @FXML
    private TableColumn<Employee, String> colPhone;

    @FXML
    private TableColumn<Employee, String> colUsername;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TextField txtSearch;

    private DBConnection dbConnection;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public EmployeeController() {
        dbConnection = new DBConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        employeeList.clear();

        try (Connection connection = dbConnection.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setCodeEmployee(resultSet.getString("code_employee"));
                employee.setFullname(resultSet.getString("fullname"));
                employee.setGender(resultSet.getString("gender"));
                employee.setBirthdate(resultSet.getDate("birthdate") != null ? resultSet.getDate("birthdate").toLocalDate() : null);
                employee.setAddress(resultSet.getString("address"));
                employee.setPhone(resultSet.getString("phone"));
                employee.setUsername(resultSet.getString("username"));
                employeeList.add(employee);
            }

            // Set dữ liệu cho các cột
            colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
            colCodeEmployee.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
            colFullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

            // Đổ dữ liệu vào TableView
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void addEmployee(ActionEvent event) {
        // TODO: Implement add employee
        showAlert(AlertType.INFORMATION, "Thông báo", "Chức năng thêm đang được phát triển!");
    }

    @FXML
    void deleteEmployee(ActionEvent event) {
        // TODO: Implement delete employee
        showAlert(AlertType.INFORMATION, "Thông báo", "Chức năng xóa đang được phát triển!");
    }

    @FXML
    void editEmployee(ActionEvent event) {
        // TODO: Implement edit employee
        showAlert(AlertType.INFORMATION, "Thông báo", "Chức năng sửa đang được phát triển!");
    }

    @FXML
    void searchEmployee(ActionEvent event) {
        // TODO: Implement search employee
        showAlert(AlertType.INFORMATION, "Thông báo", "Chức năng tìm kiếm đang được phát triển!");
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}