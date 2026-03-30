package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.SanPhamDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    public List<SanPhamDTO> selectAll() {
        List<SanPhamDTO> list = new ArrayList<>();
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "SELECT MaSanPham, TenSanPham, HinhAnh, GiaBan, KichThuoc, soluong, trangthai FROM SanPham";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO(
                        rs.getInt("MaSanPham"),
                        rs.getString("TenSanPham"),
                        rs.getString("HinhAnh"),
                        rs.getInt("GiaBan"),
                        rs.getString("KichThuoc"),
                        rs.getInt("soluong"),
                        rs.getString("trangthai") != null ? rs.getString("trangthai") : "");
                list.add(sp);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            UtilsJDBC.closeConnection();
        }
        return list;
    }

    public boolean insert(SanPhamDTO sp) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "INSERT INTO SanPham (TenSanPham, HinhAnh, GiaBan, KichThuoc, soluong, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sp.getTenSanPham());
            ps.setString(2, sp.getHinhAnh());
            ps.setInt(3, sp.getGiaBan());
            ps.setString(4, sp.getKichThuoc());
            ps.setInt(5, sp.getSoLuong());
            ps.setString(6, sp.getTrangThai());
            boolean ok = ps.executeUpdate() > 0;
            ps.close();
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            UtilsJDBC.closeConnection();
        }
    }

    public boolean update(SanPhamDTO sp) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "UPDATE SanPham SET TenSanPham = ?, HinhAnh = ?, GiaBan = ?, KichThuoc = ?, soluong = ?, trangthai = ? WHERE MaSanPham = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sp.getTenSanPham());
            ps.setString(2, sp.getHinhAnh());
            ps.setInt(3, sp.getGiaBan());
            ps.setString(4, sp.getKichThuoc());
            ps.setInt(5, sp.getSoLuong());
            ps.setString(6, sp.getTrangThai());
            ps.setInt(7, sp.getMaSanPham());
            boolean ok = ps.executeUpdate() > 0;
            ps.close();
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            UtilsJDBC.closeConnection();
        }
    }

    public boolean delete(int maSanPham) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "DELETE FROM SanPham WHERE MaSanPham = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, maSanPham);
            boolean ok = ps.executeUpdate() > 0;
            ps.close();
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            UtilsJDBC.closeConnection();
        }
    }
}
