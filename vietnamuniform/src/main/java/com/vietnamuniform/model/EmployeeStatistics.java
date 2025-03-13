package com.vietnamuniform.model;

public class EmployeeStatistics {
    private int statId;
    private String codeEmployee; // Sửa từ employeeId (int) thành codeEmployee (String)
    private String employeeName;
    private int orderCount;
    private double totalAmount;
    private String codeProduct; // Sửa từ productId (int) thành codeProduct (String)
    private String productName;
    private double importPrice;
    private double salePrice;
    private int quantitySold;

    // Constructor
    public EmployeeStatistics(int statId, String codeEmployee, String employeeName, int orderCount, double totalAmount,
                              String codeProduct, String productName, double importPrice, double salePrice, int quantitySold) {
        this.statId = statId;
        this.codeEmployee = codeEmployee;
        this.employeeName = employeeName;
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
        this.codeProduct = codeProduct;
        this.productName = productName;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.quantitySold = quantitySold;
    }

    // Getters
    public int getStatId() { return statId; }
    public String getCodeEmployee() { return codeEmployee; }
    public String getEmployeeName() { return employeeName; }
    public int getOrderCount() { return orderCount; }
    public double getTotalAmount() { return totalAmount; }
    public String getCodeProduct() { return codeProduct; }
    public String getProductName() { return productName; }
    public double getImportPrice() { return importPrice; }
    public double getSalePrice() { return salePrice; }
    public int getQuantitySold() { return quantitySold; }

    // Setters
    public void setStatId(int statId) { this.statId = statId; }
    public void setCodeEmployee(String codeEmployee) { this.codeEmployee = codeEmployee; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setCodeProduct(String codeProduct) { this.codeProduct = codeProduct; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setImportPrice(double importPrice) { this.importPrice = importPrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }
}
