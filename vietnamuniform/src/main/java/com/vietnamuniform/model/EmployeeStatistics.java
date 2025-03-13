package com.vietnamuniform.model;

import java.time.LocalDate;

public class EmployeeStatistics {
    private int statId;
    private LocalDate reportDate;
    private int orderId;
    private int employeeId;
    private String employeeName;
    private int productId;
    private String productName;

    public EmployeeStatistics(int statId, LocalDate reportDate, int orderId, int employeeId, String employeeName, int productId, String productName) {
        this.statId = statId;
        this.reportDate = reportDate;
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.productId = productId;
        this.productName = productName;
    }

    public int getStatId() { return statId; }
    public LocalDate getReportDate() { return reportDate; }
    public int getOrderId() { return orderId; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
}