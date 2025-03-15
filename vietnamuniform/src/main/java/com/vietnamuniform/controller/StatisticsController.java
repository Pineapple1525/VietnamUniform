
package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.RevenueStatistics;
import com.vietnamuniform.model.EmployeeStatistics;
import com.vietnamuniform.model.ProductStatistics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController {
    @FXML
    private BorderPane mainPane;

    @FXML
    private TableView<ProductStatistics> productStatisticsTable;
    @FXML
    private TableView<EmployeeStatistics> employeeStatisticsTable;
    @FXML
    private TableView<RevenueStatistics> revenueStatisticsTable;

    /*PRODUCT*/
    @FXML
    private TableColumn<ProductStatistics, Integer> colProductId;
    @FXML
    private TableColumn<ProductStatistics, String> colProductCode;
    @FXML
    private TableColumn<ProductStatistics, String> colProductName;
    @FXML
    private TableColumn<ProductStatistics, String> colProductDetails;
    @FXML
    private TableColumn<ProductStatistics, String> colProductSize;
    @FXML
    private TableColumn<ProductStatistics, Integer> colTotalImport;
    @FXML
    private TableColumn<ProductStatistics, Integer> colTotalSold;
    @FXML
    private TableColumn<ProductStatistics, Integer> colRemainingStock;

    /*EMPLOYEE*/
    @FXML
    private TableColumn<EmployeeStatistics, Integer> colEmployeeStatId;
    @FXML
    private TableColumn<EmployeeStatistics, LocalDate> colEmployeeReportDate;
    @FXML
    private TableColumn<EmployeeStatistics, Integer> colEmployeeCode;
    @FXML
    private TableColumn<EmployeeStatistics, String> colEmployeeName;
    @FXML
    private TableColumn<EmployeeStatistics, Integer> colTotalOrders;
    @FXML
    private TableColumn<EmployeeStatistics, Double> colTotalRevenue;
    @FXML
    private TableColumn<EmployeeStatistics, Integer> colSoldProductCode;
    @FXML
    private TableColumn<EmployeeStatistics, String> colSoldProductName;
    @FXML
    private TableColumn<EmployeeStatistics, Double> colImportPrice;
    @FXML
    private TableColumn<EmployeeStatistics, Double> colSalePrice;
    @FXML
    private TableColumn<EmployeeStatistics, Integer> colTotalProductsSold;

    /*REVENUE*/
    @FXML
    private TableColumn<RevenueStatistics, Integer> colRevenueStatId;
    @FXML
    private TableColumn<RevenueStatistics, Date> colRevenueReportDate;
    @FXML
    private TableColumn<RevenueStatistics, Integer> colTotalProductsSoldRevenue;
    @FXML
    private TableColumn<RevenueStatistics, Double> colTotalRevenueDay;
    @FXML
    private TableColumn<RevenueStatistics, String> colRevenueEmployeeCode;
    @FXML
    private TableColumn<RevenueStatistics, String> colRevenueEmployeeName;

    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private TextField searchEmployeeField, searchProductField;




    @FXML
    public void initialize() {
        System.out.println("Initialize StatisticsController...");

        // Liên kết dữ liệu cột với thuộc tính của ProductStatistics
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductDetails.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colTotalImport.setCellValueFactory(new PropertyValueFactory<>("totalStock"));
        colTotalSold.setCellValueFactory(new PropertyValueFactory<>("soldQuantity"));
        colRemainingStock.setCellValueFactory(new PropertyValueFactory<>("remainingQuantity"));
        // Liên kết dữ liệu cột với thuộc tính của EmployeeStatistics
        colEmployeeStatId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmployeeReportDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colEmployeeCode.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colTotalOrders.setCellValueFactory(new PropertyValueFactory<>("totalOrders"));
        colTotalRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        colSoldProductCode.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colSoldProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colTotalProductsSold.setCellValueFactory(new PropertyValueFactory<>("totalProductsSold"));
        // Liên kết dữ liệu cột với thuộc tính của RevenueStatistics
        colRevenueStatId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRevenueReportDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colTotalProductsSoldRevenue.setCellValueFactory(new PropertyValueFactory<>("totalProductsSold"));
        colTotalRevenueDay.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        colRevenueEmployeeCode.setCellValueFactory(new PropertyValueFactory<>("employeeCode"));
        colRevenueEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));





        // Tải dữ liệu vào TableView
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList(getProductStatistics());
        productStatisticsTable.setItems(productList);
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList(getEmployeeStatistics());
        employeeStatisticsTable.setItems(employeeList);
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList(getRevenueStatistics());
        revenueStatisticsTable.setItems(revenueList);

        watchCSS("src/main/resources/com/vietnamuniform/views/styles/statistic.css");

    }

    public List<ProductStatistics> getProductStatistics() {
        List<ProductStatistics> statisticsList = new ArrayList<>();
        String query = """
        SELECT
            p.product_id AS id,
            p.code_product AS code,
            p.name AS name,
            p.description AS description,
            p.size AS size,
            SUM(p.quantity) AS totalStock,
            COALESCE(SUM(od.quantity), 0) AS soldQuantity,
            (SUM(p.quantity) - COALESCE(SUM(od.quantity), 0)) AS remainingQuantity
        FROM products p
        LEFT JOIN order_details od ON p.code_product = od.code_product
        GROUP BY p.product_id, p.code_product, p.name, p.description, p.size
    """;

        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProductStatistics stat = new ProductStatistics(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getInt("totalStock"),
                        rs.getInt("soldQuantity"),
                        rs.getInt("remainingQuantity")
                );
                statisticsList.add(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statisticsList;
    }


    public List<RevenueStatistics> getRevenueStatistics() {
        List<RevenueStatistics> statisticsList = new ArrayList<>();
        String query = """
        SELECT
            rs.stat_id AS id,
            rs.report_date AS reportDate,
            COALESCE(SUM(od.quantity), 0) AS totalProductsSold,
            COALESCE(SUM(o.total_amount), 0) AS totalRevenue,
            e.code_employee AS employeeCode,
            e.fullname AS employeeName
        FROM revenue_statistics rs
        JOIN orders o ON rs.order_id = o.order_id
        JOIN employees e ON rs.employee_id = e.employee_id
        LEFT JOIN order_details od ON o.code_order = od.code_order
        GROUP BY rs.stat_id, rs.report_date, e.code_employee, e.fullname
    """;

        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                Timestamp timestamp = rs.getTimestamp("reportDate");
                Date reportDate = timestamp != null ? new Date(timestamp.getTime()) : null;

                int totalProductsSold = rs.getInt("totalProductsSold");

                double totalRevenue = rs.getBigDecimal("totalRevenue").doubleValue();

                String employeeCode = rs.getString("employeeCode");
                String employeeName = rs.getString("employeeName");

                RevenueStatistics stat = new RevenueStatistics(id, reportDate, totalProductsSold, totalRevenue, employeeCode, employeeName);
                statisticsList.add(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statisticsList;
    }


    public List<EmployeeStatistics> getEmployeeStatistics() {
        List<EmployeeStatistics> statisticsList = new ArrayList<>();
        String query = """
        SELECT
            es.stat_id AS id,
            es.report_date AS reportDate,
            e.employee_id AS employeeId,
            e.fullname AS employeeName,
            COUNT(DISTINCT o.order_id) AS totalOrders,
            COALESCE(SUM(o.total_amount), 0) AS totalRevenue,
            p.product_id AS productId,
            p.name AS productName,
            p.import_price AS importPrice,
            p.sale_price AS salePrice,
            COALESCE(SUM(od.quantity), 0) AS totalProductsSold
        FROM employee_statistics es
        JOIN employees e ON es.employee_id = e.employee_id
        JOIN orders o ON es.order_id = o.order_id
        JOIN products p ON es.product_id = p.product_id
        LEFT JOIN order_details od ON o.code_order = od.code_order AND od.code_product = p.code_product
        GROUP BY es.stat_id, es.report_date, e.employee_id, e.fullname, p.product_id, p.name, p.import_price, p.sale_price
    """;

        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                // Chuyển đổi Timestamp -> LocalDate
                Timestamp timestamp = rs.getTimestamp("reportDate");
                LocalDate reportDate = timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;

                int employeeId = rs.getInt("employeeId");
                String employeeName = rs.getString("employeeName");
                int totalOrders = rs.getInt("totalOrders");

                // Chuyển đổi BigDecimal -> double
                double totalRevenue = rs.getBigDecimal("totalRevenue").doubleValue();
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                double importPrice = rs.getBigDecimal("importPrice").doubleValue();
                double salePrice = rs.getBigDecimal("salePrice").doubleValue();
                int totalProductsSold = rs.getInt("totalProductsSold");

                // Tạo đối tượng EmployeeStatistics với đúng kiểu dữ liệu
                EmployeeStatistics stat = new EmployeeStatistics(
                        id, reportDate, employeeId, employeeName, totalOrders, totalRevenue,
                        productId, productName, importPrice, salePrice, totalProductsSold
                );
                statisticsList.add(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statisticsList;
    }
    public List<RevenueStatistics> searchRevenueStatistics() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!", Alert.AlertType.WARNING);
            return new ArrayList<>();
        }
        if (startDate.isAfter(endDate)) {
            showAlert("Ngày bắt đầu không được lớn hơn ngày kết thúc!", Alert.AlertType.WARNING);
            return new ArrayList<>();
        }

        List<RevenueStatistics> statisticsList = new ArrayList<>();
        String query = """
    SELECT rs.stat_id AS id, rs.report_date AS reportDate, 
           COALESCE(SUM(od.quantity), 0) AS totalProductsSold, 
           COALESCE(SUM(o.total_amount), 0) AS totalRevenue, 
           e.code_employee AS employeeCode, e.fullname AS employeeName 
    FROM revenue_statistics rs 
    JOIN orders o ON rs.order_id = o.order_id 
    JOIN employees e ON rs.employee_id = e.employee_id 
    LEFT JOIN order_details od ON o.code_order = od.code_order 
    WHERE rs.report_date BETWEEN ? AND ? 
    GROUP BY rs.stat_id, rs.report_date, e.code_employee, e.fullname""";

        try (Connection conn = DBConnection.getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statisticsList.add(new RevenueStatistics(
                            rs.getInt("id"),
                            rs.getDate("reportDate"),
                            rs.getInt("totalProductsSold"),
                            rs.getBigDecimal("totalRevenue").doubleValue(),
                            rs.getString("employeeCode"),
                            rs.getString("employeeName")
                    ));
                }
            }

            // Cập nhật TableView sau khi truy vấn thành công
            Platform.runLater(() -> {
                revenueStatisticsTable.setItems(FXCollections.observableArrayList(statisticsList));
            });

        } catch (SQLException e) {
            showAlert("Lỗi tìm kiếm doanh thu: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        return statisticsList;
    }


    public List<EmployeeStatistics> searchEmployeeStatistics() {
        String employeeName = searchEmployeeField.getText().trim();
        List<EmployeeStatistics> statisticsList = new ArrayList<>();
        String query = """
        SELECT es.stat_id AS id, es.report_date AS reportDate, e.employee_id AS employeeId, 
               e.fullname AS employeeName, COUNT(DISTINCT o.order_id) AS totalOrders, 
               COALESCE(SUM(o.total_amount), 0) AS totalRevenue, 
               p.product_id AS productId, p.name AS productName, 
               p.import_price AS importPrice, p.sale_price AS salePrice, 
               COALESCE(SUM(od.quantity), 0) AS totalProductsSold 
        FROM employee_statistics es 
        JOIN employees e ON es.employee_id = e.employee_id 
        JOIN orders o ON es.order_id = o.order_id 
        JOIN products p ON es.product_id = p.product_id 
        LEFT JOIN order_details od ON o.code_order = od.code_order AND od.code_product = p.code_product 
        WHERE e.fullname LIKE ? 
        GROUP BY es.stat_id, es.report_date, e.employee_id, e.fullname, p.product_id, p.name, p.import_price, p.sale_price""";

        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + employeeName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statisticsList.add(new EmployeeStatistics(
                            rs.getInt("id"),
                            rs.getTimestamp("reportDate").toLocalDateTime().toLocalDate(),
                            rs.getInt("employeeId"),
                            rs.getString("employeeName"),
                            rs.getInt("totalOrders"),
                            rs.getBigDecimal("totalRevenue").doubleValue(),
                            rs.getInt("productId"),
                            rs.getString("productName"),
                            rs.getBigDecimal("importPrice").doubleValue(),
                            rs.getBigDecimal("salePrice").doubleValue(),
                            rs.getInt("totalProductsSold")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            employeeStatisticsTable.setItems(FXCollections.observableArrayList(statisticsList));
        });
        return statisticsList;
    }

    public List<ProductStatistics> searchProductStatistics() {
        String productName = searchProductField.getText().trim();
        List<ProductStatistics> statisticsList = new ArrayList<>();
        String query = """
        SELECT p.product_id AS id, p.code_product AS code, p.name AS name, 
               p.description AS description, p.size AS size, 
               SUM(p.quantity) AS totalStock, 
               COALESCE(SUM(od.quantity), 0) AS soldQuantity, 
               (SUM(p.quantity) - COALESCE(SUM(od.quantity), 0)) AS remainingQuantity 
        FROM products p 
        LEFT JOIN order_details od ON p.code_product = od.code_product 
        WHERE p.name LIKE ? 
        GROUP BY p.product_id, p.code_product, p.name, p.description, p.size""";

        try (Connection conn = DBConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + productName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statisticsList.add(new ProductStatistics(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("size"),
                            rs.getInt("totalStock"),
                            rs.getInt("soldQuantity"),
                            rs.getInt("remainingQuantity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            productStatisticsTable.setItems(FXCollections.observableArrayList(statisticsList));
        });
        return statisticsList;
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
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
