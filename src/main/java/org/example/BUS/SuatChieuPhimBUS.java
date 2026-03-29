package org.example.BUS;

import org.example.DAO.SuatChieuPhimDAO;
import org.example.DTO.SuatChieuPhimDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuPhimBUS {
    private final SuatChieuPhimDAO suatChieuDAO = new SuatChieuPhimDAO();
    private List<SuatChieuPhimDTO> listSc = new ArrayList<>();

    // Chỉ dùng 1 format dd/MM/yyyy HH:mm cho cả form và Excel
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SuatChieuPhimBUS() {
        refreshList();
    }

    public void refreshList() {
        listSc = suatChieuDAO.selectAll();
    }

    public List<SuatChieuPhimDTO> getListSuatChieu() {
        return listSc;
    }

    private boolean isTrungLichChieu(SuatChieuPhimDTO scMoi) {
        for (SuatChieuPhimDTO scCu : listSc) {
            // Kiểm tra cùng mã phòng và khác ID suất chiếu (trường hợp đang Sửa)
            if (scCu.getMaPhong() == scMoi.getMaPhong() && scCu.getMaSuatChieu() != scMoi.getMaSuatChieu()) {
                // Do LocalDateTime chứa cả ngày giờ, nên check khoảng thời gian giao nhau là đủ
                boolean biTrung = scMoi.getGioBatDau().isBefore(scCu.getGioKetThuc()) &&
                        scMoi.getGioKetThuc().isAfter(scCu.getGioBatDau());
                if (biTrung) {
                    return true;
                }
            }
        }
        return false;
    }

    // =========================================================================
    // HÀM KIỂM TRA DỮ LIỆU DÙNG CHUNG (VALIDATION)
    // =========================================================================
    private void validateSuatChieu(SuatChieuPhimDTO sc) throws IllegalArgumentException {
        // 1. Kiểm tra rỗng
        if (sc.getGioBatDau() == null || sc.getGioKetThuc() == null) {
            throw new IllegalArgumentException("Thời gian không được để trống!");
        }

        // 2. Kiểm tra giờ bắt đầu / kết thúc
        if (!sc.getGioKetThuc().isAfter(sc.getGioBatDau())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!");
        }

        // 3. Kiểm tra quá khứ (Không quá 3 tháng)
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        if (sc.getGioBatDau().isBefore(threeMonthsAgo)) {
            throw new IllegalArgumentException("Không thể xếp lịch chiếu cho thời gian trong quá khứ vượt quá 3 tháng!");
        }

        // 4. Kiểm tra giá vé (Không âm và >= 30,000)
        if (sc.getGiaVeGoc() < 30000) {
            throw new IllegalArgumentException("Giá vé không hợp lệ! Giá vé phải từ 30,000 VNĐ trở lên.");
        }

        // 5. Kiểm tra trùng lịch chiếu
        if (isTrungLichChieu(sc)) {
            throw new IllegalArgumentException("Phòng " + sc.getMaPhong() + " đã có lịch chiếu bị trùng thời gian!");
        }
    }

    public boolean add(SuatChieuPhimDTO sc) throws IllegalArgumentException {
        validateSuatChieu(sc);
        boolean ok = suatChieuDAO.insert(sc);
        if (ok) refreshList();
        return ok;
    }

    public boolean update(SuatChieuPhimDTO sc) throws IllegalArgumentException {
        validateSuatChieu(sc);
        boolean ok = suatChieuDAO.update(sc);
        if (ok) refreshList();
        return ok;
    }

    public boolean delete(int maSuatChieu) {
        boolean ok = suatChieuDAO.delete(maSuatChieu);
        if (ok) refreshList();
        return ok;
    }

    public List<SuatChieuPhimDTO> searchAdvanced(String maPhim, String maPhong, LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<SuatChieuPhimDTO> result = new ArrayList<>();
        for (SuatChieuPhimDTO sc : listSc) {
            boolean match = true;
            if (maPhim != null && !maPhim.trim().isEmpty()) {
                if (!String.valueOf(sc.getMaPhim()).equals(maPhim.trim())) match = false;
            }
            if (maPhong != null && !maPhong.trim().isEmpty()) {
                if (!String.valueOf(sc.getMaPhong()).equals(maPhong.trim())) match = false;
            }
            if (tuNgay != null && sc.getGioBatDau().isBefore(tuNgay)) match = false;
            if (denNgay != null && sc.getGioBatDau().isAfter(denNgay)) match = false;
            if (match) result.add(sc);
        }
        return result;
    }

    public List<SuatChieuPhimDTO> search(String tieuChi, String tuKhoa) {
        return suatChieuDAO.search(tieuChi, tuKhoa);
    }

    // =========================================================================
    // TÍNH NĂNG EXCEL (Chỉ còn 6 cột)
    // =========================================================================

    public void exportExcel(File file) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SuatChieuPhim");

        // Quay lại 6 cột (Không có Ngày Chiếu riêng)
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã SC", "Mã Phim", "Mã Phòng", "Giờ Bắt Đầu", "Giờ Kết Thúc", "Giá Vé"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (SuatChieuPhimDTO sc : listSc) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sc.getMaSuatChieu());
            row.createCell(1).setCellValue(sc.getMaPhim());
            row.createCell(2).setCellValue(sc.getMaPhong());

            row.createCell(3).setCellValue(sc.getGioBatDau().format(formatter));
            row.createCell(4).setCellValue(sc.getGioKetThuc().format(formatter));

            row.createCell(5).setCellValue(sc.getGiaVeGoc());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public String importExcel(File file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Đọc theo 6 cột
                    int maPhim = (int) getNumericValue(row.getCell(1));
                    int maPhong = (int) getNumericValue(row.getCell(2));

                    String strBatDau = getStringValue(row.getCell(3)); // Giờ Bắt Đầu ở cột 3
                    String strKetThuc = getStringValue(row.getCell(4)); // Giờ Kết Thúc ở cột 4
                    LocalDateTime batDau = LocalDateTime.parse(strBatDau, formatter);
                    LocalDateTime ketThuc = LocalDateTime.parse(strKetThuc, formatter);

                    double giaVe = getNumericValue(row.getCell(5)); // Giá vé ở cột 5

                    SuatChieuPhimDTO newSc = new SuatChieuPhimDTO(0, maPhim, maPhong, batDau, ketThuc, giaVe);

                    // Add hàm check (đã bao gồm các logic bắt lỗi)
                    if (this.add(newSc)) {
                        successCount++;
                    }

                } catch (IllegalArgumentException ex) {
                    failCount++;
                    errorMsg.append("Dòng ").append(i + 1).append(" bị từ chối: ").append(ex.getMessage()).append("\n");
                } catch (Exception ex) {
                    failCount++;
                    errorMsg.append("Dòng ").append(i + 1).append(" lỗi dữ liệu: ").append(ex.getMessage()).append("\n");
                }
            }
        }
        return "Nhập thành công: " + successCount + " suất chiếu.\nThất bại: " + failCount + " suất chiếu.\n" + errorMsg.toString();
    }

    private double getNumericValue(Cell cell) throws Exception {
        if (cell == null) throw new Exception("Ô trống");
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) return Double.parseDouble(cell.getStringCellValue());
        throw new Exception("Sai định dạng số");
    }

    public List<org.example.DTO.TrangThaiGheDTO> layTrangThaiGhe(int maSuatChieu, int maPhong) {
        return suatChieuDAO.layTrangThaiGhe(maSuatChieu, maPhong);
    }

    private String getStringValue(Cell cell) throws Exception {
        if (cell == null) throw new Exception("Ô trống");
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        throw new Exception("Sai định dạng chuỗi");
    }

    // Nối từ GUI xuống DAO
    public List<SuatChieuPhimDTO> laySuatChieuTheoNgayVaPhim(java.sql.Date ngayChieu, int maPhim) {
        return suatChieuDAO.laySuatChieuTheoNgayVaPhim(ngayChieu, maPhim);
    }
}