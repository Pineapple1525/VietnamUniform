
package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.EmployeeStatistics;
import com.vietnamuniform.model.ProductStatistics;
import com.vietnamuniform.model.RevenueStatistics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;

public class StatisticsController {

    @FXML private TableView<RevenueStatistics> revenueTable;
    @FXML private TableView<EmployeeStatistics> employeeTable;
    @FXML private TableView<ProductStatistics> productTable;

    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private TextField searchEmployeeField, searchProductField;

    // Cột thống kê doanh thu
    @FXML private TableColumn<RevenueStatistics, String> colDate, colEmployeeCode, colEmployeeName;
    @FXML private TableColumn<RevenueStatistics, Integer> colTotalProducts;
    @FXML private TableColumn<RevenueStatistics, Double> colTotalAmount;

    // Cột thống kê nhân viên
    @FXML private TableColumn<EmployeeStatistics, Integer> colStatId, colTotalOrders, colQuantitySold;
    @FXML private TableColumn<EmployeeStatistics, String> colEmployeeCodeEmp, colEmployeeNameEmp, colProductCodeEmp, colProductNameEmp;
    @FXML private TableColumn<EmployeeStatistics, Double> colTotalOrderAmount, colImportPrice, colSalePrice;

    // Cột thống kê sản phẩm
    @FXML private TableColumn<ProductStatistics, String> colProductCode, colProductName, colProductDetail, colSize;
    @FXML private TableColumn<ProductStatistics, Integer> colTotalStock, colQuantitySoldProd, colQuantityRemaining;

    @FXML
    private ScrollPane mainPane;
    @FXML
    public void initialize() {
        setupTableColumns();
        loadAllStatistics();
        watchCSS("src/main/resources/com/vietnamuniform/views/styles/statistic.css");
    }

    private void setupTableColumns() {
        // Cấu hình cột cho bảng thống kê doanh thu
        colDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colTotalProducts.setCellValueFactory(new PropertyValueFactory<>("totalProducts"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colEmployeeCode.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

// Cấu hình cột cho bảng thống kê nhân viên
        colStatId.setCellValueFactory(new PropertyValueFactory<>("statId"));
        colEmployeeCodeEmp.setCellValueFactory(new PropertyValueFactory<>("codeEmployee"));
        colEmployeeNameEmp.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colTotalOrders.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
        colTotalOrderAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colProductCodeEmp.setCellValueFactory(new PropertyValueFactory<>("codeProduct"));
        colProductNameEmp.setCellValueFactory(new PropertyValueFactory<>("productName")); // Đổi từ colProductEmp
        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colQuantitySold.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));

// Cấu hình cột cho bảng thống kê sản phẩm
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("codeProduct"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductDetail.setCellValueFactory(new PropertyValueFactory<>("productDetail"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colTotalStock.setCellValueFactory(new PropertyValueFactory<>("totalStock"));
        colQuantitySoldProd.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        colQuantityRemaining.setCellValueFactory(new PropertyValueFactory<>("quantityRemaining"));

    }

    private void loadAllStatistics() {
        loadAllRevenueStatistics();
        loadAllEmployeeStatistics();
        loadAllProductStatistics();
    }



    private void loadAllRevenueStatistics() {
        String query = "SELECT report_date, total_products, total_amount, code_employee, employee_name FROM revenue_statistics";
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("total_products"),
                        rs.getDouble("total_amount"),
                        rs.getString("code_employee"),
                        rs.getString("employee_name")
                ));
            }
            revenueTable.setItems(revenueList);
        } catch (SQLException e) {
            showAlert("Lỗi tải dữ liệu doanh thu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadAllEmployeeStatistics() {
        String query = "SELECT stat_id, code_employee, employee_name, order_count, total_amount, " +
                "code_product AS employee_product_id, product_name, import_price, sale_price, quantity_sold " +
                "FROM employee_statistics";
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getString("code_employee"), // Sửa employee_id thành code_employee
                        rs.getString("employee_name"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_amount"),
                        rs.getString("employee_product_id"), // Sửa employee_product_id thành kiểu String
                        rs.getString("product_name"),
                        rs.getDouble("import_price"),
                        rs.getDouble("sale_price"),
                        rs.getInt("quantity_sold")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadAllProductStatistics() {
        String query = "SELECT code_product, product_name, product_detail, size, total_stock, quantity_sold, quantity_remaining FROM product_statistics";
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getString("code_product"),
                        rs.getString("product_name"),
                        rs.getString("product_detail"),
                        rs.getString("size"),
                        rs.getInt("total_stock"),
                        rs.getInt("quantity_sold"),
                        rs.getInt("quantity_remaining")
                ));
            }
            productTable.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void searchRevenueStatistics() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            showAlert("Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!", Alert.AlertType.WARNING);
            return;
        }
        if (startDate.isAfter(endDate)) { // Thêm kiểm tra hợp lệ
            showAlert("Ngày bắt đầu không được lớn hơn ngày kết thúc!", Alert.AlertType.WARNING);
            return;
        }

