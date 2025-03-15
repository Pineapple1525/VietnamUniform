package com.vietnamuniform.model;

public class OrderDetail {
    private String codeOrder;
    private String codeProduct;
    private String productName;
    private double salePrice;
    private int quantity;
    private String size;
    private double totalPrice;

    public OrderDetail(String codeOrder, String codeProduct, String productName, double salePrice,
                       int quantity, String size, double totalPrice) {
        this.codeOrder = codeOrder;
        this.codeProduct = codeProduct;
        this.productName = productName;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.size = size;
        this.totalPrice = totalPrice;
    }

    // Getter và Setter
    public String getCodeOrder() { return codeOrder; }
    public String getCodeProduct() { return codeProduct; }
    public String getProductName() { return productName; }
    public double getSalePrice() { return salePrice; }
    public int getQuantity() { return quantity; }
    public String getSize() { return size; }
    public double getTotalPrice() { return totalPrice; }

    public void setCodeOrder(String codeOrder) { this.codeOrder = codeOrder; }
    public void setCodeProduct(String codeProduct) { this.codeProduct = codeProduct; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setSize(String size) { this.size = size; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}