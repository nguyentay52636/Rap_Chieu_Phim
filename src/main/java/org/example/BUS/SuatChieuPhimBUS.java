package org.example.BUS;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Stub implementation for showtimes. Uses fixed sample data.
 */
public class SuatChieuPhimBUS {

    public ArrayList<Integer> getListPhongByPhim(int maPhim) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        result.add(2);
        return result;
    }

    public ArrayList<Integer> getListSuatByPhim(int maPhim) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        result.add(2);
        result.add(3);
        return result;
    }

    public int getMaSuatChieuPhim(int maPhim, int maPhong, int maSuatChieu, Date ngayChieu) {
        // Trả về id giả lập từ các tham số
        return Math.abs((maPhim * 100 + maPhong * 10 + maSuatChieu));
    }

    public int getGiaVeGoc(int maSuatChieuPhim) {
        // Giá vé cố định
        return 80000;
    }
}

