package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.VeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VeDAO {

    public List<VeDTO> selectAll() {
        List<VeDTO> list = new ArrayList<>();

        // Câu SQL JOIN các bảng lại để lấy Tên thay vì lấy Mã (ID)
        String sql = "SELECT v.MaVe, " +
                "COALESCE(kh.HoTen, 'Khách vãng lai') AS KhachHang, " +
                "p.TenPhim, pc.TenPhong, CONCAT(g.HangGhe, g.SoGhe) AS TenGhe, " +
                "sc.NgayChieu, sc.GioBatDau, v.GiaVe, v.TrangThai " +
                "FROM ve v " +
                "JOIN suatchieu sc ON v.MaSuatChieu = sc.MaSuatChieu " +
                "JOIN phim p ON sc.MaPhim = p.MaPhim " +
                "JOIN phongchieu pc ON sc.MaPhong = pc.MaPhong " +
                "JOIN ghe g ON v.MaGhe = g.MaGhe " +
                "LEFT JOIN chitiethoadonve ctv ON v.MaVe = ctv.MaVe " +
                "LEFT JOIN hoadon hd ON ctv.MaHoaDon = hd.MaHoaDon " +
                "LEFT JOIN khachhang kh ON hd.MaKH = kh.MaKH " +
                "ORDER BY v.MaVe DESC";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                VeDTO ve = new VeDTO();
                ve.setMaVe(rs.getInt("MaVe"));
                ve.setKhachHang(rs.getString("KhachHang"));
                ve.setTenPhim(rs.getString("TenPhim"));
                ve.setTenPhong(rs.getString("TenPhong"));
                ve.setTenGhe(rs.getString("TenGhe"));
                ve.setGiaVe(rs.getInt("GiaVe"));
                ve.setTrangThai(rs.getString("TrangThai"));

                // Xử lý Ngày và Giờ
                java.sql.Date ngay = rs.getDate("NgayChieu");
                java.sql.Time gio = rs.getTime("GioBatDau");
                if (ngay != null) ve.setNgayChieu(sdfDate.format(ngay));
                if (gio != null) ve.setGioBatDau(sdfTime.format(gio));

                list.add(ve);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}