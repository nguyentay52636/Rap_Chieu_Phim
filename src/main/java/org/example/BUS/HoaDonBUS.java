package org.example.BUS;

import org.example.DTO.HoaDonDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub invoice BUS. Stores invoices in memory only.
 */
public class HoaDonBUS {

    private final List<HoaDonDTO> list = new ArrayList<>();
    private final List<String> cthdVe = new ArrayList<>();
    private int nextId = 1;

    public int add(HoaDonDTO hoaDon) {
        HoaDonDTO stored = new HoaDonDTO(nextId, hoaDon.getMaKH(), hoaDon.getMaNV(),
                hoaDon.getNgayBan(), hoaDon.getSoLuongVe(), hoaDon.getTongTienVe(),
                hoaDon.getTongTienSanPham(), hoaDon.getMaKhuyenMai(),
                hoaDon.getTongTienGiam(), hoaDon.getTongThanhToan());
        list.add(stored);
        return nextId++;
    }

    public void addCTHDVe(int maHoaDon, int maVe, int giaVe) {
        cthdVe.add("HD" + maHoaDon + "-VE" + maVe + "-" + giaVe);
    }
}

