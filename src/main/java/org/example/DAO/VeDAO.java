package org.example.DAO;

import org.example.DTO.VeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class VeDAO 
{
    // ==========================================
    // LÃNH THỔ CỦA ÂN  (QUẢN LÝ - XEM & HỦY)
    // ==========================================
    
    // 1. Hàm lấy danh sách vé (Bạn ông phải viết câu lệnh SQL JOIN vào đây)
    public ArrayList<VeDTO> selectAll() 
    {
        ArrayList<VeDTO> list = new ArrayList<>();
        // ... Code SQL JOIN và vòng lặp ResultSet của bạn ông ở đây ...
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
    public boolean insertVe(VeDTO ve) 
    {
        // ... Code INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES (?, ?, ?, 'DaBan') ...
        return false;
    }
}