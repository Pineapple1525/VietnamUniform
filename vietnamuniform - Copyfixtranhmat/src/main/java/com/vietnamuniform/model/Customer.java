package com.vietnamuniform.model;

public class Customer {
    private int customerId;
    private String codeCustomer;
    private String fullname;
    private String address;
    private String gender;
    private String phone;
    private String customerType;

    public Customer(int customerId, String codeCustomer, String fullname, String address, String gender, String phone, String customerType) {
        this.customerId = customerId;
        this.codeCustomer = codeCustomer;
        this.fullname = fullname;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.customerType = customerType;
    }

    // Getter & Setter
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCodeCustomer() { return codeCustomer; }
    public void setCodeCustomer(String codeCustomer) { this.codeCustomer = codeCustomer; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
}
