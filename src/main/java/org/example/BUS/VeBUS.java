package org.example.BUS;

import org.example.DAO.VeDAO;
import org.example.DTO.VeDTO;
import java.util.ArrayList;

public class VeBUS 
{
    private VeDAO veDAO = new VeDAO();
    private ArrayList<VeDTO> listVe = null;

    // ==========================================
    // LÃNH THỔ CỦA BẠN ÂN (QUẢN LÝ - XEM & TÌM KIẾM)
    // ==========================================
    
    public void docDanhSach() 
    {
        listVe = veDAO.selectAll();
    }

    public ArrayList<VeDTO> getDanhSach() 
    {
        if (listVe == null) docDanhSach();
        return listVe;
    }

    // Bạn ông sẽ viết hàm dùng vòng lặp filter danh sách vé theo keyword ở đây
    public ArrayList<VeDTO> timKiem(String keyword) 
    {
        ArrayList<VeDTO> result = new ArrayList<>();
        // ... code tìm kiếm ...
        return result;
    }
    
    public boolean huyVe(int maVe)
    {
        if(veDAO.huyVe(maVe)) {
            docDanhSach();
            return true;
        }
        return false;
    }

    // ==========================================
    // LÃNH THỔ CỦA BÁCH (NGHIỆP VỤ BÁN VÉ)
    // ==========================================

    public boolean banVe(VeDTO ve) 
    {
        // 1. Kiểm tra logic đầu vào
        if (ve.getMaGhe() == 0 || ve.getMaSuatChieu() == 0) {
            return false; // Chưa chọn đủ thông tin
        }

        // 2. Kiểm tra xem ghế đã bị thằng khác nhanh tay mua mất chưa
        if (veDAO.kiemTraGheDaBan(ve.getMaSuatChieu(), ve.getMaGhe())) {
            return false; // Trùng ghế
        }

        // 3. Lưu xuống CSDL
        if (veDAO.insertVe(ve)) {
            docDanhSach(); // Cập nhật lại danh sách RAM
            return true;
        }
        return false;
    }
}