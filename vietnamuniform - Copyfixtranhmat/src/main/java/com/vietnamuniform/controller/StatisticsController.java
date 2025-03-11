
package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.EmployeeStatistics;
import com.vietnamuniform.model.ProductStatistics;
import com.vietnamuniform.model.RevenueStatistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class StatisticsController {

    @FXML
    private TableView<RevenueStatistics> revenueTable;
    @FXML
    private TableView<EmployeeStatistics> employeeTable;
    @FXML
    private TableView<ProductStatistics> productTable;

    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private TextField searchEmployeeField, searchProductField;
    @FXML private TableColumn<RevenueStatistics, String> colDate;
    @FXML private TableColumn<RevenueStatistics, Integer> colTotalProducts;
    @FXML private TableColumn<RevenueStatistics, Double> colTotalAmount;
    @FXML private TableColumn<RevenueStatistics, Integer> colEmployeeId;
    @FXML private TableColumn<RevenueStatistics, String> colEmployeeName;

    @FXML private TableColumn<EmployeeStatistics, Integer> colStatId;
    @FXML private TableColumn<EmployeeStatistics, Integer> colEmployeeIdEmp;
    @FXML private TableColumn<EmployeeStatistics, String> colEmployeeNameEmp;
    @FXML private TableColumn<EmployeeStatistics, Integer> colTotalOrders;
    @FXML private TableColumn<EmployeeStatistics, Double> colTotalOrderAmount;
    @FXML private TableColumn<EmployeeStatistics, Integer> colEmployeeProductId;
    @FXML private TableColumn<EmployeeStatistics, String> colProduct;
    @FXML private TableColumn<EmployeeStatistics, Double> colImportPrice;
    @FXML private TableColumn<EmployeeStatistics, Double> colSalePrice;
    @FXML private TableColumn<EmployeeStatistics, Integer> colQuantitySold;

    @FXML private TableColumn<ProductStatistics, Integer> colProductId;
    @FXML private TableColumn<ProductStatistics, String> colProductName;
    @FXML private TableColumn<ProductStatistics, String> colProductDetail;
    @FXML private TableColumn<ProductStatistics, String> colSize;
    @FXML private TableColumn<ProductStatistics, Integer> colTotalStock;
    @FXML private TableColumn<ProductStatistics, Integer> colQuantitySoldProd;
    @FXML private TableColumn<ProductStatistics, Integer> colQuantityRemaining;

    @FXML
    public void initialize() {

        System.out.println("Initializing StatisticsController...");
        setupTableColumns();
        loadAllStatistics();
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colTotalProducts.setCellValueFactory(new PropertyValueFactory<>("totalProducts"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        colStatId.setCellValueFactory(new PropertyValueFactory<>("statId"));
        colEmployeeIdEmp.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmployeeNameEmp.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colTotalOrders.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
        colTotalOrderAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colEmployeeProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colQuantitySold.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
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
        String query = "SELECT report_date, total_products, total_amount, employee_id, employee_name FROM revenue_statistics";
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getString("report_date"),
                        rs.getInt("total_products"),
                        rs.getDouble("total_amount"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name")
                ));
            }
            revenueTable.setItems(revenueList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllEmployeeStatistics() {
        String query = "SELECT stat_id, employee_id, employee_name, order_count, total_amount, " +
                "product_id AS employee_product_id, product_name, import_price, sale_price, quantity_sold " +
                "FROM employee_statistics";
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_amount"),
                        rs.getInt("employee_product_id"),
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
        String query = "SELECT product_id, product_name, product_detail, size, total_stock, quantity_sold, quantity_remaining FROM product_statistics";
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getInt("product_id"),
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
            showAlert("Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
            return;
        }
        String query = "SELECT report_date, total_products, total_amount, employee_id, employee_name FROM revenue_statistics WHERE report_date BETWEEN ? AND ?";
        ObservableList<RevenueStatistics> revenueList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                revenueList.add(new RevenueStatistics(
                        rs.getString("report_date"),
                        rs.getInt("total_products"),
                        rs.getDouble("total_amount"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name")
                ));
            }
            revenueTable.setItems(revenueList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchEmployeeStatistics() {
        String keyword = searchEmployeeField.getText().trim();
        if (keyword.isEmpty()) {
            showAlert("Vui lòng nhập tên nhân viên cần tìm!");
            return;
        }

        String query = "SELECT stat_id, employee_id, employee_name, order_count, total_amount, " +
                "product_id AS employee_product_id, product_name, import_price, sale_price, quantity_sold " +
                "FROM employee_statistics WHERE employee_name LIKE ?";
        ObservableList<EmployeeStatistics> employeeList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                employeeList.add(new EmployeeStatistics(
                        rs.getInt("stat_id"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_amount"),
                        rs.getInt("employee_product_id"),
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
            showAlert("Vui lòng nhập tên sản phẩm cần tìm!");
            return;
        }
        String query = "SELECT product_id, product_name, product_detail, size, total_stock, quantity_sold, quantity_remaining FROM product_statistics WHERE product_name LIKE ?";
        ObservableList<ProductStatistics> productList = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection().getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productList.add(new ProductStatistics(
                        rs.getInt("product_id"),
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


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
