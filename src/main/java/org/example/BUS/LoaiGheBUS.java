package org.example.BUS;

import org.example.DAO.LoaiGheDAO;
import org.example.DTO.LoaiGheDTO;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class LoaiGheBUS {

    private final LoaiGheDAO loaiGheDAO = new LoaiGheDAO();
    private List<LoaiGheDTO> listLoaiGhe = new ArrayList<>();

    public LoaiGheBUS() {
        refreshList();
    }

    public void refreshList() {
        listLoaiGhe = loaiGheDAO.selectAll();
    }

    public List<LoaiGheDTO> getList() {
        if (listLoaiGhe == null || listLoaiGhe.isEmpty()) {
            refreshList();
        }
        return listLoaiGhe;
    }
}
