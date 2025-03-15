package com.vietnamuniform.model;

import java.math.BigDecimal;

public class Order {
    private String codeOrder;
    private String codeEmployee;
    private String employeeName;
    private String codeCustomer;
    private String orderDate;

    // Constructor

    public void setCodeOrder(String codeOrder) {
        this.codeOrder = codeOrder;
    }

    public void setCodeEmployee(String codeEmployee) {
        this.codeEmployee = codeEmployee;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setCodeCustomer(String codeCustomer) {
        this.codeCustomer = codeCustomer;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Order(String codeOrder, String codeEmployee, String employeeName, String codeCustomer, String orderDate) {
        this.codeOrder = codeOrder;
        this.codeEmployee = codeEmployee;
        this.employeeName = employeeName;
        this.codeCustomer = codeCustomer;
        this.orderDate = orderDate;

    }

    // Getters
    public String getCodeOrder() { return codeOrder; }
    public String getCodeEmployee() { return codeEmployee; }
    public String getEmployeeName() { return employeeName; }
    public String getCodeCustomer() { return codeCustomer; }
    public String getOrderDate() { return orderDate; }

}


