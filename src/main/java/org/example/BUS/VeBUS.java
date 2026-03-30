package org.example.BUS;

import org.example.DAO.VeDAO;
import org.example.DTO.VeDTO;

import java.util.ArrayList;
import java.util.List;

public class VeBUS {

    // Gọi duy nhất 1 thằng DAO để giao tiếp với Database
    private VeDAO veDAO = new VeDAO();

    // ==========================================
    // 1. LẤY DANH SÁCH VÉ
    // ==========================================
    public List<VeDTO> getAllVe() {
        // Lấy dữ liệu THẬT từ Database lên
        return veDAO.selectAll();
    }

    // ==========================================
    // 2. NGHIỆP VỤ THÊM / HỦY VÉ
    // ==========================================
    public boolean add(VeDTO ve) {
        // Kiểm tra logic cơ bản trước khi đẩy xuống DB
        // (Đã sửa lại thành getMaSuatChieuPhim() cho khớp với DTO của nhóm)
        if (ve.getMaGhe() <= 0 || ve.getMaSuatChieuPhim() <= 0) {
            System.err.println("Lỗi: Mã ghế hoặc mã suất chiếu không hợp lệ!");
            return false;
        }
        if (ve.getGiaVe() < 0) {
            System.err.println("Lỗi: Giá vé không được âm!");
            return false;
        }

        // Gọi DAO để INSERT thẳng xuống MySQL
        return veDAO.insert(ve); 
    }

    public boolean delete(int maVe) {
        if (maVe <= 0) return false;
        
        // Hủy vé dưới CSDL (Đổi trạng thái thành Đã Hủy)
        return veDAO.delete(maVe); 
    }

    // ==========================================
    // 3. TÌM KIẾM VÉ ĐA NĂNG (Gõ tới đâu lọc tới đó)
    // ==========================================
    public List<VeDTO> timKiemVe(String tieuChi, String tuKhoa, String trangThai) {
        List<VeDTO> danhSachGoc = veDAO.selectAll();
        List<VeDTO> ketQua = new ArrayList<>();

        tuKhoa = tuKhoa.toLowerCase().trim();

        for (VeDTO ve : danhSachGoc) {
            String maVe = String.valueOf(ve.getMaVe());
            String khachHang = ve.getKhachHang() != null ? ve.getKhachHang().toLowerCase() : "";
            String tenPhim = ve.getTenPhim() != null ? ve.getTenPhim().toLowerCase() : "";
            String tt = ve.getTrangThai();

            // 1. Khớp từ khóa
            boolean khopTuKhoa = false;
            if (tuKhoa.isEmpty()) {
                khopTuKhoa = true;
            } else {
                switch (tieuChi) {
                    case "Tất cả":
                        if (maVe.contains(tuKhoa) || tenPhim.contains(tuKhoa) || khachHang.contains(tuKhoa)) khopTuKhoa = true;
                        break;
                    case "Mã Vé":
                        if (maVe.contains(tuKhoa)) khopTuKhoa = true;
                        break;
                    case "Tên Phim":
                        if (tenPhim.contains(tuKhoa)) khopTuKhoa = true;
                        break;
                    case "Khách Hàng":
                        if (khachHang.contains(tuKhoa)) khopTuKhoa = true;
                        break;
                }
            }

            // 2. Khớp trạng thái
            boolean khopTrangThai = false;
            if (trangThai.equals("Tất cả")) {
                khopTrangThai = true;
            } else if (tt != null && tt.equalsIgnoreCase(trangThai)) {
                khopTrangThai = true;
            }

            // 3. Đúng cả 2 tiêu chuẩn thì bế vào danh sách
            if (khopTuKhoa && khopTrangThai) {
                ketQua.add(ve);
            }
        }
        return ketQua;
    }

    // ==========================================
    // 4. LẤY GHẾ TRỐNG (Đã bỏ data giả)
    // ==========================================
    public List<String> getGheTrong(int maSuatChieuPhim) {
        // Chọc xuống CSDL để lấy danh sách ghế thực tế
        return veDAO.layDanhSachGheTrong(maSuatChieuPhim); 
    }
}