package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.HoaDonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class HoaDonDAO {

    private static final String BASE_SELECT =
            "SELECT hd.MaHoaDon, hd.MaKH, hd.MaNV, kh.HoTen AS TenKhachHang, nv.HoTen AS TenNhanVien, " +
                    "hd.NgayLapHoaDon, hd.TongTienVe, hd.TongTienSanPham, hd.TongThanhToan " +
                    "FROM HoaDon hd " +
                    "LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                    "LEFT JOIN NhanVien nv ON hd.MaNV = nv.MaNV ";

    public ArrayList<HoaDonDTO> getAll() {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY hd.MaHoaDon DESC";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql);
             ResultSet rs = (ps == null ? null : ps.executeQuery())) {

            if (rs == null) {
                return result;
            }

            while (rs.next()) {
                result.add(mapResultSetToHoaDonDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int add(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO HoaDon (MaKH, MaNV, NgayLapHoaDon, TongTienVe, TongTienSanPham, TongThanhToan) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql, Statement.RETURN_GENERATED_KEYS)) {

            if (ps == null) {
                return -1;
            }

            setHoaDonInsertParams(ps, hoaDon);
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

    public boolean addCTHDVe(int maHoaDon, int maVe, int donGia) {
        String sql = "INSERT INTO ChiTietHoaDonVe (MaHoaDon, MaVe, DonGia) VALUES (?, ?, ?)";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql)) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, maHoaDon);
            ps.setInt(2, maVe);
            ps.setInt(3, donGia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public HoaDonDTO findById(int maHoaDon) {
        String sql = BASE_SELECT + "WHERE hd.MaHoaDon = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql)) {

            if (ps == null) {
                return null;
            }

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
        String sql = BASE_SELECT +
                "WHERE CAST(hd.MaHoaDon AS CHAR) LIKE ? " +
                "OR CAST(hd.MaKH AS CHAR) LIKE ? " +
                "OR CAST(hd.MaNV AS CHAR) LIKE ? " +
                "OR kh.HoTen LIKE ? " +
                "OR nv.HoTen LIKE ? " +
                "OR CAST(hd.NgayLapHoaDon AS CHAR) LIKE ? " +
                "ORDER BY hd.MaHoaDon DESC";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql)) {

            if (ps == null) {
                return result;
            }

            String value = "%" + (keyword == null ? "" : keyword.trim()) + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);
            ps.setString(5, value);
            ps.setString(6, value);

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

    public ArrayList<HoaDonDTO> search(String maHoaDon, String maKH, String maNV, String thoiGian) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + "WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();

        if (maHoaDon != null && !maHoaDon.trim().isEmpty()) {
            sql.append(" AND CAST(hd.MaHoaDon AS CHAR) LIKE ?");
            params.add("%" + maHoaDon.trim() + "%");
        }
        if (maKH != null && !maKH.trim().isEmpty()) {
            sql.append(" AND (CAST(hd.MaKH AS CHAR) LIKE ? OR kh.HoTen LIKE ?)");
            String value = "%" + maKH.trim() + "%";
            params.add(value);
            params.add(value);
        }
        if (maNV != null && !maNV.trim().isEmpty()) {
            sql.append(" AND (CAST(hd.MaNV AS CHAR) LIKE ? OR nv.HoTen LIKE ?)");
            String value = "%" + maNV.trim() + "%";
            params.add(value);
            params.add(value);
        }

        if (thoiGian != null) {
            switch (thoiGian) {
                case "Hôm nay":
                    sql.append(" AND DATE(hd.NgayLapHoaDon) = CURDATE()");
                    break;
                case "Tuần này":
                    sql.append(" AND YEARWEEK(hd.NgayLapHoaDon, 1) = YEARWEEK(CURDATE(), 1)");
                    break;
                case "Tháng này":
                    sql.append(" AND MONTH(hd.NgayLapHoaDon) = MONTH(CURDATE()) AND YEAR(hd.NgayLapHoaDon) = YEAR(CURDATE())");
                    break;
                default:
                    break;
            }
        }

        sql.append(" ORDER BY hd.MaHoaDon DESC");

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql.toString())) {

            if (ps == null) {
                return result;
            }

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

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

    public boolean update(HoaDonDTO hoaDon) {
        String sql = "UPDATE HoaDon SET " +
                "MaKH = ?, " +
                "MaNV = ?, " +
                "NgayLapHoaDon = ?, " +
                "TongTienVe = ?, " +
                "TongTienSanPham = ?, " +
                "TongThanhToan = ? " +
                "WHERE MaHoaDon = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql)) {

            if (ps == null) {
                return false;
            }

            setHoaDonUpdateParams(ps, hoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int maHoaDon) {
        Connection con = null;
        PreparedStatement psDeleteCTSP = null;
        PreparedStatement psDeleteCTVe = null;
        PreparedStatement psDeleteHoaDon = null;

        try {
            con = UtilsJDBC.getConnectDB();
            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            psDeleteCTSP = con.prepareStatement("DELETE FROM ChiTietHoaDonSanPham WHERE MaHoaDon = ?");
            psDeleteCTVe = con.prepareStatement("DELETE FROM ChiTietHoaDonVe WHERE MaHoaDon = ?");
            psDeleteHoaDon = con.prepareStatement("DELETE FROM HoaDon WHERE MaHoaDon = ?");

            psDeleteCTSP.setInt(1, maHoaDon);
            psDeleteCTSP.executeUpdate();

            psDeleteCTVe.setInt(1, maHoaDon);
            psDeleteCTVe.executeUpdate();

            psDeleteHoaDon.setInt(1, maHoaDon);
            boolean deleted = psDeleteHoaDon.executeUpdate() > 0;

            con.commit();
            return deleted;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            closeStatement(psDeleteHoaDon);
            closeStatement(psDeleteCTVe);
            closeStatement(psDeleteCTSP);
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeConnection(con);
        }

        return false;
    }

    private void setHoaDonInsertParams(PreparedStatement ps, HoaDonDTO hoaDon) throws SQLException {
        ps.setInt(1, hoaDon.getMaKH());
        ps.setInt(2, hoaDon.getMaNV());
        setNgayLapHoaDon(ps, 3, hoaDon.getNgayLapHoaDon());
        ps.setInt(4, hoaDon.getTongTienVe());
        ps.setInt(5, hoaDon.getTongTienSanPham());
        ps.setInt(6, hoaDon.getTongThanhToan());
    }

    private void setHoaDonUpdateParams(PreparedStatement ps, HoaDonDTO hoaDon) throws SQLException {
        ps.setInt(1, hoaDon.getMaKH());
        ps.setInt(2, hoaDon.getMaNV());
        setNgayLapHoaDon(ps, 3, hoaDon.getNgayLapHoaDon());
        ps.setInt(4, hoaDon.getTongTienVe());
        ps.setInt(5, hoaDon.getTongTienSanPham());
        ps.setInt(6, hoaDon.getTongThanhToan());
        ps.setInt(7, hoaDon.getMaHoaDon());
    }

    private void setNgayLapHoaDon(PreparedStatement ps, int parameterIndex, Timestamp ngayLapHoaDon) throws SQLException {
        if (ngayLapHoaDon != null) {
            ps.setTimestamp(parameterIndex, ngayLapHoaDon);
        } else {
            ps.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
        }
    }

    private HoaDonDTO mapResultSetToHoaDonDTO(ResultSet rs) throws SQLException {
        return new HoaDonDTO(
                rs.getInt("MaHoaDon"),
                rs.getInt("MaKH"),
                rs.getInt("MaNV"),
                rs.getString("TenKhachHang"),
                rs.getString("TenNhanVien"),
                rs.getTimestamp("NgayLapHoaDon"),
                rs.getInt("TongTienVe"),
                rs.getInt("TongTienSanPham"),
                rs.getInt("TongThanhToan")
        );
    }

    private PreparedStatement prepareStatement(Connection con, String sql) throws SQLException {
        if (con == null) {
            return null;
        }
        return con.prepareStatement(sql);
    }

    private PreparedStatement prepareStatement(Connection con, String sql, int autoGeneratedKeys) throws SQLException {
        if (con == null) {
            return null;
        }
        return con.prepareStatement(sql, autoGeneratedKeys);
    }

    private void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
