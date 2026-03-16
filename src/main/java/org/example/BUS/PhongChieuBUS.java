package org.example.BUS;

import org.example.DAO.PhongChieuDAO;
import org.example.DTO.PhongChieuDTO;

import java.util.ArrayList;

public class PhongChieuBUS {
    private final PhongChieuDAO phongChieuDAO = new PhongChieuDAO();

    public ArrayList<PhongChieuDTO> getList() {
        return phongChieuDAO.selectAll();
    }
}