package org.example.BUS;

import org.example.DAO.SuatChieuPhimDAO;
import org.example.DTO.SuatChieuPhimDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuPhimBUS {
    private final SuatChieuPhimDAO suatChieuDAO = new SuatChieuPhimDAO();
    private List<SuatChieuPhimDTO> listSc = new ArrayList<>();

    public SuatChieuPhimBUS() {
        refreshList();
    }

    public void refreshList() {
        listSc = suatChieuDAO.selectAll();
    }

    public List<SuatChieuPhimDTO> getListSuatChieu() {
        return listSc;
    }

    // Hàm kiểm tra xem lịch mới có bị trùng với lịch đã có trong hệ thống không
    private boolean isTrungLichChieu(SuatChieuPhimDTO scMoi) {
        // TỐI ƯU: Dùng luôn listSc (cache) thay vì gọi DAO chọc xuống DB
        for (SuatChieuPhimDTO scCu : listSc) {
            // Chỉ kiểm tra trùng lịch nếu CÙNG PHÒNG CHIẾU
            // (Khi đang Cập nhật, bỏ qua việc tự so sánh với chính bản ghi cũ của nó)
            if (scCu.getMaPhong() == scMoi.getMaPhong() && scCu.getMaSuatChieu() != scMoi.getMaSuatChieu()) {

                // Công thức kiểm tra 2 khoảng thời gian giao nhau:
                // (Bắt_đầu_mới < Kết_thúc_cũ) VÀ (Kết_thúc_mới > Bắt_đầu_cũ)
                boolean biTrung = scMoi.getGioBatDau().isBefore(scCu.getGioKetThuc()) &&
                        scMoi.getGioKetThuc().isAfter(scCu.getGioBatDau());

                if (biTrung) {
                    return true; // Bị trùng giờ!
                }
            }
        }
        return false; // Lịch an toàn, không trùng
    }

    // Đã gộp logic bảo vệ vào một hàm add duy nhất
    public boolean add(SuatChieuPhimDTO sc) throws IllegalArgumentException {
        if (!sc.getGioKetThuc().isAfter(sc.getGioBatDau())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!");
        }
        if (isTrungLichChieu(sc)) {
            throw new IllegalArgumentException("Phòng " + sc.getMaPhong() + " đã có lịch chiếu trong khung giờ này!");
        }

        boolean ok = suatChieuDAO.insert(sc);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean update(SuatChieuPhimDTO sc) throws IllegalArgumentException {
        // 1. Bắt lỗi du hành thời gian (Giờ kết thúc <= Giờ bắt đầu)
        if (!sc.getGioKetThuc().isAfter(sc.getGioBatDau())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!");
        }

        // 2. Bắt lỗi trùng lịch chiếu trong cùng 1 phòng
        if (isTrungLichChieu(sc)) {
            throw new IllegalArgumentException("Phòng " + sc.getMaPhong() + " đã có lịch chiếu trong khung giờ này!");
        }

        // 3. Vượt qua hết vòng gửi xe thì tiến hành gọi DAO
        boolean ok = suatChieuDAO.update(sc);
        if (ok) {
            refreshList(); // Cập nhật lại list cache
        }
        return ok;
    }

    public boolean delete(int maSuatChieu) {
        boolean ok = suatChieuDAO.delete(maSuatChieu);
        if (ok) {
            refreshList();
        }
        return ok;
    }
    public List<SuatChieuPhimDTO> searchAdvanced(String maPhim, String maPhong, LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<SuatChieuPhimDTO> result = new ArrayList<>();

        for (SuatChieuPhimDTO sc : listSc) {
            boolean match = true;

            // 1. Lọc theo Mã phim (nếu người dùng có nhập)
            if (maPhim != null && !maPhim.trim().isEmpty()) {
                if (!String.valueOf(sc.getMaPhim()).equals(maPhim.trim())) {
                    match = false;
                }
            }

            // 2. Lọc theo Mã phòng (nếu người dùng có nhập)
            if (maPhong != null && !maPhong.trim().isEmpty()) {
                if (!String.valueOf(sc.getMaPhong()).equals(maPhong.trim())) {
                    match = false;
                }
            }

            // 3. Lọc theo khoảng thời gian (So sánh với Giờ bắt đầu của suất chiếu)
            if (tuNgay != null && sc.getGioBatDau().isBefore(tuNgay)) {
                match = false; // Bị loại nếu suất chiếu bắt đầu TRƯỚC "từ ngày"
            }
            if (denNgay != null && sc.getGioBatDau().isAfter(denNgay)) {
                match = false; // Bị loại nếu suất chiếu bắt đầu SAU "đến ngày"
            }

            // Nếu vượt qua mọi điều kiện thì thêm vào danh sách kết quả
            if (match) {
                result.add(sc);
            }
        }
        return result;
    }

    public List<SuatChieuPhimDTO> search(String tieuChi, String tuKhoa) {
        return suatChieuDAO.search(tieuChi, tuKhoa);
    }
}