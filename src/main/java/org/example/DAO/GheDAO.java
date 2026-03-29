package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.GheDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GheDAO {

    // Thêm một ghế mới vào CSDL
    public boolean insert(GheDTO ghe) {
        String sql = "INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe) VALUES (?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ghe.getMaPhong());
            ps.setInt(2, ghe.getMaLoaiGhe());
            ps.setString(3, ghe.getHangGhe());
            ps.setInt(4, ghe.getSoGhe());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm nhiều ghế cùng lúc (Batch Insert) - Rất hữu ích khi tạo phòng chiếu mới
    public boolean insertBatch(List<GheDTO> listGhe) {
        String sql = "INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe) VALUES (?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Bắt đầu Transaction

            for (GheDTO ghe : listGhe) {
                ps.setInt(1, ghe.getMaPhong());
                ps.setInt(2, ghe.getMaLoaiGhe());
                ps.setString(3, ghe.getHangGhe());
                ps.setInt(4, ghe.getSoGhe());
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}