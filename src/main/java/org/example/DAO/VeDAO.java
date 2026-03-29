package org.example.DAO;

import org.example.DTO.VeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.example.Connection.UtilsJDBC;
import java.sql.SQLException;
public class VeDAO 
{
    // ==========================================
    // LÃNH THỔ CỦA ÂN  (QUẢN LÝ - XEM & HỦY)
    // ==========================================
    
    // 1. Hàm lấy danh sách vé (Bạn ông phải viết câu lệnh SQL JOIN vào đây)
    public ArrayList<VeDTO> selectAll() 
    {
        ArrayList<VeDTO> list = new ArrayList<>();
        
        // ... Code SELECT Ve.MaVe, Ve.MaGhe, Ve.MaSuatChieu, Ve.GiaVe, Ve.TrangThai, SuatChieu.ThoiGianChieu, Phim.TenPhim FROM Ve JOIN SuatChieu ON Ve.MaSuatChieu = SuatChieu.MaSuatChieu JOIN Phim ON SuatChieu.MaPhim = Phim.MaPhim ...
        // ... Lặp ResultSet để tạo ra các đối tượng VeDTO rồi add vào list
        return list;
    }

    // 2. Hàm hủy vé (Chuyển trạng thái từ Trong/DaBan sang DaHuy)
    public boolean huyVe(int maVe) 
    {
        // ... Code UPDATE Ve SET TrangThai = 'DaHuy' WHERE MaVe = ? ...
        return false;
    }

    

    // ==========================================
    // LÃNH THỔ CỦA BÁCH (NGHIỆP VỤ BÁN VÉ)
    // ==========================================

    // 3. Hàm kiểm tra ghế (Ông viết lệnh SELECT đếm xem mã ghế trong suất chiếu này đã bán chưa)
    public boolean kiemTraGheDaBan(int maSuatChieu, int maGhe) 
    {
        // ... Code SELECT COUNT(*) FROM Ve WHERE MaSuatChieu = ? AND MaGhe = ? AND TrangThai = 'DaBan'
        return false;
    }

    // 4. Hàm thêm vé mới xuống Database
    // ==========================================
    // LÃNH THỔ CỦA BÁCH & ÂN (NGHIỆP VỤ THÊM VÉ)
    // Cập nhật: Luồng thêm Vé + Thêm Chi Tiết Hóa Đơn ảo
    // ==========================================
    // Ném hàm này vào trong class VeDAO
    public ArrayList<Object[]> getDanhSachVeTable() {
        ArrayList<Object[]> list = new ArrayList<>();
        // Câu lệnh SQL kéo dữ liệu từ 7 bảng (Đã fix chuẩn 100% theo Database mới)
        String sql = "SELECT v.MaVe, " +
                     "IFNULL(kh.HoTen, 'Khách vãng lai') AS TenKhachHang, " +
                     "p.TenPhim, pc.TenPhong, " +
                     "CONCAT(g.HangGhe, g.SoGhe) AS TenGhe, " +
                     "sc.NgayChieu, sc.GioBatDau, v.GiaVe, v.TrangThai " +
                     "FROM Ve v " +
                     "JOIN SuatChieu sc ON v.MaSuatChieu = sc.MaSuatChieu " +
                     "JOIN Phim p ON sc.MaPhim = p.MaPhim " +
                     "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                     "JOIN PhongChieu pc ON g.MaPhong = pc.MaPhong " +
                     "LEFT JOIN ChiTietHoaDonVe ctv ON v.MaVe = ctv.MaVe " +
                     "LEFT JOIN HoaDon hd ON ctv.MaHoaDon = hd.MaHoaDon " +
                     "LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                     "ORDER BY v.MaVe DESC";
                     
        // Chú ý: utilsJDBC.getConnectDB() là chỗ kết nối Database của ông nha
        try (java.sql.Connection con = org.example.Connection.UtilsJDBC.getConnectDB(); 
             java.sql.PreparedStatement pst = con.prepareStatement(sql);
             java.sql.ResultSet rs = pst.executeQuery()) {
             
            while (rs.next()) {
                // Gom dữ liệu của 1 dòng thành 1 mảng Object[] để ném lên bảng
                Object[] row = new Object[]{
                    "V" + String.format("%03d", rs.getInt("MaVe")), // V001, V002...
                    rs.getString("TenKhachHang"),
                    rs.getString("TenPhim"),
                    rs.getString("TenPhong"),
                    rs.getString("TenGhe"),
                    rs.getDate("NgayChieu"),
                    rs.getTime("GioBatDau"),
                    String.format("%,d VNĐ", rs.getInt("GiaVe")), // Tiền tệ có dấu phẩy
                    rs.getString("TrangThai")
                };
                list.add(row);
            }
        } catch (Exception e) {
            System.err.println("LỖI KÉO DỮ LIỆU BẢNG VÉ RỒI BÁCH ƠI: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public boolean insertVe(VeDTO ve) 
    {
        Connection con = null;
        try 
        {
            con = UtilsJDBC.getConnectDB(); // Mở kết nối
            con.setAutoCommit(false); // BẬT CHẾ ĐỘ TRANSACTION (Khóa an toàn: Bán lỗi là hủy toàn bộ, không lưu nửa vời)

            // --- HÀNH ĐỘNG 1: THÊM XUỐNG BẢNG VÉ ---
            String sqlVe = "INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES (?, ?, ?, 'Đã thanh toán')";
            // Lệnh đặc biệt: Thêm xong trả về cái Mã Vé tự tăng cho tui!
            PreparedStatement pstVe = con.prepareStatement(sqlVe, java.sql.Statement.RETURN_GENERATED_KEYS);
            pstVe.setInt(1, ve.getMaGhe());
            pstVe.setInt(2, ve.getMaSuatChieu());
            pstVe.setInt(3, ve.getGiaVe());
            pstVe.executeUpdate();

            // Lấy cái Mã Vé mới tinh vừa được Database tạo ra
            java.sql.ResultSet rs = pstVe.getGeneratedKeys();
            int maVeMoi = -1;
            if (rs.next()) {
                maVeMoi = rs.getInt(1); // Lụm được mã vé rồi!
            }

            // --- HÀNH ĐỘNG 2: THÊM XUỐNG BẢNG CHI TIẾT HÓA ĐƠN ---
            // FAKE DATA: Tạm thời gắn cứng MaHoaDon = 1 (Cái hóa đơn mồi ông vừa tạo ở Bước 1)
            String sqlChiTiet = "INSERT INTO ChiTietHoaDonVe (MaHoaDon, MaVe, DonGia) VALUES (1, ?, ?)";
            PreparedStatement pstChiTiet = con.prepareStatement(sqlChiTiet);
            pstChiTiet.setInt(1, maVeMoi); // Nhét mã vé mới lụm được vào đây
            pstChiTiet.setInt(2, ve.getGiaVe());
            pstChiTiet.executeUpdate();

            // --- HÀNH ĐỘNG 3: CHỐT ĐƠN ---
            con.commit(); // Lưu tất cả xuống Database!
            return true;
        } 
        catch (Exception e) 
        {
            try { 
                if (con != null) con.rollback();
            } catch (Exception ex) {} 
            
            e.printStackTrace();
            return false;
        }
    }

    
}