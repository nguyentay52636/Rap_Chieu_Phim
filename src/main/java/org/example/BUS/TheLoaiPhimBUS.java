package org.example.BUS;

import org.example.DAO.TheLoaiPhimDAO;
import org.example.DTO.TheLoaiPhimDTO;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiPhimBUS {

    private final TheLoaiPhimDAO theLoaiPhimDAO = new TheLoaiPhimDAO();
    private List<TheLoaiPhimDTO> listTheLoai = new ArrayList<>();

    public TheLoaiPhimBUS() {
        refreshList();
    }

    public void refreshList() {
        listTheLoai = theLoaiPhimDAO.selectAll();
    }

    public List<TheLoaiPhimDTO> getList() {
        if (listTheLoai == null || listTheLoai.isEmpty()) {
            refreshList();
        }
        return listTheLoai;
    }

    public boolean add(TheLoaiPhimDTO theloai) {
        boolean ok = theLoaiPhimDAO.insert(theloai);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean update(TheLoaiPhimDTO theloai) {
        boolean ok = theLoaiPhimDAO.update(theloai);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean delete(int maLoaiPhim) {
        boolean ok = theLoaiPhimDAO.delete(maLoaiPhim);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public String getTenLoaiPhimById(int maLoaiPhim) {
        for (TheLoaiPhimDTO tl : getList()) {
            if (tl.getMaLoaiPhim() == maLoaiPhim) {
                return tl.getTenLoaiPhim();
            }
        }
        return "Không xác định";
    }
}
