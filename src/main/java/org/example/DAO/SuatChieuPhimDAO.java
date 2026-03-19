package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.KhachHangDTO;
import org.example.DTO.SuatChieuPhimDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuPhimDAO {
    public List<SuatChieuPhimDTO> selectAll() {
        List<SuatChieuPhimDTO> list = new ArrayList<>();
        // Sửa dòng này trong selectAll()
        String sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                "FROM SuatChieu " +
                "ORDER BY GioBatDau ASC, MaPhong ASC"; // Sắp xếp tăng dần theo giờ và phòng
        // Try-with-resources: Tự động quản lý đóng Connection, Statement, ResultSet
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Đọc an toàn: Kiểm tra NULL trước khi ép sang LocalDateTime
                Timestamp tsBatDau = rs.getTimestamp("GioBatDau");
                LocalDateTime gioBatDau = (tsBatDau != null) ? tsBatDau.toLocalDateTime() : null;

                Timestamp tsKetThuc = rs.getTimestamp("GioKetThuc");
                LocalDateTime gioKetThuc = (tsKetThuc != null) ? tsKetThuc.toLocalDateTime() : null;

                // Khởi tạo đối tượng với đầy đủ tham số
                SuatChieuPhimDTO sc = new SuatChieuPhimDTO(
                        rs.getInt("MaSuatChieu"),
                        rs.getInt("MaPhim"),
                        rs.getInt("MaPhong"),
                        gioBatDau,
                        gioKetThuc,
                        rs.getDouble("GiaVeGoc") // Đừng quên lấy thêm giá vé gốc có trong câu SQL
                );
                list.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ĐÃ LẤY ĐƯỢC: " + list.size() + " SUẤT CHIẾU TỪ CSDL");
        return list;
    }
    public boolean insert(SuatChieuPhimDTO sc) {
        String sql = "INSERT INTO SuatChieu (MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhong());
            ps.setObject(3, sc.getGioBatDau());
            ps.setObject(4, sc.getGioKetThuc());
            ps.setDouble(5,sc.getGiaVeGoc());

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
    public boolean update(SuatChieuPhimDTO sc) {
        String sql = "UPDATE SuatChieu SET MaPhim = ?, MaPhong = ?, GioBatDau = ?, GioKetThuc = ?, GiaVeGoc = ? WHERE MaSuatChieu = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sc.getMaPhim());
            ps.setInt(2, sc.getMaPhong());
            ps.setObject(3, sc.getGioBatDau());
            ps.setObject(4, sc.getGioKetThuc());
            ps.setDouble(5,sc.getGiaVeGoc());
            ps.setInt(6, sc.getMaSuatChieu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<SuatChieuPhimDTO> search(String tieuChi, String tuKhoa) {
        List<SuatChieuPhimDTO> list = new ArrayList<>();
        String sql = "";

        // Thêm ORDER BY GioBatDau ASC, MaPhong ASC vào cuối mỗi câu SQL
        switch (tieuChi) {
            case "Mã SC":
            case "MaSuatChieu":
                sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                        "FROM SuatChieu " +
                        "WHERE MaSuatChieu LIKE ? " +
                        "ORDER BY GioBatDau ASC, MaPhong ASC";
                break;

            case "Tên Phim":
                // Khi JOIN bảng cần chỉ định rõ sc.GioBatDau và sc.MaPhong
                sql = "SELECT sc.MaSuatChieu, sc.MaPhim, sc.MaPhong, sc.GioBatDau, sc.GioKetThuc, sc.GiaVeGoc " +
                        "FROM SuatChieu sc JOIN Phim p ON sc.MaPhim = p.MaPhim " +
                        "WHERE p.TenPhim LIKE ? " +
                        "ORDER BY sc.GioBatDau ASC, sc.MaPhong ASC";
                break;

            case "MaPhong":
                sql = "SELECT MaSuatChieu, MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc " +
                        "FROM SuatChieu " +
                        "WHERE MaPhong LIKE ? " +
                        "ORDER BY GioBatDau ASC, MaPhong ASC";
                break;

            default:
                return list;
        }

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Truyền từ khóa vào câu SQL, thêm % để tìm kiếm chứa chuỗi (LIKE)
            ps.setString(1, "%" + tuKhoa + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp tsBatDau = rs.getTimestamp("GioBatDau");
                    LocalDateTime gioBatDau = (tsBatDau != null) ? tsBatDau.toLocalDateTime() : null;

                    Timestamp tsKetThuc = rs.getTimestamp("GioKetThuc");
                    LocalDateTime gioKetThuc = (tsKetThuc != null) ? tsKetThuc.toLocalDateTime() : null;

                    SuatChieuPhimDTO sc = new SuatChieuPhimDTO(
                            rs.getInt("MaSuatChieu"),
                            rs.getInt("MaPhim"),
                            rs.getInt("MaPhong"),
                            gioBatDau,
                            gioKetThuc,
                            rs.getDouble("GiaVeGoc")
                    );
                    list.add(sc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}