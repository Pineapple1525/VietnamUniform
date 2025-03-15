package com.vietnamuniform.model;

public class ProductStatistics {
    private int id;
    private String code;
    private String name;
    private String description;
    private String size;
    private int totalStock;
    private int soldQuantity;
    private int remainingQuantity;

    // Constructor
    public ProductStatistics(int id, String code, String name, String description, String size,
                             int totalStock, int soldQuantity, int remainingQuantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.size = size;
        this.totalStock = totalStock;
        this.soldQuantity = soldQuantity;
        this.remainingQuantity = remainingQuantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    @Override
    public String toString() {
        return "ProductStatistics{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", totalStock=" + totalStock +
                ", soldQuantity=" + soldQuantity +
                ", remainingQuantity=" + remainingQuantity +
                '}';
    }
}
