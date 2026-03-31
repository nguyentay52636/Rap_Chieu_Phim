package org.example.DAO;

import org.example.Connection.UtilsJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    public long getTongDoanhThu(Timestamp tuNgay, Timestamp denNgay) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COALESCE(SUM(x.TongThanhToan), 0) AS TongDoanhThu ")
           .append("FROM ( ")
           .append("    SELECT hd.MaHoaDon, hd.TongThanhToan ")
           .append("    FROM HoaDon hd ")
           .append("    WHERE 1 = 1 ");

        List<Timestamp> params = new ArrayList<>();
        appendDateFilter(sql, params, "hd.NgayLapHoaDon", tuNgay, denNgay);
        sql.append(") x");

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("TongDoanhThu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public int getTongSoVeBan(Timestamp tuNgay, Timestamp denNgay) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COALESCE(SUM(x.SoVe), 0) AS TongSoVeBan ")
           .append("FROM ( ")
           .append("    SELECT hd.MaHoaDon, COUNT(ctv.MaVe) AS SoVe ")
           .append("    FROM HoaDon hd ")
           .append("    LEFT JOIN ChiTietHoaDonVe ctv ON hd.MaHoaDon = ctv.MaHoaDon ")
           .append("    WHERE 1 = 1 ");

        List<Timestamp> params = new ArrayList<>();
        appendDateFilter(sql, params, "hd.NgayLapHoaDon", tuNgay, denNgay);
        sql.append(" GROUP BY hd.MaHoaDon ")
           .append(") x");

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("TongSoVeBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongSoSanPhamBan(Timestamp tuNgay, Timestamp denNgay) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COALESCE(SUM(x.SoLuongSanPham), 0) AS TongSoSanPhamBan ")
           .append("FROM ( ")
           .append("    SELECT hd.MaHoaDon, COALESCE(SUM(ctsp.SoLuong), 0) AS SoLuongSanPham ")
           .append("    FROM HoaDon hd ")
           .append("    LEFT JOIN ChiTietHoaDonSanPham ctsp ON hd.MaHoaDon = ctsp.MaHoaDon ")
           .append("    WHERE 1 = 1 ");

        List<Timestamp> params = new ArrayList<>();
        appendDateFilter(sql, params, "hd.NgayLapHoaDon", tuNgay, denNgay);
        sql.append(" GROUP BY hd.MaHoaDon ")
           .append(") x");

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("TongSoSanPhamBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Object[]> getBangXepHangDoanhThuTheoPhim(Timestamp tuNgay, Timestamp denNgay) {
        ArrayList<Object[]> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT y.MaPhim, y.TenPhim, SUM(y.SoVeDaBan) AS SoVeDaBan, SUM(y.DoanhThu) AS TongDoanhThu ")
           .append("FROM ( ")
           .append("    SELECT p.MaPhim, p.TenPhim, sc.MaSuatChieu, ")
           .append("           COUNT(ctv.MaVe) AS SoVeDaBan, ")
           .append("           COALESCE(SUM(ctv.DonGia), 0) AS DoanhThu ")
           .append("    FROM Phim p ")
           .append("    INNER JOIN SuatChieu sc ON p.MaPhim = sc.MaPhim ")
           .append("    INNER JOIN Ve v ON sc.MaSuatChieu = v.MaSuatChieu ")
           .append("    INNER JOIN ChiTietHoaDonVe ctv ON v.MaVe = ctv.MaVe ")
           .append("    INNER JOIN HoaDon hd ON ctv.MaHoaDon = hd.MaHoaDon ")
           .append("    WHERE 1 = 1 ");

        List<Timestamp> params = new ArrayList<>();
        appendDateFilter(sql, params, "hd.NgayLapHoaDon", tuNgay, denNgay);
        sql.append(" GROUP BY p.MaPhim, p.TenPhim, sc.MaSuatChieu ")
           .append(") y ")
           .append("GROUP BY y.MaPhim, y.TenPhim ")
           .append("ORDER BY TongDoanhThu DESC, SoVeDaBan DESC, y.MaPhim ASC");

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = prepareStatement(con, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {

            int top = 1;
            while (rs.next()) {
                result.add(new Object[]{
                        top++,
                        rs.getInt("MaPhim"),
                        rs.getString("TenPhim"),
                        rs.getInt("SoVeDaBan"),
                        rs.getLong("TongDoanhThu")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void appendDateFilter(StringBuilder sql, List<Timestamp> params, String columnName,
                                  Timestamp tuNgay, Timestamp denNgay) {
        if (tuNgay != null) {
            sql.append(" AND ").append(columnName).append(" >= ? ");
            params.add(tuNgay);
        }
        if (denNgay != null) {
            sql.append(" AND ").append(columnName).append(" <= ? ");
            params.add(denNgay);
        }
    }

    private PreparedStatement prepareStatement(Connection con, String sql, List<Timestamp> params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
        for (int i = 0; i < params.size(); i++) {
            ps.setTimestamp(i + 1, params.get(i));
        }
        return ps;
    }
}
