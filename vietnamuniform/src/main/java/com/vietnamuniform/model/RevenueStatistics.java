package com.vietnamuniform.model;

import java.time.LocalDate;

public class RevenueStatistics {
    private LocalDate reportDate;
    private int totalProducts;
    private double totalAmount;
    private String codeEmployee;
    private String employeeName;


    public RevenueStatistics(LocalDate reportDate, int totalProducts, double totalAmount, String codeEmployee, String employeeName) {
        this.reportDate = reportDate;
        this.totalProducts = totalProducts;
        this.totalAmount = totalAmount;
        this.codeEmployee = codeEmployee;
        this.employeeName = employeeName;
    }


    public RevenueStatistics() {}

    public LocalDate getReportDate() { return reportDate; }
    public int getTotalProducts() { return totalProducts; }
    public double getTotalAmount() { return totalAmount; }
    public String getCodeEmployee() { return codeEmployee; }
    public String getEmployeeName() { return employeeName; }

    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setCodeEmployee(String employeeId) { this.codeEmployee = codeEmployee; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    @Override
    public String toString() {
        return "RevenueStatistics{" +
                "reportDate=" + reportDate +
                ", totalProducts=" + totalProducts +
                ", totalAmount=" + totalAmount +
                ", employeeId=" + codeEmployee +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
