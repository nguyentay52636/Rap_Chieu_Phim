package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.PhimDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class PhimDAO {

    public ArrayList<PhimDTO> selectAll() {
        ArrayList<PhimDTO> list = new ArrayList<>();
        String sql = "SELECT MaPhim, MaLoaiPhim, TenPhim, ThoiLuong, DaoDien, NamSanXuat, AnhMauPhim, NgayKhoiChieu, TrangThai FROM Phim";

        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                PhimDTO phim = new PhimDTO(
                        rs.getInt("MaPhim"),
                        rs.getInt("MaLoaiPhim"),
                        rs.getString("TenPhim"),
                        rs.getInt("ThoiLuong"),
                        rs.getString("DaoDien"),
                        rs.getInt("NamSanXuat"),
                        rs.getString("AnhMauPhim"),
                        rs.getDate("NgayKhoiChieu"),
                        rs.getString("TrangThai"));
                list.add(phim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Phim!");
        }
        return list;
    }

    public boolean insert(PhimDTO phim) {
        String sql = "INSERT INTO Phim (MaLoaiPhim, TenPhim, ThoiLuong, DaoDien, NamSanXuat, AnhMauPhim, NgayKhoiChieu, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, phim.getMaTheLoaiPhim());
            pst.setString(2, phim.getTenPhim());
            pst.setInt(3, phim.getThoiLuong());
            pst.setString(4, phim.getDaoDien());
            pst.setInt(5, phim.getNamSanXuat());
            pst.setString(6, phim.getPosterURL());

            if (phim.getNgayKhoiChieu() != null) {
                pst.setDate(7, new java.sql.Date(phim.getNgayKhoiChieu().getTime()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }

            pst.setString(8, phim.getTrangThai());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(PhimDTO phim) {
        String sql = "UPDATE Phim SET MaLoaiPhim=?, TenPhim=?, ThoiLuong=?, DaoDien=?, NamSanXuat=?, AnhMauPhim=?, NgayKhoiChieu=?, TrangThai=? WHERE MaPhim=?";
        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, phim.getMaTheLoaiPhim());
            pst.setString(2, phim.getTenPhim());
            pst.setInt(3, phim.getThoiLuong());
            pst.setString(4, phim.getDaoDien());
            pst.setInt(5, phim.getNamSanXuat());
            pst.setString(6, phim.getPosterURL());

            if (phim.getNgayKhoiChieu() != null) {
                pst.setDate(7, new java.sql.Date(phim.getNgayKhoiChieu().getTime()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }

            pst.setString(8, phim.getTrangThai());
            pst.setInt(9, phim.getMaPhim());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maPhim) {
        String sql = "DELETE FROM Phim WHERE MaPhim=?";
        try (Connection con = UtilsJDBC.getConnectDB();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, maPhim);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}