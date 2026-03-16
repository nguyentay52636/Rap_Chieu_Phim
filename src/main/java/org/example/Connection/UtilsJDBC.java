package org.example.Connection ;

import java.sql.Connection;

public class UtilsJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/bookticket?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnectDB() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Kết nối CSDL thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy driver MySQL!");
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            System.err.println("❌ Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                System.out.println("✅ Đã đóng kết nối CSDL.");
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("🔄 Đang thử kết nối đến MySQL...");

        Connection conn = getConnectDB();

        if (conn != null) {
            System.out.println("🎉 Kết nối thành công! Bắt đầu xử lý dữ liệu...");
        } else {
            System.out.println("❌ Kết nối thất bại!");
        }

        closeConnection();
    }
}