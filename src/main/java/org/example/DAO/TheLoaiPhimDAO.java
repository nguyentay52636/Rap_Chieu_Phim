package org.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.TheLoaiPhimDTO;

public class TheLoaiPhimDAO {

    public List<TheLoaiPhimDTO> selectAll() {
        List<TheLoaiPhimDTO> list = new ArrayList<>();
        String sql = "SELECT MaLoaiPhim, TenLoaiPhim FROM theloaiphim";
        try (Connection conn = UtilsJDBC.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TheLoaiPhimDTO theloai = new TheLoaiPhimDTO(
                        rs.getInt("MaLoaiPhim"),
                        rs.getString("TenLoaiPhim")
                );
                list.add(theloai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách thể loại phim!");
        }
        return list;
    }

    public boolean insert(TheLoaiPhimDTO theloai) {
        String sql = "INSERT INTO theloaiphim (TenLoaiPhim) VALUES (?)";
        try (Connection conn = UtilsJDBC.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, theloai.getTenLoaiPhim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(TheLoaiPhimDTO theloai) {
        String sql = "UPDATE theloaiphim SET TenLoaiPhim = ? WHERE MaLoaiPhim = ?";
        try (Connection conn = UtilsJDBC.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, theloai.getTenLoaiPhim());
            ps.setInt(2, theloai.getMaLoaiPhim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int maLoaiPhim) {
        String sql = "DELETE FROM theloaiphim WHERE MaLoaiPhim = ?";
        try (Connection conn = UtilsJDBC.getConnectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLoaiPhim);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
