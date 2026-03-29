package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.LoaiGheDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoaiGheDAO {
    public ArrayList<LoaiGheDTO> selectAll() {
        ArrayList<LoaiGheDTO> list = new ArrayList<>();
        String sql = "SELECT MaLoaiGhe, TenLoaiGhe, GiaPhuThu FROM LoaiGhe";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(new LoaiGheDTO(
                        rs.getInt("MaLoaiGhe"),
                        rs.getString("TenLoaiGhe"),
                        rs.getInt("GiaPhuThu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Loại Ghế từ CSDL!");
        }
        return list;
    }
}