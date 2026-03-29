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

    // Lấy danh sách ghế theo mã phòng
    public ArrayList<GheDTO> getListGheTheoPhong(int maPhong) {
        ArrayList<GheDTO> listGhe = new ArrayList<>();
        String sql = "SELECT MaGhe, MaPhong, MaLoaiGhe, HangGhe, SoGhe FROM Ghe WHERE MaPhong = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maPhong);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    GheDTO ghe = new GheDTO(
                            rs.getInt("MaGhe"),
                            rs.getInt("MaPhong"),
                            rs.getInt("MaLoaiGhe"),
                            rs.getInt("HangGhe"),
                            rs.getInt("SoGhe")
                    );
                    listGhe.add(ghe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Ghế cho Phòng: " + maPhong);
        }
        return listGhe;
    }

    // Lấy chi tiết 1 phòng chiếu (BAO GỒM CẢ DANH SÁCH GHẾ)
    public PhongChieuDTO getPhongChieuById(int maPhong) {
        PhongChieuDTO room = null;
        String sql = "SELECT MaPhong, TenPhong, LoaiPhong, SoHang, SoGheMoiHang FROM PhongChieu WHERE MaPhong = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Lấy thông tin cơ bản của phòng
                    room = new PhongChieuDTO(
                            rs.getInt("MaPhong"),
                            rs.getString("TenPhong"),
                            rs.getString("LoaiPhong"),
                            rs.getInt("SoHang"),
                            rs.getInt("SoGheMoiHang")
                    );

                    // 2. Lấy danh sách ghế và gắn vào phòng
                    ArrayList<GheDTO> listGhe = getListGheTheoPhong(maPhong);
                    room.setGheList(listGhe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public boolean add(PhongChieuDTO pc) {
        boolean ok = phongChieuDAO.insert(pc);
        if (ok) {
            refreshList(); // Cập nhật lại list ở BUS
        }
        return ok;
    }
}