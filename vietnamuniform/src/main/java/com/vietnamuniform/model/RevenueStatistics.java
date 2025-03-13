package com.vietnamuniform.model;

import java.time.LocalDate;
public class RevenueStatistics {
    private LocalDate reportDate;
    private int orderId;
    private int employeeId;
    private String employeeName;
    private double totalAmount;

    public RevenueStatistics(LocalDate reportDate, int orderId, int employeeId, String employeeName, double totalAmount) {
        this.reportDate = reportDate;
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalAmount = totalAmount;
    }

    public LocalDate getReportDate() { return reportDate; }
    public int getOrderId() { return orderId; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public double getTotalAmount() { return totalAmount; }
}
