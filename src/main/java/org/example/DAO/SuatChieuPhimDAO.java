package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.SuatChieuPhimDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuPhimDAO {

    public List<SuatChieuPhimDTO> selectAll() {
        List<SuatChieuPhimDTO> list = new ArrayList<>();
        // Truy vấn dựa trên CSDL cũ (không có NgayChieu)
        String sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                "FROM SuatChieu " +
                "ORDER BY GioBatDau ASC, MaPhong ASC";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ĐÃ LẤY ĐƯỢC: " + list.size() + " SUẤT CHIẾU TỪ CSDL");
        return list;
    }

    public boolean insert(SuatChieuPhimDTO sc) {
        // ĐÃ THÊM: NgayChieu vào câu SQL
        String sql = "INSERT INTO SuatChieu (MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeGoc) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhong());

            // Tách LocalDateTime thành Date và Time cho Database
            ps.setDate(3, java.sql.Date.valueOf(sc.getGioBatDau().toLocalDate()));
            ps.setTime(4, java.sql.Time.valueOf(sc.getGioBatDau().toLocalTime()));
            ps.setTime(5, java.sql.Time.valueOf(sc.getGioKetThuc().toLocalTime()));

            ps.setDouble(6, sc.getGiaVeGoc());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SuatChieuPhimDTO sc) {
        // ĐÃ THÊM: NgayChieu vào câu SQL
        String sql = "UPDATE SuatChieu SET MaPhim = ?, MaPhong = ?, NgayChieu = ?, GioBatDau = ?, GioKetThuc = ?, GiaVeGoc = ? WHERE MaSuatChieu = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhong());

            // Tách LocalDateTime thành Date và Time
            ps.setDate(3, java.sql.Date.valueOf(sc.getGioBatDau().toLocalDate()));
            ps.setTime(4, java.sql.Time.valueOf(sc.getGioBatDau().toLocalTime()));
            ps.setTime(5, java.sql.Time.valueOf(sc.getGioKetThuc().toLocalTime()));

            ps.setDouble(6, sc.getGiaVeGoc());
            ps.setInt(7, sc.getMaSuatChieu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int MaSuatChieu) {
        String sql = "DELETE FROM SuatChieu WHERE MaSuatChieu = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, MaSuatChieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SuatChieuPhimDTO> search(String tieuChi, String tuKhoa) {
        List<SuatChieuPhimDTO> list = new ArrayList<>();
        String sql = "";

        switch (tieuChi) {
            case "Mã SC":
            case "MaSuatChieu":
                sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                        "FROM SuatChieu WHERE MaSuatChieu LIKE ? " +
                        "ORDER BY GioBatDau ASC, MaPhong ASC";
                break;

            case "Tên Phim":
                sql = "SELECT sc.MaSuatChieu, sc.MaPhim, sc.MaPhong, sc.GioBatDau, sc.GioKetThuc, sc.GiaVeGoc " +
                        "FROM SuatChieu sc JOIN Phim p ON sc.MaPhim = p.MaPhim " +
                        "WHERE p.TenPhim LIKE ? " +
                        "ORDER BY sc.GioBatDau ASC, sc.MaPhong ASC";
                break;

            case "Phòng":
            case "MaPhong":
                sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                        "FROM SuatChieu WHERE MaPhong LIKE ? " +
                        "ORDER BY GioBatDau ASC, MaPhong ASC";
                break;

            default:
                return list;
        }

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + tuKhoa + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<org.example.DTO.TrangThaiGheDTO> layTrangThaiGhe(int maSuatChieu, int maPhong) {
        java.util.List<org.example.DTO.TrangThaiGheDTO> list = new java.util.ArrayList<>();

        // 1. Thêm g.MaGhe vào đầu câu SELECT
        String sql = "SELECT g.MaGhe, g.HangGhe, g.SoGhe, COALESCE(v.TrangThai, 'Trong') AS TrangThai " +
                "FROM Ghe g " +
                "LEFT JOIN Ve v ON g.MaGhe = v.MaGhe AND v.MaSuatChieu = ? " +
                "WHERE g.MaPhong = ? " +
                "ORDER BY g.HangGhe ASC, g.SoGhe ASC";

        try (java.sql.Connection con = org.example.Connection.UtilsJDBC.getConnectDB();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSuatChieu);
            ps.setInt(2, maPhong);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 2. Truyền đủ 4 tham số vào đây
                    list.add(new org.example.DTO.TrangThaiGheDTO(
                            rs.getInt("MaGhe"),     // Dòng bạn đang bị thiếu là đây
                            rs.getString("HangGhe"),
                            rs.getInt("SoGhe"),
                            rs.getString("TrangThai")
                    ));
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm phụ trợ map dữ liệu (Đọc Timestamp từ CSDL lên)
    private SuatChieuPhimDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        Timestamp tsBatDau = rs.getTimestamp("GioBatDau");
        Timestamp tsKetThuc = rs.getTimestamp("GioKetThuc");

        LocalDateTime ldtBatDau = (tsBatDau != null) ? tsBatDau.toLocalDateTime() : null;
        LocalDateTime ldtKetThuc = (tsKetThuc != null) ? tsKetThuc.toLocalDateTime() : null;

        return new SuatChieuPhimDTO(
                rs.getInt("MaSuatChieu"),
                rs.getInt("MaPhim"),
                rs.getInt("MaPhong"),
                ldtBatDau,
                ldtKetThuc,
                rs.getDouble("GiaVeGoc")
        );
    }

    public List<SuatChieuPhimDTO> laySuatChieuTheoNgayVaPhim(java.sql.Date ngayChieu, int maPhim) {
        List<SuatChieuPhimDTO> list = new ArrayList<>();

        // CHÚ Ý QUAN TRỌNG: Đã thêm cột NgayChieu vào sau MaPhong
        String sql = "SELECT MaSuatChieu, MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeGoc " +
                "FROM SuatChieu " +
                "WHERE NgayChieu = ? AND MaPhim = ? AND TrangThai = 'SapChieu' " +
                "ORDER BY GioBatDau ASC";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, ngayChieu);
            ps.setInt(2, maPhim);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            // Nếu có lỗi, nó sẽ in ra màn hình Console (Run tab) ở dưới đáy IDE của bạn
            e.printStackTrace();
        }
        return list;
    }


}