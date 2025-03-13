package com.vietnamuniform.model;
import java.time.LocalDate;

public class ProductStatistics {
    private int statId;
    private LocalDate reportDate;
    private int productId;
    private String productName;
    private double price;

    public ProductStatistics(int statId, LocalDate reportDate, int productId, String productName, double price) {
        this.statId = statId;
        this.reportDate = reportDate;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public int getStatId() { return statId; }
    public LocalDate getReportDate() { return reportDate; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
}