package org.example.DTO;

import java.util.ArrayList;
import java.util.List;

public class PhongChieuDTO 
{
    private int maPhong;
    private String tenPhong;
    private String loaiPhong;
    private int soHang;
    private int soGheMoiHang;

    private List<GheDTO> gheList = new ArrayList<GheDTO>();

    public PhongChieuDTO() {}

    public PhongChieuDTO(int maPhong, String tenPhong, String loaiPhong, int soHang, int soGheMoiHang) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.soHang = soHang;
        this.soGheMoiHang = soGheMoiHang;
        this.gheList = new ArrayList<>();
    }


    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    public int getSoHang() { return soHang; }
    public void setSoHang(int soHang) { this.soHang = soHang; }

    public int getSoGheMoiHang() { return soGheMoiHang; }
    public void setSoGheMoiHang(int soGheMoiHang) { this.soGheMoiHang = soGheMoiHang; }

    public List<GheDTO> getGheList() { return gheList; }
    public void setGheList(List<GheDTO> gheList) { this.gheList = gheList; }
}
