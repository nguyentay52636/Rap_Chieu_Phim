package org.example.BUS;

import org.example.Connection.UtilsJDBC;
import org.example.DAO.PhongChieuDAO;
import org.example.DTO.GheDTO;
import org.example.DTO.PhongChieuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongChieuBUS {
    private final PhongChieuDAO phongChieuDAO = new PhongChieuDAO();
    private List<PhongChieuDTO> listPc = new ArrayList<>();

    public PhongChieuBUS() {
        refreshList();
    }

    public void refreshList()
    {
        listPc = phongChieuDAO.selectAll();
    }

    public List<PhongChieuDTO> getList() {
        return listPc;
    }


    public boolean update(PhongChieuDTO pc){
        boolean ok = phongChieuDAO.update(pc);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean delete(int maPC){
        boolean ok = phongChieuDAO.delete(maPC);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public ArrayList<GheDTO> getListGheTheoPhong(int maPhong) {
        return phongChieuDAO.getListGheTheoPhong(maPhong);
    }

    public PhongChieuDTO getPhongChieuById(int maPhong) {
        return phongChieuDAO.getPhongChieuById(maPhong);
    }

    public boolean add(PhongChieuDTO pc) {
        boolean ok = phongChieuDAO.insert(pc);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean updateChairType(int maPhong, List<String> selectedSeats, int maLoaiGheMoi) {
        return phongChieuDAO.updateChairTypeBatch(maPhong, selectedSeats, maLoaiGheMoi);
    }

    public boolean updateRoomAndSeats(PhongChieuDTO pc) {
        boolean ok = phongChieuDAO.updateRoomAndSeatsTransaction(pc);
        if (ok) {
            refreshList();
        }
        return ok;
    }

    public boolean addRoomWithSeats(PhongChieuDTO pc, int defaultMaLoaiGhe) {
        boolean ok = phongChieuDAO.insertRoomAndSeatsTransaction(pc, defaultMaLoaiGhe);
        if (ok) {
            refreshList();
        }
        return ok;
    }
}