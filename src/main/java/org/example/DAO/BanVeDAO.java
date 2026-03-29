package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.ChiTietHoaDonSanPhamDTO;
import org.example.DTO.HoaDonDTO;
import org.example.DTO.VeDTO;

import java.sql.*;
import java.util.List;

public class BanVeDAO {

    public boolean thanhToanGiaoDich(HoaDonDTO hoaDon, List<VeDTO> danhSachVe, List<ChiTietHoaDonSanPhamDTO> danhSachSP) {
        Connection con = null;

        String sqlInsertHoaDon = "INSERT INTO HoaDon (MaKH, MaNV, NgayLapHoaDon, TongTienVe, TongTienSanPham, TongThanhToan) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlInsertVe = "INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES (?, ?, ?, 'Da Ban')";
        String sqlInsertCTHDVe = "INSERT INTO ChiTietHoaDonVe (MaHoaDon, MaVe, DonGia) VALUES (?, ?, ?)";
        String sqlInsertCTHDSP = "INSERT INTO ChiTietHoaDonSanPham (MaHoaDon, MaSanPham, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateSP = "UPDATE SanPham SET SoLuong = SoLuong - ? WHERE MaSanPham = ?";
        String sqlUpdateKH = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy + ? WHERE MaKH = ?";

        try {
            con = UtilsJDBC.getConnectDB();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // ==========================================
            // 1. THÊM HÓA ĐƠN
            // ==========================================
            int maHoaDonMoi = -1;
            try (PreparedStatement psHoaDon = con.prepareStatement(sqlInsertHoaDon, Statement.RETURN_GENERATED_KEYS)) {
                if (hoaDon.getMaKH() > 0) {
                    psHoaDon.setInt(1, hoaDon.getMaKH());
                } else {
                    psHoaDon.setNull(1, java.sql.Types.INTEGER);
                }
                psHoaDon.setInt(2, hoaDon.getMaNV());
                // Ép từ java.sql.Date sang Timestamp để lưu được vào cột DATETIME của DB
                psHoaDon.setTimestamp(3, new java.sql.Timestamp(hoaDon.getNgayBan().getTime()));
                psHoaDon.setInt(4, hoaDon.getTongTienVe());
                psHoaDon.setInt(5, hoaDon.getTongTienSanPham());
                psHoaDon.setInt(6, hoaDon.getTongThanhToan());

                psHoaDon.executeUpdate();

                try (ResultSet rs = psHoaDon.getGeneratedKeys()) {
                    if (rs.next()) {
                        maHoaDonMoi = rs.getInt(1);
                    } else {
                        throw new SQLException("Không thể lấy ID Hóa Đơn!");
                    }
                }
            }

            // ==========================================
            // 2. THÊM VÉ & CHI TIẾT HÓA ĐƠN VÉ
            // ==========================================
            try (PreparedStatement psVe = con.prepareStatement(sqlInsertVe, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psCTHDVe = con.prepareStatement(sqlInsertCTHDVe)) {

                for (VeDTO ve : danhSachVe) {
                    psVe.setInt(1, ve.getMaGhe());
                    psVe.setInt(2, ve.getMaSuatChieuPhim()); // Đã sửa theo getter của bạn
                    psVe.setInt(3, ve.getGiaVe());
                    psVe.executeUpdate();

                    int maVeMoi = -1;
                    try (ResultSet rsVe = psVe.getGeneratedKeys()) {
                        if (rsVe.next()) maVeMoi = rsVe.getInt(1);
                        else throw new SQLException("Không thể lấy ID Vé!");
                    }

                    psCTHDVe.setInt(1, maHoaDonMoi);
                    psCTHDVe.setInt(2, maVeMoi);
                    psCTHDVe.setInt(3, ve.getGiaVe());
                    psCTHDVe.addBatch();
                }
                psCTHDVe.executeBatch();
            }

            // ==========================================
            // 3. THÊM CHI TIẾT SẢN PHẨM & TRỪ TỒN KHO
            // ==========================================
            if (danhSachSP != null && !danhSachSP.isEmpty()) {
                try (PreparedStatement psCTHDSP = con.prepareStatement(sqlInsertCTHDSP);
                     PreparedStatement psUpdateSP = con.prepareStatement(sqlUpdateSP)) {

                    for (ChiTietHoaDonSanPhamDTO sp : danhSachSP) {
                        psCTHDSP.setInt(1, maHoaDonMoi);
                        psCTHDSP.setInt(2, sp.getMaSanPham());
                        psCTHDSP.setInt(3, sp.getSoLuong());
                        psCTHDSP.setInt(4, sp.getDonGia());
                        psCTHDSP.setInt(5, sp.getThanhTien());
                        psCTHDSP.addBatch();

                        psUpdateSP.setInt(1, sp.getSoLuong());
                        psUpdateSP.setInt(2, sp.getMaSanPham());
                        psUpdateSP.addBatch();
                    }
                    psCTHDSP.executeBatch();
                    psUpdateSP.executeBatch();
                }
            }

            // ==========================================
            // 4. CẬP NHẬT ĐIỂM TÍCH LŨY (10k = 1 điểm)
            // ==========================================
            if (hoaDon.getMaKH() > 0) {
                try (PreparedStatement psUpdateKH = con.prepareStatement(sqlUpdateKH)) {
                    int diemCongThem = hoaDon.getTongThanhToan() / 10000;
                    psUpdateKH.setInt(1, diemCongThem);
                    psUpdateKH.setInt(2, hoaDon.getMaKH());
                    psUpdateKH.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Lấy danh sách ID các ghế đã được bán trong 1 suất chiếu
    public List<Integer> layDanhSachMaGheDaBan(int maSuatChieu) {
        List<Integer> list = new java.util.ArrayList<>();
        String sql = "SELECT MaGhe FROM Ve WHERE MaSuatChieu = ? AND TrangThai = 'Da Ban'";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("MaGhe"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}