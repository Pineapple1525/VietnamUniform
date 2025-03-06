package com.vietnamuniform.model;

public class EmployeeStatistics {
    private int statId;
    private int employeeId;
    private String employeeName;
    private int orderCount;
    private double totalAmount;
    private int productId;
    private String productName;
    private double importPrice;
    private double salePrice;
    private int quantitySold;

    // Constructor
    public EmployeeStatistics(int statId, int employeeId, String employeeName, int orderCount, double totalAmount,
                              int productId, String productName, double importPrice, double salePrice, int quantitySold) {
        this.statId = statId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
        this.productId = productId;
        this.productName = productName;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.quantitySold = quantitySold;
    }

    // Getters
    public int getStatId() { return statId; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public int getOrderCount() { return orderCount; }
    public double getTotalAmount() { return totalAmount; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getImportPrice() { return importPrice; }
    public double getSalePrice() { return salePrice; }
    public int getQuantitySold() { return quantitySold; }

    // Setters
    public void setStatId(int statId) { this.statId = statId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setImportPrice(double importPrice) { this.importPrice = importPrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }
}
