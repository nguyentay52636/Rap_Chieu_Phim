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

public class VeDAO 
{
    // ==========================================
    // 1. LẤY TOÀN BỘ DANH SÁCH VÉ
    // ==========================================
    public List<VeDTO> selectAll() 
    {
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
             ResultSet rs = ps.executeQuery()) 
        {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            while (rs.next()) 
            {
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
        }
        
        catch (SQLException e) 
        {
            System.err.println("Lỗi khi kéo danh sách Vé từ DB!");
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================
    // 2. THÊM VÉ MỚI VÀO CSDL
    // ==========================================
    public boolean insert(VeDTO ve) 
    {
        String sql = "INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, ve.getMaGhe());
            ps.setInt(2, ve.getMaSuatChieuPhim()); // Đã gọt bớt khúc dư thừa
            ps.setInt(3, ve.getGiaVe());
            ps.setString(4, ve.getTrangThai() != null ? ve.getTrangThai() : "Chưa thanh toán");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm Vé mới!");
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 3. XÓA / HỦY VÉ
    // ==========================================
    public boolean delete(int maVe) 
    {
        // Tốt nhất là Cập nhật trạng thái thành "Đã Hủy" thay vì xóa trắng dữ liệu
        String sql = "UPDATE Ve SET TrangThai = 'Đã Hủy' WHERE MaVe = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, maVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi hủy Vé!");
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 4. TÌM GHẾ TRỐNG CỦA SUẤT CHIẾU (Dành cho chức năng Bán vé)
    // ==========================================
    public List<String> layDanhSachGheTrong(int maSuatChieu) 
    {
        List<String> listGheTrong = new ArrayList<>();
        
        // Lấy tất cả ghế thuộc Phòng Chiếu của Suất Chiếu đó, 
        // NGOẠI TRỪ những ghế đã nằm trong bảng Vé (đã bán/đã đặt)
        String sql = "SELECT g.HangGhe, g.SoGhe, g.MaGhe " +
                     "FROM Ghe g " +
                     "JOIN SuatChieu sc ON g.MaPhong = sc.MaPhong " +
                     "WHERE sc.MaSuatChieu = ? " +
                     "AND g.MaGhe NOT IN (SELECT MaGhe FROM Ve WHERE MaSuatChieu = ? AND TrangThai != 'Đã Hủy')";

        
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, maSuatChieu);
            ps.setInt(2, maSuatChieu);

            try (ResultSet rs = ps.executeQuery()) 
            {
                while (rs.next()) 
                {
                    String hangGhe = rs.getString("HangGhe");
                    int soGhe = rs.getInt("SoGhe");
                    int maGhe = rs.getInt("MaGhe");
                    
                    // Format đẹp đẹp để quăng lên giao diện chọn
                    listGheTrong.add("Ghế " + hangGhe + "-" + soGhe + " (MaGhe: " + maGhe + ")");
                }
            }
        } 
        
        catch (SQLException e) 
        {
            System.err.println("Lỗi khi lấy danh sách ghế trống!");
            e.printStackTrace();
        }
        return listGheTrong;
    }
}