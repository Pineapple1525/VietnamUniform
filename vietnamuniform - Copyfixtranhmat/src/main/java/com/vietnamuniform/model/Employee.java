package com.vietnamuniform.model;

import java.sql.Date;

public class Employee {
    private int employeeId;
    private String codeEmployee;
    private String fullname;
    private String gender;
    private Date birthdate;
    private String address;
    private String phone;
    private String username;
    private String password;
    private String role;

    // Constructor
    public Employee(int employeeId, String codeEmployee, String fullname, String gender, Date birthdate, String address, String phone, String username, String password, String role) {
        this.employeeId = employeeId;
        this.codeEmployee = codeEmployee;
        this.fullname = fullname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getCodeEmployee() {
        return codeEmployee;
    }

    public void setCodeEmployee(String codeEmployee) {
        this.codeEmployee = codeEmployee;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}