package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.TaiKhoanDTO;
import java.sql.*;

public class TaiKhoanDAO {
    
    public TaiKhoanDTO checkLogin(String username, String password) {
        // Kiểm tra tài khoản khớp username, password và trạng thái phải đang hoạt động
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 'HoatDong'";
        
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, username);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                        rs.getInt("MaTaiKhoan"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getInt("MaNV"),
                        rs.getString("VaiTro"),
                        rs.getString("TrangThai")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
