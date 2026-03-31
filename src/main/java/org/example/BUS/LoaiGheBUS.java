package org.example.BUS;

import org.example.DAO.LoaiGheDAO;
import org.example.DTO.LoaiGheDTO;

import java.util.ArrayList;

public class LoaiGheBUS {
    private final LoaiGheDAO loaiGheDAO = new LoaiGheDAO();

    public ArrayList<LoaiGheDTO> getList() {
        return loaiGheDAO.selectAll();
    }
}