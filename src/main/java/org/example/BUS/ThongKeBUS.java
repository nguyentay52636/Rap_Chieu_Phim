package org.example.BUS;

import org.example.DAO.ThongKeDAO;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ThongKeBUS {

    private final ThongKeDAO thongKeDAO;

    public ThongKeBUS() {
        this.thongKeDAO = new ThongKeDAO();
    }

    public long getTongDoanhThu(String tuNgay, String denNgay) {
        return thongKeDAO.getTongDoanhThu(parseStartDate(tuNgay), parseEndDate(denNgay));
    }

    public int getTongSoVeBan(String tuNgay, String denNgay) {
        return thongKeDAO.getTongSoVeBan(parseStartDate(tuNgay), parseEndDate(denNgay));
    }

    public int getTongSoSanPhamBan(String tuNgay, String denNgay) {
        return thongKeDAO.getTongSoSanPhamBan(parseStartDate(tuNgay), parseEndDate(denNgay));
    }

    public ArrayList<Object[]> getBangXepHangDoanhThuTheoPhim(String tuNgay, String denNgay) {
        return thongKeDAO.getBangXepHangDoanhThuTheoPhim(parseStartDate(tuNgay), parseEndDate(denNgay));
    }

    public Object[] getTongQuanThongKe(String tuNgay, String denNgay) {
        long tongDoanhThu = getTongDoanhThu(tuNgay, denNgay);
        int tongSoVeBan = getTongSoVeBan(tuNgay, denNgay);
        int tongSoSanPhamBan = getTongSoSanPhamBan(tuNgay, denNgay);
        return new Object[]{tongDoanhThu, tongSoVeBan, tongSoSanPhamBan};
    }

    private Timestamp parseStartDate(String dateText) {
        if (dateText == null || dateText.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateText.trim());
            return Timestamp.valueOf(date.atStartOfDay());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày bắt đầu không đúng định dạng yyyy-MM-dd: " + dateText);
        }
    }

    private Timestamp parseEndDate(String dateText) {
        if (dateText == null || dateText.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateText.trim());
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return Timestamp.valueOf(endOfDay);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày kết thúc không đúng định dạng yyyy-MM-dd: " + dateText);
        }
    }
}
