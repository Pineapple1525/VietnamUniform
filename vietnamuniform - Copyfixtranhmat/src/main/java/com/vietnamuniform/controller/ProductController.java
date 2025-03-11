package com.vietnamuniform.controller;

import com.vietnamuniform.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import com.vietnamuniform.connection.DBConnection;

import java.sql.*;

public class ProductController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, ImageView> colImage;
    @FXML private TableColumn<Product, Integer> colId, colQuantity;
    @FXML private TableColumn<Product, String> colName, colCategory, colDescription, colManufactureDate;
    @FXML private TableColumn<Product, Double> colImportPrice, colSalePrice;
    @FXML private TableColumn<Product, Void> colAction;

    @FXML private TextField txtName, txtCategory, txtDescription, txtImportPrice, txtSalePrice, txtQuantity, txtManufactureDate, txtImageUrl;
    @FXML private Button btnAdd, btnUpdate, btnDelete;
    private boolean isEditing = false;
    private Product selectedProduct;
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colManufactureDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        colAction.setCellValueFactory(param -> null);

        btnUpdate.setOnAction(e -> updateProduct());
        btnDelete.setOnAction(e -> clearFields());
        btnAdd.setOnAction(e -> addProduct());

        addActionButton();
        loadProductsFromDatabase();
    }

    private void clearFields() {
        txtName.clear();
        txtCategory.clear();
        txtDescription.clear();
        txtImportPrice.clear();
        txtSalePrice.clear();
        txtQuantity.clear();
        txtManufactureDate.clear();
        txtImageUrl.clear();
    }

    private void loadProductsFromDatabase() {
        productList.clear();
        String query = "SELECT * FROM Products";
        try (Connection conn = new DBConnection().getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDouble("import_price"),
                        rs.getDouble("sale_price"),
                        rs.getInt("quantity"),
                        rs.getString("manufacture_date"),
                        rs.getString("image_url")
                );
                productList.add(product);
            }
            productTable.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addProduct() {
        if (txtName.getText().isEmpty() || txtImportPrice.getText().isEmpty() || txtSalePrice.getText().isEmpty() || txtQuantity.getText().isEmpty()) {
            System.out.println("⚠️ Vui lòng nhập đầy đủ thông tin sản phẩm!");
            return;
        }

        String query = "INSERT INTO Products (name, category, description, import_price, sale_price, quantity, manufacture_date, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBConnection().getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtCategory.getText());
            pstmt.setString(3, txtDescription.getText());
            pstmt.setDouble(4, Double.parseDouble(txtImportPrice.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtSalePrice.getText()));
            pstmt.setInt(6, Integer.parseInt(txtQuantity.getText()));
            pstmt.setString(7, txtManufactureDate.getText());
            pstmt.setString(8, txtImageUrl.getText());

            pstmt.executeUpdate();
            clearFields();
            loadProductsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void updateProduct() {
        if (selectedProduct == null) return;

        String query = "UPDATE Products SET name=?, category=?, description=?, import_price=?, sale_price=?, quantity=?, manufacture_date=?, image_url=? WHERE product_id=?";
        try (Connection conn = new DBConnection().getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtCategory.getText());
            pstmt.setString(3, txtDescription.getText());
            pstmt.setDouble(4, Double.parseDouble(txtImportPrice.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtSalePrice.getText()));
            pstmt.setInt(6, Integer.parseInt(txtQuantity.getText()));
            pstmt.setString(7, txtManufactureDate.getText());
            pstmt.setString(8, txtImageUrl.getText());
            pstmt.setInt(9, selectedProduct.getProductId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Sản phẩm đã được cập nhật!");
                clearFields();
                isEditing = false;
                selectedProduct = null;
                loadProductsFromDatabase();
            } else {
                System.out.println("❌ Cập nhật sản phẩm thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteProduct(Product product) {
        if (product == null) return;

        String query = "DELETE FROM Products WHERE product_id=?";
        try (Connection conn = new DBConnection().getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, product.getProductId());
            pstmt.executeUpdate();
            productList.remove(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionButton() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setOnAction(e -> deleteProduct(getTableView().getItems().get(getIndex())));
                editButton.setOnAction(e -> {
                    Product selectedProduct = getTableView().getItems().get(getIndex());
                    if (selectedProduct != null) {
                        txtName.setText(selectedProduct.getName());
                        txtCategory.setText(selectedProduct.getCategory());
                        txtDescription.setText(selectedProduct.getDescription());
                        txtImportPrice.setText(String.valueOf(selectedProduct.getImportPrice()));
                        txtSalePrice.setText(String.valueOf(selectedProduct.getSalePrice()));
                        txtQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
                        txtManufactureDate.setText(selectedProduct.getManufactureDate());
                        txtImageUrl.setText(selectedProduct.getImageUrl());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editButton, deleteButton));
            }
        });
    }
}