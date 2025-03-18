package com.vietnamuniform.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.vietnamuniform.connection.DBConnection;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceGeneratorController {

    public void createInvoice(String dest, String orderCode) throws FileNotFoundException {
        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Fetch invoice data from the database
        Connection conn = null;

        try {
            conn = DBConnection.getConn();

            // Fixed SQL query with proper comma
            String invoiceQuery = "SELECT o.code_order, o.order_date, " +
                    "d.product_name, d.quantity, d.sale_price, c.fullname AS customer_name " +
                    "FROM orders o " +
                    "JOIN order_details d ON o.code_order = d.code_order " +
                    "JOIN customers c ON o.code_customer = c.code_customer " +
                    "WHERE o.code_order = ?";


            try (PreparedStatement stmt = conn.prepareStatement(invoiceQuery)) {
                stmt.setString(1, orderCode);
                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {  // Check if result set is empty
                    document.add(new Paragraph("Không tìm thấy hóa đơn cho mã đơn hàng: " + orderCode));
                    System.out.println("Không tìm thấy hóa đơn cho mã đơn hàng: " + orderCode);
                } else {
                    // Tạo tiêu đề hóa đơn
                    document.add(new Paragraph("Hóa Đơn").setFontSize(20));

                    // Get first row data
                    rs.next();
                    // Thêm thông tin hóa đơn
                    document.add(new Paragraph("Số Hóa Đơn: " + rs.getString("code_order")));
                    document.add(new Paragraph("Ngày: " + rs.getString("order_date")));
                    document.add(new Paragraph("Khách Hàng: " + rs.getString("customer_name")));

                    // Tạo bảng sản phẩm
                    Table table = new Table(3);
                    table.addHeaderCell("Sản Phẩm");
                    table.addHeaderCell("Số Lượng");
                    table.addHeaderCell("Giá");

                    double total = 0.0;
                    do {
                        String productName = rs.getString("product_name");
                        int quantity = rs.getInt("quantity");
                        double price = rs.getDouble("sale_price");
                        double itemTotal = quantity * price;

                        table.addCell(productName);
                        table.addCell(String.valueOf(quantity));
                        table.addCell(String.format("$%.2f", itemTotal));

                        total += itemTotal;
                    } while (rs.next());

                    document.add(table);
                    document.add(new Paragraph("Tổng: $" + String.format("%.2f", total))
                            .setFontSize(16).setBold());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            document.add(new Paragraph("Lỗi khi tạo hóa đơn: " + e.getMessage()));
        } finally {
            document.close();
            if (conn != null) {
                DBConnection.close();
            }
            System.out.println("Hóa đơn đã được tạo tại: " + dest);
        }
    }

    public static void main(String[] args) {
        InvoiceGeneratorController generator = new InvoiceGeneratorController();
        try {
            generator.createInvoice("invoice.pdf", "12345");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}