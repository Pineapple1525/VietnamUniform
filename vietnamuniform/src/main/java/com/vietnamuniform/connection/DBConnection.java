package com.vietnamuniform.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;

    private DBConnection() { } // Ngăn chặn khởi tạo đối tượng

    public static Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {  // Kiểm tra kết nối trước khi sử dụng
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/storemanagement", "root", "duy2692005");
                System.out.println("✅ Kết nối thành công!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("❌ Lỗi kết nối đến MySQL", e);
        }
        return conn;
    }


    public static void close() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Đặt lại null để có thể mở lại sau này
                System.out.println("🔌 Kết nối đã đóng!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
