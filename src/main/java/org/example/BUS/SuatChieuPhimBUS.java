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

    // Định dạng ngày giờ chuẩn để ghi/đọc Excel
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
            if (scCu.getMaPhong() == scMoi.getMaPhong() && scCu.getMaSuatChieu() != scMoi.getMaSuatChieu()) {
                boolean biTrung = scMoi.getGioBatDau().isBefore(scCu.getGioKetThuc()) &&
                        scMoi.getGioKetThuc().isAfter(scCu.getGioBatDau());
                if (biTrung) {
                    return true;
                }
            }
        }
        return false;
    }

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
        if (!sc.getGioKetThuc().isAfter(sc.getGioBatDau())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!");
        }
        if (isTrungLichChieu(sc)) {
            throw new IllegalArgumentException("Phòng " + sc.getMaPhong() + " đã có lịch chiếu trong khung giờ này!");
        }

        boolean ok = suatChieuDAO.update(sc);
        if (ok) {
            refreshList();
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
    // TÍNH NĂNG EXCEL
    // =========================================================================

    // 1. Xuất dữ liệu ra Excel
    public void exportExcel(File file) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SuatChieuPhim");

        // Tạo dòng Header
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã SC", "Mã Phim", "Mã Phòng", "Giờ Bắt Đầu", "Giờ Kết Thúc", "Giá Vé"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu từ list vào Excel
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

        // Tự động căn chỉnh độ rộng cột
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // 2. Nhập dữ liệu từ Excel (Trả về thông báo kết quả)
    public String importExcel(File file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Duyệt từ dòng 1 (bỏ qua header ở dòng 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Cột 0 là Mã SC -> Bỏ qua vì khi Import DB sẽ tự sinh mã mới (Auto Increment)
                    // Đọc các cột tiếp theo
                    int maPhim = (int) getNumericValue(row.getCell(1));
                    int maPhong = (int) getNumericValue(row.getCell(2));

                    String strBatDau = getStringValue(row.getCell(3));
                    String strKetThuc = getStringValue(row.getCell(4));
                    LocalDateTime batDau = LocalDateTime.parse(strBatDau, formatter);
                    LocalDateTime ketThuc = LocalDateTime.parse(strKetThuc, formatter);

                    double giaVe = getNumericValue(row.getCell(5));

                    // Tạo đối tượng DTO (truyền mã SC là 0 để DB tự tăng)
                    SuatChieuPhimDTO newSc = new SuatChieuPhimDTO(0, maPhim, maPhong, batDau, ketThuc, giaVe);

                    // Tận dụng hàm add() để nó check trùng lịch chiếu luôn
                    this.add(newSc);
                    successCount++;

                } catch (Exception ex) {
                    failCount++;
                    errorMsg.append("Dòng ").append(i + 1).append(" lỗi: ").append(ex.getMessage()).append("\n");
                }
            }
        }
        return "Nhập thành công: " + successCount + " suất chiếu.\nThất bại: " + failCount + " suất chiếu.\n" + errorMsg.toString();
    }

    // Hàm hỗ trợ đọc giá trị số từ cell an toàn
    private double getNumericValue(Cell cell) throws Exception {
        if (cell == null) throw new Exception("Ô trống");
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) return Double.parseDouble(cell.getStringCellValue());
        throw new Exception("Sai định dạng số");
    }

    // Hàm hỗ trợ đọc chuỗi từ cell an toàn
    private String getStringValue(Cell cell) throws Exception {
        if (cell == null) throw new Exception("Ô trống");
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        throw new Exception("Sai định dạng chuỗi");
    }
}