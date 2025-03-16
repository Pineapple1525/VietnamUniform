package com.vietnamuniform.controller;

import com.vietnamuniform.connection.DBConnection;
import com.vietnamuniform.model.OrderDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class OrderDetailsController {

    @FXML private ComboBox<String> orderComboBox;
    @FXML private TableView<OrderDetail> orderDetailsTable;
    @FXML private TableColumn<OrderDetail, String> orderIdColumn;
    @FXML private TableColumn<OrderDetail, String> productIdColumn;
    @FXML private TableColumn<OrderDetail, String> productNameColumn;
    @FXML private TableColumn<OrderDetail, Double> salePriceColumn;
    @FXML private TableColumn<OrderDetail, Integer> quantityColumn;
    @FXML private TableColumn<OrderDetail, String> sizeColumn;
    @FXML private TableColumn<OrderDetail, Double> totalPriceColumn;

    @FXML private TextField searchField, quantityField, productNameField;
    @FXML private ComboBox<String> productIdField;
    @FXML private ComboBox<String> sizeField;

    @FXML private Button addOrderDetail, editOrderDetail, deleteOrderDetail, searchOrder, printInvoiceButton;
    @FXML private TextField employeeIdField;
    @FXML private TextField employeeNameField;
    @FXML private TextField customerIdField;
    @FXML private Label invoiceDateField;

    private Connection conn;

    @FXML
    public void initialize() {
         this.conn = DBConnection.getConn();
        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết nối đến cơ sở dữ liệu!");
            return;
        }
        loadOrderCodes();
        setupTableColumns();
        loadProductData();
        loadSizeOptions();

        orderComboBox.setOnAction(event -> {
            String selectedOrderCode = orderComboBox.getValue();
            loadOrderDetails();
            clearInputFields();
            loadProductData();
            loadOrderInfo(selectedOrderCode); // Điền thông tin nhân viên và khách hàng
        });
        productIdField.setOnAction(event -> updateProductName());
    }

    private void updateProductName() {
        String selectedProductId = productIdField.getValue();
        if (selectedProductId == null || selectedProductId.isEmpty()) {
            productNameField.clear();
            return;
        }

        String query = "SELECT name FROM products WHERE code_product = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, selectedProductId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                productNameField.setText(rs.getString("name"));
            } else {
                productNameField.clear();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lấy tên sản phẩm: " + e.getMessage());
        }
    }

    private boolean isConnectionValid() {
        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết nối đến cơ sở dữ liệu!");
            return false;
        }
        return true;
    }

    private void loadSizeOptions() {
        if (!isConnectionValid()) return;
        String query = "SELECT DISTINCT size FROM products WHERE size IS NOT NULL AND size <> ''";
        ObservableList<String> sizes = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sizes.add(rs.getString("size"));
            }
            sizeField.setItems(sizes);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải kích thước sản phẩm: " + e.getMessage());
        }
    }

    private void loadOrderCodes() {
        if (!isConnectionValid()) return;
        String query = "SELECT code_order FROM orders";
        ObservableList<String> orderCodes = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orderCodes.add(rs.getString("code_order"));
            }
            orderComboBox.setItems(orderCodes);
            System.out.println("Số mã đơn hàng tải được: " + orderCodes.size());
            if (orderCodes.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không có đơn hàng nào trong hệ thống!");
            } else if (orderCodes.size() < 3) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chỉ tải được " + orderCodes.size() + " đơn hàng, mặc dù có 3 đơn hàng trong DB!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải mã đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("codeOrder"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("codeProduct"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    @FXML
    private void addOrderDetail() {
        if (!isConnectionValid()) return;

        String orderCode = orderComboBox.getValue();
        String productId = productIdField.getValue();
        String size = sizeField.getValue() != null ? sizeField.getValue() : "Không xác định";

        if (orderCode == null || productId == null || orderCode.isEmpty() || productId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn mã đơn hàng và mã sản phẩm!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải lớn hơn 0!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập số hợp lệ cho số lượng!");
            return;
        }

        String productQuery = "SELECT name, sale_price FROM products WHERE code_product = ?";
        try (PreparedStatement productStmt = conn.prepareStatement(productQuery)) {
            productStmt.setString(1, productId);
            ResultSet rs = productStmt.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Sản phẩm không tồn tại!");
                return;
            }

            String productName = rs.getString("name");
            double salePrice = rs.getDouble("sale_price");

            if (salePrice <= 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Giá bán không hợp lệ!");
                return;
            }

            String insertQuery = "INSERT INTO order_details (code_order, code_product, product_name, sale_price, quantity, size) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, orderCode);
                stmt.setString(2, productId);
                stmt.setString(3, productName);
                stmt.setDouble(4, salePrice);
                stmt.setInt(5, quantity);
                stmt.setString(6, size);
                stmt.executeUpdate();

                loadOrderDetails();
                loadOrderCodes(); // Cập nhật lại danh sách mã đơn hàng
                clearInputFields();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sản phẩm vào đơn hàng thành công!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm sản phẩm: " + e.getMessage());
        }
    }

    @FXML
    private void deleteOrderDetail() {
        if (!isConnectionValid()) return;

        OrderDetail selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn sản phẩm để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa sản phẩm này?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

        String deleteQuery = "DELETE FROM order_details WHERE code_order = ? AND code_product = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, selectedOrder.getCodeOrder());
            stmt.setString(2, selectedOrder.getCodeProduct());
            stmt.executeUpdate();

            loadOrderDetails();
            loadOrderCodes(); // Cập nhật lại danh sách mã đơn hàng
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa sản phẩm khỏi đơn hàng thành công!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa sản phẩm: " + e.getMessage());
        }
    }

    private void loadProductData() {
        if (!isConnectionValid()) return;
        String query = "SELECT code_product FROM products";
        ObservableList<String> productIds = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                productIds.add(rs.getString("code_product"));
            }
            productIdField.setItems(productIds);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sản phẩm: " + e.getMessage());
        }
    }

    @FXML
    private void editOrderDetail(ActionEvent actionEvent) {
        if (!isConnectionValid()) return;

        OrderDetail selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn sản phẩm để sửa!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải lớn hơn 0!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập số hợp lệ!");
            return;
        }

        String query = "UPDATE order_details SET quantity = ? WHERE code_order = ? AND code_product = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, selectedOrder.getCodeOrder());
            stmt.setString(3, selectedOrder.getCodeProduct());
            stmt.executeUpdate();

            loadOrderDetails();
            clearInputFields();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật số lượng thành công!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @FXML
    private void loadOrderDetails() {
        if (!isConnectionValid()) return;

        String orderCode = orderComboBox.getValue();

        ObservableList<OrderDetail> orderDetailList = FXCollections.observableArrayList();
        String query = "SELECT code_order, code_product, product_name, sale_price, quantity, size, " +
                "(sale_price * quantity) AS total_price FROM order_details WHERE code_order = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orderDetailList.add(new OrderDetail(
                        rs.getString("code_order"),
                        rs.getString("code_product"),
                        rs.getString("product_name"),
                        rs.getDouble("sale_price"),
                        rs.getInt("quantity"),
                        rs.getString("size"),
                        rs.getDouble("total_price")
                ));
            }

            orderDetailsTable.setItems(orderDetailList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải chi tiết đơn hàng: " + e.getMessage());
        }
    }

    private void loadOrderInfo(String orderCode) {
        if (!isConnectionValid() || orderCode == null || orderCode.trim().isEmpty()) {
            employeeIdField.setText("");
            employeeNameField.setText("");
            customerIdField.setText("");
            invoiceDateField.setText("Chưa có ngày");
            return;
        }

        String query = "SELECT code_order, employee_name, code_customer, order_date FROM orders WHERE code_order = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employeeIdField.setText(rs.getString("code_order"));
                employeeNameField.setText(rs.getString("employee_name"));
                customerIdField.setText(rs.getString("code_customer"));
                invoiceDateField.setText(rs.getString("order_date") != null ? rs.getString("order_date") : "Chưa có ngày");
            } else {
                employeeIdField.setText("");
                employeeNameField.setText("");
                customerIdField.setText("");
                invoiceDateField.setText("Chưa có ngày");
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy thông tin cho đơn hàng: " + orderCode);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải thông tin đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void searchOrder() {
        if (!isConnectionValid()) return;

        String searchText = searchField.getText().trim();
        ObservableList<OrderDetail> filteredList = FXCollections.observableArrayList();

        if (searchText.isEmpty()) {
            loadOrderDetails();
            return;
        }

        String searchQuery = "SELECT od.code_order, od.code_product, od.product_name, od.sale_price, " +
                "od.quantity, od.size, (od.sale_price * od.quantity) AS total_price " +
                "FROM order_details od " +
                "WHERE od.code_order LIKE ? OR od.product_name LIKE ? OR od.code_product LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            stmt.setString(3, "%" + searchText + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filteredList.add(new OrderDetail(
                        rs.getString("code_order"),
                        rs.getString("code_product"),
                        rs.getString("product_name"),
                        rs.getDouble("sale_price"),
                        rs.getInt("quantity"),
                        rs.getString("size"),
                        rs.getDouble("total_price")
                ));
            }

            orderDetailsTable.setItems(filteredList);
            if (filteredList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy kết quả phù hợp!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm: " + e.getMessage());
        }
    }

    @FXML
    private void printInvoice() {
        System.out.println("In hóa đơn...");
        // TODO: Thêm logic in hóa đơn thực tế tại đây
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearInputFields() {
        productIdField.setValue(null);
        productNameField.clear();
        quantityField.clear();
        sizeField.setValue(null);
    }
}