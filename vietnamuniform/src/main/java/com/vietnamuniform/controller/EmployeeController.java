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
import javafx.scene.layout.GridPane;

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

    // Các thành phần cho add/edit
    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtFullname;

    @FXML
    private ComboBox<String> cbGender;

    @FXML
    private DatePicker dpBirthdate;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lbHeader;

    @FXML
    private GridPane employeeFormGrid;

    private DBConnection dbConnection;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private String currentRole;
    private Employee selectedEmployee;

    public EmployeeController() {
        dbConnection = new DBConnection();
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbGender.getItems().addAll("MEN", "WOMEN", "OTH");
        cbGender.setValue("MEN");
        loadEmployeeData();
        checkRole();
        employeeFormGrid.setVisible(false);
    }

    private void checkRole() {
        if (currentRole != null && !currentRole.equals("BOSS")) {
            btnAdd.setVisible(false);
            btnDelete.setVisible(false);
            btnEdit.setVisible(false);
        }
    }

    private void loadEmployeeData() {
        employeeList.clear();
        Connection connection = dbConnection.getConn();
        try (
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

            colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
            colCodeEmployee.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
            colFullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

            employeeTable.setItems(employeeList);
            employeeTable.refresh();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void addEmployee(ActionEvent event) {
        selectedEmployee = null; // Đánh dấu là thêm mới
        lbHeader.setText("Thêm nhân viên");
        showForm();
        clearForm();
    }

    @FXML
    void deleteEmployee(ActionEvent event) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn nhân viên để xóa.");
            return;
        }

        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Xác nhận");
        confirmation.setHeaderText("Bạn có chắc chắn muốn xóa nhân viên này?");
        confirmation.setContentText("Tên nhân viên: " + selectedEmployee.getFullname());
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Connection connection = dbConnection.getConn();
                try {
                    //xóa user trước
                    String sqlDeleteUser = "DELETE FROM users WHERE username = ?";
                    PreparedStatement preparedStatementUser = connection.prepareStatement(sqlDeleteUser);
                    preparedStatementUser.setString(1, selectedEmployee.getUsername());
                    preparedStatementUser.executeUpdate();
                    //sau đó xóa nhân viên
                    String sqlDeleteEmployee = "DELETE FROM employees WHERE employee_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteEmployee);
                    preparedStatement.setInt(1, selectedEmployee.getEmployeeId());
                    preparedStatement.executeUpdate();

                    employeeList.remove(selectedEmployee);
                    showAlert(AlertType.INFORMATION, "Thành công", "Xóa nhân viên thành công.");
                    loadEmployeeData();
                } catch (SQLException e) {
                    showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi xóa nhân viên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void editEmployee(ActionEvent event) {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn nhân viên để sửa.");
            return;
        }
        lbHeader.setText("Sửa thông tin nhân viên");
        showForm();
        loadEmployeeToForm(selectedEmployee);

    }

    @FXML
    void searchEmployee(ActionEvent event) {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadEmployeeData();
            return;
        }

        ObservableList<Employee> searchResults = FXCollections.observableArrayList();
        for (Employee employee : employeeList) {
            if (employee.getFullname().toLowerCase().contains(keyword) || employee.getCodeEmployee().toLowerCase().contains(keyword)) {
                searchResults.add(employee);
            }
        }

        employeeTable.setItems(searchResults);
    }

    @FXML
    void saveEmployee(ActionEvent event) {
        String code = txtCode.getText();
        String fullname = txtFullname.getText();
        String gender = cbGender.getValue();
        LocalDate birthdate = dpBirthdate.getValue();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String username = txtUsername.getText();

        if (code.isEmpty() || fullname.isEmpty() || gender.isEmpty() || phone.isEmpty() || username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        Connection connection = dbConnection.getConn();
        try {
            if (selectedEmployee == null) {
                if (!isUsernameExists(username)) {
                    if (!addUserToUsersTable(username)) {
                        showAlert(AlertType.ERROR, "Lỗi", "Không thể thêm user mới vào bảng users.");
                        return;
                    }
                }
                String sql = "INSERT INTO employees (code_employee, fullname, gender, birthdate, address, phone, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, code);
                    preparedStatement.setString(2, fullname);
                    preparedStatement.setString(3, gender);
                    preparedStatement.setObject(4, birthdate);
                    preparedStatement.setString(5, address);
                    preparedStatement.setString(6, phone);
                    preparedStatement.setString(7, username);
                    preparedStatement.executeUpdate();
                }
            } else {
                // Sửa
                String oldUsername = selectedEmployee.getUsername();
                // Sửa thông tin username trong bảng users trước nếu có thay đổi.
                if (!oldUsername.equals(username)) {
                    if(!updateUsernameInUsersTable(oldUsername, username)){
                        return;
                    }
                }
                String sql = "UPDATE employees SET code_employee = ?, fullname = ?, gender = ?, birthdate = ?, address = ?, phone = ?, username = ? WHERE employee_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, code);
                    preparedStatement.setString(2, fullname);
                    preparedStatement.setString(3, gender);
                    preparedStatement.setObject(4, birthdate);
                    preparedStatement.setString(5, address);
                    preparedStatement.setString(6, phone);
                    preparedStatement.setString(7, username);
                    preparedStatement.setInt(8, selectedEmployee.getEmployeeId());
                    preparedStatement.executeUpdate();
                }
            }
            hideForm();
            loadEmployeeData();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi thêm/sửa nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        hideForm();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showForm(){
        employeeFormGrid.setVisible(true);
    }
    private void hideForm(){
        employeeFormGrid.setVisible(false);
    }
    private void clearForm(){
        txtCode.clear();
        txtFullname.clear();
        cbGender.setValue("MEN");
        dpBirthdate.setValue(null);
        txtAddress.clear();
        txtPhone.clear();
        txtUsername.clear();
    }
    //load dữ liệu lên form
    private void loadEmployeeToForm(Employee employee){
        txtCode.setText(employee.getCodeEmployee());
        txtFullname.setText(employee.getFullname());
        cbGender.setValue(employee.getGender());
        dpBirthdate.setValue(employee.getBirthdate());
        txtAddress.setText(employee.getAddress());
        txtPhone.setText(employee.getPhone());
        txtUsername.setText(employee.getUsername());
    }

    private boolean isUsernameExists(String username) {
        Connection connection = dbConnection.getConn();
        String sql = "SELECT username FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi kiểm tra username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Thêm username vào bảng users
    private boolean addUserToUsersTable(String username) {
        Connection connection = dbConnection.getConn();
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, "123");
            preparedStatement.setString(3, "EMPLOYEE");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi thêm user vào bảng users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    //sửa username trong bảng users
    private boolean updateUsernameInUsersTable(String oldUsername, String newUsername) {
        if (!isUsernameExists(newUsername)){
            showAlert(AlertType.ERROR, "Lỗi", "Username chưa tồn tại trong bảng users: " + newUsername);
            return false;
        }

        Connection connection = dbConnection.getConn();
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi sửa username trong bảng users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}