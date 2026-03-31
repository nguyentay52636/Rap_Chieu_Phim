package org.example.DAO;

import org.example.DTO.HoaDonDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonDAO {

    private static HoaDonDAO instance;

    public static HoaDonDAO getInstance() {
        if (instance == null) {
            instance = new HoaDonDAO();
        }
        return instance;
    }

    private HoaDonDAO() {
    }

    public int add(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO HoaDon ("
                + "maKH, maNV, ngayBan, soLuongVe, tongTienVe, tongTienSanPham, "
                + "maKhuyenMai, tongTienGiam, tongThanhToan"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hoaDon.getMaKH());
            ps.setInt(2, hoaDon.getMaNV());
            ps.setDate(3, Date.valueOf(hoaDon.getNgayBan().toString()));
            ps.setInt(4, hoaDon.getSoLuongVe());
            ps.setDouble(5, hoaDon.getTongTienVe());
            ps.setDouble(6, hoaDon.getTongTienSanPham());

            if (hoaDon.getMaKhuyenMai() == 0) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, hoaDon.getMaKhuyenMai());
            }

            ps.setDouble(8, hoaDon.getTongTienGiam());
            ps.setDouble(9, hoaDon.getTongThanhToan());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addCTHDVe(int maHoaDon, int maVe, int giaVe) {
        String sql = "INSERT INTO CTHD_VE (maHoaDon, maVe, giaVe) VALUES (?, ?, ?)";

        try (
                Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ps.setInt(2, maVe);
            ps.setInt(3, giaVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // TÌM THEO MÃ HÓA ĐƠN
    // =========================
    public HoaDonDTO findHoaDonById(int maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try (
                Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new HoaDonDTO(
                        rs.getInt("maHoaDon"),
                        rs.getInt("maKH"),
                        rs.getInt("maNV"),
                        rs.getDate("ngayBan").toLocalDate(),
                        rs.getInt("soLuongVe"),
                        rs.getDouble("tongTienVe"),
                        rs.getDouble("tongTienSanPham"),
                        rs.getInt("maKhuyenMai"),
                        rs.getDouble("tongTienGiam"),
                        rs.getDouble("tongThanhToan")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // TÌM KIẾM THEO TỪ KHÓA
    // Có thể tìm theo: mã hóa đơn, mã KH, mã NV, ngày bán
    // =========================
    public ArrayList<HoaDonDTO> searchHoaDon(String keyword) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();

        String sql = "SELECT * FROM HoaDon "
                + "WHERE CAST(maHoaDon AS CHAR) LIKE ? "
                + "OR CAST(maKH AS CHAR) LIKE ? "
                + "OR CAST(maNV AS CHAR) LIKE ? "
                + "OR CAST(ngayBan AS CHAR) LIKE ?";

        try (
                Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HoaDonDTO hoaDon = new HoaDonDTO(
                        rs.getInt("maHoaDon"),
                        rs.getInt("maKH"),
                        rs.getInt("maNV"),
                        rs.getDate("ngayBan").toLocalDate(),
                        rs.getInt("soLuongVe"),
                        rs.getDouble("tongTienVe"),
                        rs.getDouble("tongTienSanPham"),
                        rs.getInt("maKhuyenMai"),
                        rs.getDouble("tongTienGiam"),
                        rs.getDouble("tongThanhToan")
                );
                result.add(hoaDon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
