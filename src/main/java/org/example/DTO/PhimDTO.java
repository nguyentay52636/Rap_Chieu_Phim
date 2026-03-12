package org.example.DTO;

public class PhimDTO {

    private int maPhim;
    private int maTheLoaiPhim;
    private String tenPhim;
    private String thoiLuong; // ví dụ \"02:00\"
    private String daoDien;
    private int namSanXuat;
    private int gioiHanTuoi;
    private String posterURL;

    public PhimDTO(int maPhim, int maTheLoaiPhim, String tenPhim,
                   String thoiLuong, String daoDien, int namSanXuat,
                   int gioiHanTuoi, String posterURL) {
        this.maPhim = maPhim;
        this.maTheLoaiPhim = maTheLoaiPhim;
        this.tenPhim = tenPhim;
        this.thoiLuong = thoiLuong;
        this.daoDien = daoDien;
        this.namSanXuat = namSanXuat;
        this.gioiHanTuoi = gioiHanTuoi;
        this.posterURL = posterURL;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public int getMaTheLoaiPhim() {
        return maTheLoaiPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public String getThoiLuong() {
        return thoiLuong;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public int getNamSanXuat() {
        return namSanXuat;
    }

    public int getGioiHanTuoi() {
        return gioiHanTuoi;
    }

    public String getPosterURL() {
        return posterURL;
    }
}

