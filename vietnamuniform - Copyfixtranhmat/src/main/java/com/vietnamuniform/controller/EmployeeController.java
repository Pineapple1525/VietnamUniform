package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class EmployeeController {
    @FXML
    private TextField txtCodeEmployee, txtFullname, txtAddress, txtPhone, txtUsername, txtPassword, searchField;
    @FXML
    private ComboBox<String> cmbGender, cmbRole;
    @FXML
    private DatePicker txtBirthdate;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> colId;
    @FXML
    private TableColumn<Employee, String> colCodeEmployee, colFullname, colGender, colBirthdate, colAddress, colPhone, colUsername, colPassword, colRole;
    @FXML
    private Button btnAdd, btnUpdate, btnDelete, searchButton;

    private Connection conn;
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public void initialize() {
        conn = new DBConnection().getConn();
        loadData();

        // Cài đặt dữ liệu cho ComboBox
        cmbGender.setItems(FXCollections.observableArrayList("Men", "Women", "Other"));
        cmbRole.setItems(FXCollections.observableArrayList("Boss", "Manager", "Employee"));

        // Thiết lập các cột cho TableView
        setUpTableColumns();

        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setEmployeeFields(newSelection);
            }
        });

        searchButton.setOnAction(event -> searchEmployee());
    }

    private void setUpTableColumns() {
        colCodeEmployee.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
        colFullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadData() {
        employeeList.clear();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeeList.add(createEmployeeFromResultSet(rs));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showError("Lỗi tải dữ liệu!", e.getMessage());
        }
    }

    private Employee createEmployeeFromResultSet(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("code_employee"),
                rs.getString("fullname"),
                convertGenderFromDB(rs.getString("gender")),
                rs.getDate("birthdate"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("username"),
                rs.getString("password"),
                convertRoleFromDB(rs.getString("role"))
        );
    }

    @FXML
    private void handleAddAction() {
        if (!validateInput()) return;
        String sql = "INSERT INTO employees (code_employee, fullname, gender, birthdate, address, phone, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, false);
    }

    @FXML
    private void handleUpdateAction() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhân viên để cập nhật!");
            return;
        }
        if (!validateInput()) return;

        String sql = "UPDATE employees SET code_employee=?, fullname=?, gender=?, birthdate=?, address=?, phone=?, username=?, password=?, role=? WHERE employee_id=?";
        executeUpdate(sql, true, selected.getEmployeeId());
    }

    @FXML
    private void handleDeleteAction() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getEmployeeId());
            stmt.executeUpdate();
            loadData();
            clearFields();
            showInfo("Xóa nhân viên thành công!");
        } catch (SQLException e) {
            showError("Lỗi khi xóa nhân viên!", e.getMessage());
        }
    }

    private void executeUpdate(String sql, boolean isUpdate, Integer... id) {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStatementParameters(stmt, isUpdate, id);
            stmt.executeUpdate();
            loadData();
            clearFields();
            showInfo(isUpdate ? "Cập nhật nhân viên thành công!" : "Thêm nhân viên thành công!");
        } catch (SQLException e) {
            showError(isUpdate ? "Lỗi khi cập nhật nhân viên!" : "Lỗi khi thêm nhân viên!", e.getMessage());
        }
    }

    private void setStatementParameters(PreparedStatement stmt, boolean isUpdate, Integer... id) throws SQLException {
        stmt.setString(1, txtCodeEmployee.getText());
        stmt.setString(2, txtFullname.getText());
        stmt.setString(3, convertGenderToDB(cmbGender.getValue()));
        stmt.setDate(4, Date.valueOf(txtBirthdate.getValue()));
        stmt.setString(5, txtAddress.getText());
        stmt.setString(6, txtPhone.getText());
        stmt.setString(7, txtUsername.getText());
        stmt.setString(8, txtPassword.getText());
        stmt.setString(9, convertRoleToDB(cmbRole.getValue())); // Thêm dòng này
        if (isUpdate) {
            stmt.setInt(10, id[0]); // Tham số thứ 10 cho employee_id khi cập nhật
        }
    }

    private void searchEmployee() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        employeeList.clear();
        String sql = "SELECT * FROM employees WHERE fullname LIKE ? OR phone LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employeeList.add(createEmployeeFromResultSet(rs));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showError("Lỗi khi tìm kiếm nhân viên!", e.getMessage());
        }
    }

    private void setEmployeeFields(Employee employee) {
        txtCodeEmployee.setText(employee.getCodeEmployee());
        txtFullname.setText(employee.getFullname());
        cmbGender.setValue(employee.getGender());
        txtBirthdate.setValue(employee.getBirthdate().toLocalDate());
        txtAddress.setText(employee.getAddress());
        txtPhone.setText(employee.getPhone());
        txtUsername.setText(employee.getUsername());
        txtPassword.setText(employee.getPassword());
        cmbRole.setValue(employee.getRole());
    }

    private boolean validateInput() {
        if (txtCodeEmployee.getText().isEmpty() ||
                txtFullname.getText().isEmpty() ||
                txtPhone.getText().isEmpty() ||
                txtUsername.getText().isEmpty() ||
                txtPassword.getText().isEmpty() ||
                cmbGender.getValue() == null ||
                cmbRole.getValue() == null) {
            showWarning("Vui lòng điền đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtCodeEmployee.clear();
        txtFullname.clear();
        cmbGender.setValue(null);
        txtBirthdate.setValue(null);
        txtAddress.clear();
        txtPhone.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbRole.setValue(null);
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
            case "MEN" -> "Men";
            case "WOMEN" -> "Women";
            case "OTH" -> "Other";
            default -> "Không xác định";
        };
    }

    private String convertGenderToDB(String gender) {
        return switch (gender) {
            case "Men" -> "MEN";
            case "Women" -> "WOMEN";
            case "Other" -> "OTH";
            default -> null;
        };
    }

    private String convertRoleFromDB(String role) {
        return role; // Assuming role is already in the correct format
    }

    private String convertRoleToDB(String role) {
        return switch (role) {
            case "Boss" -> "Boss";
            case "Manager" -> "Manager";
            case "Employee" -> "Employee";
            default -> null; // Trả về null nếu không phải giá trị hợp lệ
        };
    }
}