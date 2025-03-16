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
    private TableColumn<Employee, String> colCodeEmployee, colFullname, colGender, colBirthdate, colAddress, colPhone, colUsername, colPassword, colRole;
    @FXML
    private Button btnAdd, btnUpdate, btnDelete, searchButton;

    private Connection conn;
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public void initialize() {
        this.conn = DBConnection.getConn();
        if (conn == null) {
            showError("Lỗi", "Không thể kết nối đến cơ sở dữ liệu!");
            return;
        }
        loadData();

        // Set up ComboBox data
        cmbGender.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        cmbRole.setItems(FXCollections.observableArrayList("Chủ", "Quản lý", "Nhân viên"));

        // Set up TableView columns
        setUpTableColumns();

        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setEmployeeFields(newSelection);
            }
        });
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
                rs.getString("gender"),
                rs.getDate("birthdate"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("username"),
                rs.getString("password"),
                convertRoleToDisplay(rs.getString("role"))
        );
    }

    @FXML
    private void handleAddAction() {
        if (!validateInput()) return;

        // Lấy giá trị role từ ComboBox
        String role = cmbRole.getValue() != null ? convertRole(cmbRole.getValue()) : "Employee";

        try {
            conn.setAutoCommit(false);

            // Thêm vào bảng users
            String insertUserSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql)) {
                userStmt.setString(1, txtUsername.getText());
                userStmt.setString(2, txtPassword.getText());
                userStmt.setString(3, role); // Vai trò chính xác
                userStmt.executeUpdate();
            }

            // Thêm vào bảng employees
            String insertEmployeeSql = "INSERT INTO employees (code_employee, fullname, gender, birthdate, address, phone, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement employeeStmt = conn.prepareStatement(insertEmployeeSql)) {
                setStatementParameters(employeeStmt, false);
                employeeStmt.executeUpdate();
            }

            conn.commit(); // Commit cả hai bảng
            loadData();
            clearFields();
            showInfo("Thêm nhân viên thành công!");
        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                showError("Lỗi khi rollback!", ex.getMessage());
            }
            showError("Lỗi khi thêm nhân viên!", e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); // Khôi phục auto-commit
            } catch (SQLException e) {
                showError("Lỗi khi khôi phục auto-commit!", e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateAction() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhân viên để cập nhật!");
            return;
        }
        if (!validateInput()) return;

        // Update logic (no need to update users table if username doesn't change)
        String sql = "UPDATE employees SET code_employee=?, fullname=?, gender=?, birthdate=?, address=?, phone=?, username=?, password=?, role=? WHERE employee_id=?";
        try {
            executeUpdate(sql, true, selected.getEmployeeId());
            // If username changes, update users table as well (optional)
            String updateUserSql = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(updateUserSql)) {
                userStmt.setString(1, txtPassword.getText());
                userStmt.setString(2, selected.getUsername()); // Use old username
                userStmt.executeUpdate();
            }
            showInfo("Cập nhật nhân viên thành công!");
        } catch (SQLException e) {
            showError("Lỗi khi cập nhật nhân viên!", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAction() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        // Delete will cascade to users due to ON DELETE CASCADE
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

    private void executeUpdate(String sql, boolean isUpdate, Integer... id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStatementParameters(stmt, isUpdate, id);
            stmt.executeUpdate();
        }
    }

    private void setStatementParameters(PreparedStatement stmt, boolean isUpdate, Integer... id) throws SQLException {
        stmt.setString(1, txtCodeEmployee.getText());
        stmt.setString(2, txtFullname.getText());
        stmt.setString(3, cmbGender.getValue() != null ? convertGender(cmbGender.getValue()) : "OTH");
        stmt.setDate(4, txtBirthdate.getValue() != null ? Date.valueOf(txtBirthdate.getValue()) : null);
        stmt.setString(5, txtAddress.getText());
        stmt.setString(6, txtPhone.getText());
        stmt.setString(7, txtUsername.getText());
        stmt.setString(8, txtPassword.getText());
        stmt.setString(9, cmbRole.getValue() != null ? convertRole(cmbRole.getValue()) : "Employee");
        if (isUpdate) {
            stmt.setInt(10, id[0]);
        }
    }

    private String convertGender(String gender) {
        switch (gender) {
            case "Nam":
                return "MEN";
            case "Nữ":
                return "WOMEN";
            case "Khác":
                return "OTH";
            default:
                return "OTH";
        }
    }

    private String convertRole(String role) {
        switch (role) {
            case "Chủ":
                return "Boss";
            case "Quản lý":
                return "Manager"; // Chuyển thành Manager
            case "Nhân viên":
                return "Employee";
            default:
                return "Employee"; // Giá trị mặc định
        }
    }

    private String convertRoleToDisplay(String dbRole) {
        switch (dbRole) {
            case "Boss":
                return "Chủ";
            case "Manager":
                return "Quản lý";
            case "Employee":
                return "Nhân viên";
            default:
                return "Nhân viên"; // Giá trị mặc định
        }
    }

    @FXML
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
        String genderDisplay;
        switch (employee.getGender()) {
            case "MEN":
                genderDisplay = "Nam";
                break;
            case "WOMEN":
                genderDisplay = "Nữ";
                break;
            case "OTH":
                genderDisplay = "Khác";
                break;
            default:
                genderDisplay = "Khác";
        }
        cmbGender.setValue(genderDisplay);
        txtBirthdate.setValue(employee.getBirthdate() != null ? employee.getBirthdate().toLocalDate() : null);
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
                txtBirthdate.getValue() == null ||
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
}