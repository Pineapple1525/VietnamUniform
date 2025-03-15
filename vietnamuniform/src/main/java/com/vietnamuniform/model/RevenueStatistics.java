package com.vietnamuniform.model;

import java.util.Date;

public class RevenueStatistics {
    private int id;
    private Date reportDate;
    private int totalProductsSold;
    private double totalRevenue;
    private String employeeCode;
    private String employeeName;

    // Constructor
    public RevenueStatistics(int id, Date reportDate, int totalProductsSold, double totalRevenue,
                             String employeeCode, String employeeName) {
        this.id = id;
        this.reportDate = reportDate;
        this.totalProductsSold = totalProductsSold;
        this.totalRevenue = totalRevenue;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "RevenueStatistics{" +
                "id=" + id +
                ", reportDate=" + reportDate +
                ", totalProductsSold=" + totalProductsSold +
                ", totalRevenue=" + totalRevenue +
                ", employeeCode='" + employeeCode + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