        String query = "SELECT report_date, total_products, total_amount, code_employee, employee_name " +
                "FROM revenue_statistics WHERE report_date BETWEEN ? AND ?";
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("total_products"),
                        rs.getDouble("total_amount"),
                        rs.getString("code_employee"),
                        rs.getString("employee_name")
                ));
            }
            revenueTable.setItems(revenueList);
        } catch (SQLException e) {
            showAlert("Lỗi tìm kiếm doanh thu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void searchEmployeeStatistics() {
        String keyword = searchEmployeeField.getText().trim();
        if (keyword.isEmpty()) {
            showAlert("Vui lòng nhập tên nhân viên cần tìm!", Alert.AlertType.WARNING);
            return;
        }

        String query = "SELECT stat_id, code_employee, employee_name, order_count, total_amount, " +
                "code_product AS employee_product_id, product_name, import_price, sale_price, quantity_sold " +
                "FROM employee_statistics WHERE employee_name LIKE ?";
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getString("code_employee"), // Sửa employee_id thành code_employee
                        rs.getString("employee_name"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_amount"),
                        rs.getString("employee_product_id"), // Sửa employee_product_id thành kiểu String
                        rs.getString("product_name"),
                        rs.getDouble("import_price"),
                        rs.getDouble("sale_price"),
                        rs.getInt("quantity_sold")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void searchProductStatistics() {
        String keyword = searchProductField.getText().trim();

        if (keyword.isEmpty()) {
            showAlert("Vui lòng nhập tên sản phẩm cần tìm!", Alert.AlertType.WARNING);
            return;
        }

        String query = "SELECT code_product, product_name, product_detail, size, total_stock, quantity_sold, quantity_remaining FROM product_statistics WHERE product_name LIKE ?";
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getString("code_product"), // Đảm bảo đây là String
                        rs.getString("product_name"),
                        rs.getString("product_detail"),
                        rs.getString("size"),
                        rs.getInt("total_stock"),
                        rs.getInt("quantity_sold"),
                        rs.getInt("quantity_remaining")
                ));
            }

            if (productList.isEmpty()) {
                showAlert("Không tìm thấy sản phẩm nào khớp với từ khóa: " + keyword, Alert.AlertType.INFORMATION);
            }

            productTable.setItems(productList);
        } catch (SQLException e) {
            showAlert("Lỗi tìm kiếm sản phẩm: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void watchCSS(String cssFilePath) {
        Thread thread = new Thread(() -> {
            try {
                Path path = Paths.get(cssFilePath).toAbsolutePath().getParent();
                WatchService watchService = FileSystems.getDefault().newWatchService();
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals(new File(cssFilePath).getName())) {
                            reloadCSS();
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void reloadCSS() {
        Platform.runLater(() -> {
            Scene scene = mainPane.getScene();
            if (scene != null) {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(new File("src/main/resources/com/vietnamuniform/views/styles/statistic.css").toURI().toString());
                System.out.println("CSS Reloaded!");
            }
        });
    }
}
