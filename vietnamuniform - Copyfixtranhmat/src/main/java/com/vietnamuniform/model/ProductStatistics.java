package com.vietnamuniform.model;

public class ProductStatistics {
    private int productId, totalStock, quantitySold, quantityRemaining;
    private String productName, productDetail, size;

    public ProductStatistics(int productId, String productName, String productDetail, String size, int totalStock, int quantitySold, int quantityRemaining) {
        this.productId = productId;
        this.productName = productName;
        this.productDetail = productDetail;
        this.size = size;
        this.totalStock = totalStock;
        this.quantitySold = quantitySold;
        this.quantityRemaining = quantityRemaining;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductDetail() { return productDetail; }
    public String getSize() { return size; }
    public int getTotalStock() { return totalStock; }
    public int getQuantitySold() { return quantitySold; }
    public int getQuantityRemaining() { return quantityRemaining; }
}

