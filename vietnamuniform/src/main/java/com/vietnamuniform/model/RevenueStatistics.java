package com.vietnamuniform.model;

public class RevenueStatistics {
    private String reportDate;
    private int totalProducts;
    private double totalAmount;
    private int employeeId;
    private String employeeName;

    public RevenueStatistics(String reportDate, int totalProducts, double totalAmount, int employeeId, String employeeName) {
        this.reportDate = reportDate;
        this.totalProducts = totalProducts;
        this.totalAmount = totalAmount;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public String getReportDate() { return reportDate; }
    public int getTotalProducts() { return totalProducts; }
    public double getTotalAmount() { return totalAmount; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
}
