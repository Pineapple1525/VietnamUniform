package com.vietnamuniform.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class Product {
    private int product_id, quantity;
    private String code_product, name, category, description, manufacture_date, image_url;
    private double import_price, sale_price;
    private ImageView image;

    public Product(int product_id, String code_product, String name, String category, String description, double import_price, double sale_price, int quantity, String manufacture_date, String image_url) {
        this.product_id = product_id;
        this.code_product = code_product;
        this.name = name;
        this.category = category;
        this.description = description;
        this.import_price = import_price;
        this.sale_price = sale_price;
        this.quantity = quantity;
        this.manufacture_date = manufacture_date;
        this.image_url = image_url;

        // Kiểm tra và tải ảnh nếu có
        if (image_url != null && !image_url.isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream("/com/vietnamuniform/views/images/" + image_url);
            if (imageStream != null) {
                this.image = new ImageView(new Image(imageStream));
            } else {
                this.image = new ImageView();
                System.err.println("Không tìm thấy ảnh: " + image_url);
            }

            this.image.setFitWidth(30);
            this.image.setFitHeight(30);
        } else {
            this.image = new ImageView(); // Ảnh rỗng nếu không có đường dẫn
        }
    }

    // Getters
    public int getProductId() { return product_id; }
    public String getCodeProduct() { return code_product; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getImportPrice() { return import_price; }
    public double getSalePrice() { return sale_price; }
    public int getQuantity() { return quantity; }
    public String getManufactureDate() { return manufacture_date; }
    public String getImageUrl() { return image_url; }
    public ImageView getImage() { return image; }
}
