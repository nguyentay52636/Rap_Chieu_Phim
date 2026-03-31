package org.example.BUS;

import org.example.DAO.HoaDonDAO;
import org.example.DTO.HoaDonDTO;

import java.util.ArrayList;

public class HoaDonBUS {

    private final HoaDonDAO hoaDonDAO;

    public HoaDonBUS() {
        this.hoaDonDAO = new HoaDonDAO();
    }

    public ArrayList<HoaDonDTO> getAll() {
        return hoaDonDAO.getAll();
    }

    public int add(HoaDonDTO hoaDon) {
        return hoaDonDAO.add(hoaDon);
    }

    public boolean addCTHDVe(int maHoaDon, int maVe, int donGia) {
        return hoaDonDAO.addCTHDVe(maHoaDon, maVe, donGia);
    }

    public HoaDonDTO findById(int maHoaDon) {
        return hoaDonDAO.findById(maHoaDon);
    }

    public ArrayList<HoaDonDTO> search(String keyword) {
        return hoaDonDAO.search(keyword);
    }

    public ArrayList<HoaDonDTO> search(String maHoaDon, String maKH, String maNV, String thoiGian) {
        return hoaDonDAO.search(maHoaDon, maKH, maNV, thoiGian);
    }

    public boolean update(HoaDonDTO hoaDon) {
        return hoaDonDAO.update(hoaDon);
    }

    public boolean delete(int maHoaDon) {
        return hoaDonDAO.delete(maHoaDon);
    }
}
