package com.vietnamuniform.model;

public class ProductStatistics {
    private String codeProduct, productName, productDetail, size;
    private int totalStock, quantitySold, quantityRemaining;

    public ProductStatistics(String codeProduct, String productName, String productDetail, String size, int totalStock, int quantitySold, int quantityRemaining) {
        this.codeProduct = codeProduct;
        this.productName = productName;
        this.productDetail = productDetail;
        this.size = size;
        this.totalStock = totalStock;
        this.quantitySold = quantitySold;
        this.quantityRemaining = quantityRemaining;
    }

    public String getCodeProduct() { return codeProduct; }
    public String getProductName() { return productName; }
    public String getProductDetail() { return productDetail; }
    public String getSize() { return size; }
    public int getTotalStock() { return totalStock; }
    public int getQuantitySold() { return quantitySold; }
    public int getQuantityRemaining() { return quantityRemaining; }
}
