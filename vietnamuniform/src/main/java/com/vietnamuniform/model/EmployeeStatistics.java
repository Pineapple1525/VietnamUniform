package com.vietnamuniform.model;
import java.time.LocalDate;

public class EmployeeStatistics {
    private int id;
    private LocalDate reportDate;
    private int employeeId;
    private String employeeName;
    private int totalOrders;
    private double totalRevenue;
    private int productId;
    private String productName;
    private double importPrice;
    private double salePrice;
    private int totalProductsSold;

    // Constructor đầy đủ
    public EmployeeStatistics(int id, LocalDate reportDate, int employeeId, String employeeName,
                              int totalOrders, double totalRevenue, int productId, String productName,
                              double importPrice, double salePrice, int totalProductsSold) {
        this.id = id;
        this.reportDate = reportDate;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.productId = productId;
        this.productName = productName;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.totalProductsSold = totalProductsSold;
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }

    @Override
    public String toString() {
        return "EmployeeStatistics{" +
                "id=" + id +
                ", reportDate=" + reportDate +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", totalOrders=" + totalOrders +
                ", totalRevenue=" + totalRevenue +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", importPrice=" + importPrice +
                ", salePrice=" + salePrice +
                ", totalProductsSold=" + totalProductsSold +
                '}';
    }
}
