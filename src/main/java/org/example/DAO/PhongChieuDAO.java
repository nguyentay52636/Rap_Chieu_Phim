package org.example.DAO;


import org.example.Connection.UtilsJDBC;
import org.example.DTO.PhongChieuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhongChieuDAO {
    
    // Lấy toàn bộ phòng chiếu từ DB
    public ArrayList<PhongChieuDTO> selectAll() {
        ArrayList<PhongChieuDTO> list = new ArrayList<>();
        String sql = "SELECT MaPhong, TenPhong, LoaiPhong, SoHang, SoGheMoiHang FROM PhongChieu";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                PhongChieuDTO pc = new PhongChieuDTO(
                        rs.getInt("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("LoaiPhong"),
                        rs.getInt("SoHang"),
                        rs.getInt("SoGheMoiHang")
                );
                list.add(pc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Phòng Chiếu!");
        }
        return list;
    }
}