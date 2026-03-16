package org.example.BUS;

import org.example.DAO.KhachHangDAO;
import org.example.DTO.KhachHangDTO;

import java.util.ArrayList;
import java.util.List;

public class KhachHangBUS {
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private List<KhachHangDTO> listKh = new ArrayList<>();

    public KhachHangBUS() {
        refreshList();
    }

    public void refreshList()
    {
        listKh = khachHangDAO.selectAll();
    }
    public List<KhachHangDTO> getListKhachHang() {
        return listKh;
    }

    public boolean add(KhachHangDTO kh) {
        boolean ok = khachHangDAO.insert(kh);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean delete(int maKH){
        boolean ok = khachHangDAO.delete(maKH);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean update(KhachHangDTO kh){
        boolean ok = khachHangDAO.update(kh);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean checkTrungSDT(String sdt) {
        return khachHangDAO.checkTrungSDT(sdt);
    }

    public List<KhachHangDTO> search(String tieuChi, String tuKhoa) {
        return khachHangDAO.search(tieuChi, tuKhoa);
    }

    public List<KhachHangDTO> advancedSearch(String hoTen, String sdt, java.util.Date tuNgay, java.util.Date denNgay, String diemTu, String diemDen, String phepTinh, String diemToanTu, List<String> selectedHangs){
        return khachHangDAO.advancedSearch(hoTen, sdt, tuNgay, denNgay, diemTu, diemDen, phepTinh, diemToanTu, selectedHangs);
    }
}
