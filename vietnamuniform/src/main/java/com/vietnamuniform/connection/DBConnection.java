package com.vietnamuniform.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection conn;

    private final String URL = "jdbc:mysql://localhost:3306/storemanagement";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public DBConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Đã đóng kết nối cơ sở dữ liệu.");
            } catch (SQLException e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}