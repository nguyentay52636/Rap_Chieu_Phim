package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.HoaDonDTO;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO {

    public int add(HoaDonDTO hoaDon) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "INSERT INTO HoaDon (" +
                "maKH, maNV, ngayBan, soLuongVe, tongTienVe, tongTienSanPham, " +
                "maKhuyenMai, tongTienGiam, tongThanhToan" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            setHoaDonInsertParams(ps, hoaDon);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(con);
        }

        return -1;
    }

    public boolean addCTHDVe(int maHoaDon, int maVe, int giaVe) {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO CTHD_VE (maHoaDon, maVe, giaVe) VALUES (?, ?, ?)";

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql);

            ps.setInt(1, maHoaDon);
            ps.setInt(2, maVe);
            ps.setInt(3, giaVe);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(con);
        }

        return false;
    }

    public HoaDonDTO findById(int maHoaDon) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, maHoaDon);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToHoaDonDTO(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(con);
        }

        return null;
    }

    public ArrayList<HoaDonDTO> search(String keyword) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<HoaDonDTO> result = new ArrayList<>();

        String sql = "SELECT * FROM HoaDon " +
                "WHERE CAST(maHoaDon AS CHAR) LIKE ? " +
                "OR CAST(maKH AS CHAR) LIKE ? " +
                "OR CAST(maNV AS CHAR) LIKE ? " +
                "OR CAST(ngayBan AS CHAR) LIKE ?";

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql);

            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToHoaDonDTO(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(con);
        }

        return result;
    }

    public boolean update(HoaDonDTO hoaDon) {
        Connection con = null;
        PreparedStatement ps = null;

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

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql);

            setHoaDonUpdateParams(ps, hoaDon);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(con);
        }

        return false;
    }

    public boolean delete(int maHoaDon) {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";

        try {
            con = UtilsJDBC.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, maHoaDon);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(con);
        }

        return false;
    }

    private void setHoaDonInsertParams(PreparedStatement ps, HoaDonDTO hoaDon) throws SQLException {
        ps.setInt(1, hoaDon.getMaKH());
        ps.setInt(2, hoaDon.getMaNV());
        ps.setDate(3, hoaDon.getNgayBan());
        ps.setInt(4, hoaDon.getSoLuongVe());
        ps.setInt(5, hoaDon.getTongTienVe());
        ps.setInt(6, hoaDon.getTongTienSanPham());

        if (hoaDon.getMaKhuyenMai() == null) {
            ps.setNull(7, Types.INTEGER);
        } else {
            ps.setInt(7, hoaDon.getMaKhuyenMai());
        }

        ps.setInt(8, hoaDon.getTongTienGiam());
        ps.setInt(9, hoaDon.getTongThanhToan());
    }

    private void setHoaDonUpdateParams(PreparedStatement ps, HoaDonDTO hoaDon) throws SQLException {
        ps.setInt(1, hoaDon.getMaKH());
        ps.setInt(2, hoaDon.getMaNV());
        ps.setDate(3, hoaDon.getNgayBan());
        ps.setInt(4, hoaDon.getSoLuongVe());
        ps.setInt(5, hoaDon.getTongTienVe());
        ps.setInt(6, hoaDon.getTongTienSanPham());

        if (hoaDon.getMaKhuyenMai() == null) {
            ps.setNull(7, Types.INTEGER);
        } else {
            ps.setInt(7, hoaDon.getMaKhuyenMai());
        }

        ps.setInt(8, hoaDon.getTongTienGiam());
        ps.setInt(9, hoaDon.getTongThanhToan());
        ps.setInt(10, hoaDon.getMaHoaDon());
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

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
