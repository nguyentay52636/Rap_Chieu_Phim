package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.TaiKhoanDTO;
import java.sql.*;

public class TaiKhoanDAO {

    public TaiKhoanDTO getTaiKhoanByUsername(String username) {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                            rs.getInt("MaTaiKhoan"),
                            rs.getString("TenDangNhap"),
                            rs.getString("MatKhau"),
                            rs.getInt("MaNV"),
                            rs.getString("VaiTro"),
                            rs.getString("TrangThai"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn tài khoản theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public TaiKhoanDTO checkLogin(String username, String password) {
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
                            rs.getString("TrangThai"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, newPassword);
            pst.setString(2, username);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật mật khẩu: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordByUserName(String username, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, newPassword);
            pst.setString(2, username);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật mật khẩu theo email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
