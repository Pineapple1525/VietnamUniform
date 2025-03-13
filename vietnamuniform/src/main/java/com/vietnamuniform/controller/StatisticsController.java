
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
        String query = """
        SELECT rs.report_date, rs.order_id, rs.employee_id, 
               e.name AS employee_name, o.total_amount
        FROM revenue_statistics rs
        JOIN employees e ON rs.employee_id = e.employee_id
        JOIN orders o ON rs.order_id = o.order_id
    """;
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("order_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getDouble("total_amount")
                ));
            }
            revenueTable.setItems(revenueList);
        } catch (SQLException e) {
            showAlert("Lỗi tải dữ liệu doanh thu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void loadAllEmployeeStatistics() {
        String query = """
        SELECT es.stat_id, es.report_date, es.order_id, es.employee_id, es.product_id, 
               e.name AS employee_name, p.product_name
        FROM employee_statistics es
        JOIN employees e ON es.employee_id = e.employee_id
        JOIN products p ON es.product_id = p.product_id
    """;
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("order_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getInt("product_id"),
                        rs.getString("product_name")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void loadAllProductStatistics() {
        String query = """
        SELECT ps.stat_id, ps.report_date, ps.product_id, p.product_name, p.price
        FROM product_statistics ps
        JOIN products p ON ps.product_id = p.product_id
    """;
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getInt("stat_id"),
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price")
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
        if (startDate.isAfter(endDate)) {
            showAlert("Ngày bắt đầu không được lớn hơn ngày kết thúc!", Alert.AlertType.WARNING);
            return;
        }

        String query = "SELECT rs.report_date, o.order_id, e.employee_id, e.employee_name, rs.total_amount " +
                "FROM revenue_statistics rs " +
                "JOIN orders o ON rs.order_id = o.order_id " +
                "JOIN employees e ON rs.employee_id = e.employee_id " +
                "WHERE rs.report_date BETWEEN ? AND ?";
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("order_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getDouble("total_amount")
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

        String query = "SELECT es.stat_id, es.report_date, o.order_id, e.employee_id, e.employee_name, " +
                "p.product_id, p.product_name " +
                "FROM employee_statistics es " +
                "JOIN employees e ON es.employee_id = e.employee_id " +
                "JOIN orders o ON es.order_id = o.order_id " +
                "JOIN products p ON es.product_id = p.product_id " +
                "WHERE e.employee_name LIKE ?";
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("order_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getInt("product_id"),
                        rs.getString("product_name")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showAlert("Lỗi tìm kiếm nhân viên: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void searchProductStatistics() {
        String keyword = searchProductField.getText().trim();
        if (keyword.isEmpty()) {
            showAlert("Vui lòng nhập tên sản phẩm cần tìm!", Alert.AlertType.WARNING);
            return;
        }

        String query = "SELECT ps.stat_id, ps.report_date, p.product_id, p.product_name, p.price " +
                "FROM product_statistics ps " +
                "JOIN products p ON ps.product_id = p.product_id " +
                "WHERE p.product_name LIKE ?";
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getInt("stat_id"),
                        rs.getDate("report_date").toLocalDate(),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price")
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
