package org.example.BUS;

import org.example.DAO.HoaDonDAO;
import org.example.DTO.HoaDonDTO;

import java.util.ArrayList;

public class HoaDonBUS {

    public int add(HoaDonDTO hoaDon) {
        return HoaDonDAO.getInstance().add(hoaDon);
    }

    public void addCTHDVe(int maHoaDon, int maVe, int giaVe) {
        HoaDonDAO.getInstance().addCTHDVe(maHoaDon, maVe, giaVe);
    }

    public HoaDonDTO findHoaDonById(int maHoaDon) {
        return HoaDonDAO.getInstance().findById(maHoaDon);
    }

    public ArrayList<HoaDonDTO> searchHoaDon(String keyword) {
        return HoaDonDAO.getInstance().search(keyword);
    }
}
