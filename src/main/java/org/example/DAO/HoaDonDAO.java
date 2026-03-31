package org.example.DAO;

import org.example.DTO.HoaDonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sql = "INSERT INTO HoaDon (" +
                "maKH, maNV, ngayBan, soLuongVe, tongTienVe, tongTienSanPham, " +
                "maKhuyenMai, tongTienGiam, tongThanhToan" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, hoaDon.getMaKH());
            ps.setInt(2, hoaDon.getMaNV());
            ps.setDate(3, hoaDon.getNgayBan());
            ps.setInt(4, hoaDon.getSoLuongVe());
            ps.setInt(5, hoaDon.getTongTienVe());
            ps.setInt(6, hoaDon.getTongTienSanPham());

            if (hoaDon.getMaKhuyenMai() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, hoaDon.getMaKhuyenMai());
            }

            ps.setInt(8, hoaDon.getTongTienGiam());
            ps.setInt(9, hoaDon.getTongThanhToan());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
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
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maHoaDon);
            ps.setInt(2, maVe);
            ps.setInt(3, giaVe);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public HoaDonDTO findById(int maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maHoaDon);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHoaDonDTO(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<HoaDonDTO> search(String keyword) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();

        String sql = "SELECT * FROM HoaDon " +
                "WHERE CAST(maHoaDon AS CHAR) LIKE ? " +
                "OR CAST(maKH AS CHAR) LIKE ? " +
                "OR CAST(maNV AS CHAR) LIKE ? " +
                "OR CAST(ngayBan AS CHAR) LIKE ?";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToHoaDonDTO(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // =========================
    // CHỈNH SỬA HÓA ĐƠN
    // =========================
    public boolean update(HoaDonDTO hoaDon) {
        String sql = "UPDATE HoaDon SET " +
                "maKH = ?, " +
                "maNV = ?, " +
                "ngayBan = ?, " +
                "soLuongVe = ?, " +
                "tongTienVe = ?, " +
                "tongTienSanPham = ?, " +
                "maKhuyenMai = ?, " +
                "tongTienGiam = ?, " +
                "tongThanhToan = ? " +
                "WHERE maHoaDon = ?";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, hoaDon.getMaKH());
            ps.setInt(2, hoaDon.getMaNV());
            ps.setDate(3, hoaDon.getNgayBan());
            ps.setInt(4, hoaDon.getSoLuongVe());
            ps.setInt(5, hoaDon.getTongTienVe());
            ps.setInt(6, hoaDon.getTongTienSanPham());

            if (hoaDon.getMaKhuyenMai() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, hoaDon.getMaKhuyenMai());
            }

            ps.setInt(8, hoaDon.getTongTienGiam());
            ps.setInt(9, hoaDon.getTongThanhToan());
            ps.setInt(10, hoaDon.getMaHoaDon());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // XÓA HÓA ĐƠN
    // =========================
    public boolean delete(int maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maHoaDon);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private HoaDonDTO mapResultSetToHoaDonDTO(ResultSet rs) throws SQLException {
        int maKhuyenMaiValue = rs.getInt("maKhuyenMai");
        Integer maKhuyenMai = rs.wasNull() ? null : maKhuyenMaiValue;

        return new HoaDonDTO(
                rs.getInt("maHoaDon"),
                rs.getInt("maKH"),
                rs.getInt("maNV"),
                rs.getDate("ngayBan"),
                rs.getInt("soLuongVe"),
                rs.getInt("tongTienVe"),
                rs.getInt("tongTienSanPham"),
                maKhuyenMai,
                rs.getInt("tongTienGiam"),
                rs.getInt("tongThanhToan")
        );
    }
}
