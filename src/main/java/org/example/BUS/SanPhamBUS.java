package org.example.BUS;

import org.example.DAO.SanPhamDAO;
import org.example.DTO.SanPhamDTO;

import java.util.ArrayList;
import java.util.List;

public class SanPhamBUS {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private List<SanPhamDTO> listSp = new ArrayList<>();

    public SanPhamBUS() {
        refreshList();
    }

    public void refreshList() {
        listSp = sanPhamDAO.selectAll();
    }

    public List<SanPhamDTO> getList() {
        if (listSp == null) {
            refreshList();
        }
        return listSp;
    }

    public boolean add(SanPhamDTO sp) {
        boolean ok = sanPhamDAO.insert(sp);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean update(SanPhamDTO sp) {
        boolean ok = sanPhamDAO.update(sp);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean delete(int maSanPham) {
        boolean ok = sanPhamDAO.delete(maSanPham);
        if (ok) {
            refreshList();
        }
        return ok;
    }
}
