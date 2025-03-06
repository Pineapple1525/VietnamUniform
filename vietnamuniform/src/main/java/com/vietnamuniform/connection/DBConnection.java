package com.vietnamuniform.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection conn;

    public DBConnection() {

        // kiem tra xem da cos driver hay chua

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy driver MySQL", e);
        }

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/storemanagement", "root", "duy2692005");
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối đến MySQL", e);
        }

    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void close(){
        if (this.conn != null){
            try {
                this.conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
