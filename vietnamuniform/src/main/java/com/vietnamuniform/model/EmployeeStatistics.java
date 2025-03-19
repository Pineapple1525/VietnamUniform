package com.vietnamuniform.model;
import java.time.LocalDate;

public class EmployeeStatistics {
    private int id;
    private LocalDate reportDate;
    private int employeeId;
    private String employeeName;
    private int totalOrders;
    private double totalRevenue;
    private String productCode;
    private String productName;
    private double importPrice;
    private double salePrice;
    private int totalProductsSold;

    public EmployeeStatistics(int id, LocalDate reportDate, int employeeId, String employeeName, int totalOrders, double totalRevenue, String productCode, String productName, double importPrice, double salePrice, int totalProductsSold) {
        this.id = id;
        this.reportDate = reportDate;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.productCode = productCode;
        this.productName = productName;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.totalProductsSold = totalProductsSold;
    }

    public EmployeeStatistics(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
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
                ", productCode=" + productCode +
                ", productName='" + productName + '\'' +
                ", importPrice=" + importPrice +
                ", salePrice=" + salePrice +
                ", totalProductsSold=" + totalProductsSold +
                '}';
    }
}
