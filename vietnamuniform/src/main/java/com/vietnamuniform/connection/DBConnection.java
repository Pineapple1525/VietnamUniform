package com.vietnamuniform.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;

    

        // kiem tra xem da co driver hay chuaa
    public static Connection getConn() {
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/storemanagement", "root", "duy2692005");
                System.out.println("Kết nối MySQL thành công!");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy driver MySQL", e);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối đến MySQL", e);
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("Đã đóng kết nối MySQL.");
            } catch (SQLException e) {
                throw new RuntimeException("Lỗi khi đóng kết nối MySQL", e);
            }
        }
    }
}
