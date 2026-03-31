package org.example.BUS;

import org.example.DAO.HoaDonDAO;
import org.example.DTO.HoaDonDTO;

import java.util.ArrayList;

public class HoaDonBUS {

    private final HoaDonDAO hoaDonDAO;

    public HoaDonBUS() {
        this.hoaDonDAO = new HoaDonDAO();
    }

    public int add(HoaDonDTO hoaDon) {
        return hoaDonDAO.add(hoaDon);
    }

    public boolean addCTHDVe(int maHoaDon, int maVe, int giaVe) {
        return hoaDonDAO.addCTHDVe(maHoaDon, maVe, giaVe);
    }

    public HoaDonDTO findById(int maHoaDon) {
        return hoaDonDAO.findById(maHoaDon);
    }

    public ArrayList<HoaDonDTO> search(String keyword) {
        return hoaDonDAO.search(keyword);
    }

    public boolean update(HoaDonDTO hoaDon) {
        return hoaDonDAO.update(hoaDon);
    }

    public boolean delete(int maHoaDon) {
        return hoaDonDAO.delete(maHoaDon);
    }
}
