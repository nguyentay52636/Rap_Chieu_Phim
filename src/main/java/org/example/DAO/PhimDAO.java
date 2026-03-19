package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.PhimDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        rs.getString("TrangThai")
                );
                list.add(phim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Phim!");
        }
        return list;
    }
}