package org.example.DAO;


import org.example.Connection.UtilsJDBC;
import org.example.DTO.GheDTO;
import org.example.DTO.PhongChieuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public boolean update(PhongChieuDTO pc) {
        String sql = "UPDATE PhongChieu SET TenPhong = ?, LoaiPhong = ?, SoHang = ?, SoGheMoiHang = ? WHERE MaPhong = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pc.getTenPhong());
            ps.setString(2, pc.getLoaiPhong());
            ps.setInt(3, pc.getSoHang());
            ps.setInt(4, pc.getSoGheMoiHang());
            ps.setInt(5, pc.getMaPhong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Xóa Chi Tiết Hóa Đơn Vé dựa trên mã phòng (Xóa tầng sâu nhất)
    public boolean deleteBill(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE c FROM ChiTietHoaDonVe c " +
                "JOIN Ve v ON c.MaVe = v.MaVe " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "WHERE g.MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Xóa Vé dựa trên mã phòng (Xóa tầng thứ 2)
    public boolean deleteTicket(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE v FROM Ve v " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "WHERE g.MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Xóa Ghế dựa trên mã phòng (Xóa tầng thứ 3)
    public boolean deleteChair(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE FROM Ghe WHERE MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    public boolean deleteShowtime(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE FROM SuatChieu WHERE MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Hàm xóa phòng chiếu: Quản lý Connection và Transaction
    public boolean delete(int maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE MaPhong = ?";
        Connection con = null;

        try {
            con = UtilsJDBC.getConnectDB();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // Thực thi xóa theo thứ tự từ dưới lên trên để không vi phạm khóa ngoại
            // 1. Xóa Chi Tiết Hóa Đơn
            deleteBill(con, maPhong);

            // 2. Xóa Vé
            deleteTicket(con, maPhong);

            // 3. Xóa Ghế
            deleteChair(con, maPhong);

            // 4. Xóa Suất Chiếu (Bổ sung vào đây)
            deleteShowtime(con, maPhong);

            // 5. Xóa phòng chiếu (Tầng cao nhất)
            boolean isRoomDeleted = false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maPhong);
                isRoomDeleted = ps.executeUpdate() > 0;
            }

            con.commit(); // Xác nhận lưu xuống DB
            return isRoomDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Hoàn tác nếu có lỗi ở bất kỳ bước nào
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean insert(PhongChieuDTO pc) {
        String sql = "INSERT INTO PhongChieu (TenPhong, LoaiPhong, SoHang, SoGheMoiHang) VALUES (?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pc.getTenPhong());
            ps.setString(2, pc.getLoaiPhong());
            ps.setInt(3, pc.getSoHang());
            ps.setInt(4, pc.getSoGheMoiHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}