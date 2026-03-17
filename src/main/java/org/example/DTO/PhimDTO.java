package org.example.DTO;

import java.sql.Date;

public class PhimDTO {

    private int maPhim;
    private int maLoaiPhim;
    private String tenPhim;
    private int thoiLuong;
    private String daoDien;
    private int namSanXuat;
    private String anhMauPhim;
    private Date ngayKhoiChieu;
    private String trangThai;
    private int gioiHanTuoi;

    public PhimDTO() {
    }

    // Constructor gọn cho các form/UI đang dùng
    public PhimDTO(int maPhim, int maLoaiPhim, String tenPhim, int thoiLuong,
                   String daoDien, int namSanXuat, String posterURL) {
        this(maPhim, maLoaiPhim, tenPhim, thoiLuong, daoDien, namSanXuat, 0, posterURL);
    }

    // Constructor gọn đầy đủ (FormBooking/PhimBUS đang dùng)
    public PhimDTO(int maPhim, int maLoaiPhim, String tenPhim, int thoiLuong,
                   String daoDien, int namSanXuat, int gioiHanTuoi, String posterURL) {
        this.maPhim = maPhim;
        this.maLoaiPhim = maLoaiPhim;
        this.tenPhim = tenPhim;
        this.thoiLuong = thoiLuong;
        this.daoDien = daoDien;
        this.namSanXuat = namSanXuat;
        this.gioiHanTuoi = gioiHanTuoi;
        this.anhMauPhim = posterURL;
    }

    public PhimDTO(int maPhim, int maLoaiPhim, String tenPhim, int thoiLuong,
                   String daoDien, int namSanXuat, String anhMauPhim,
                   Date ngayKhoiChieu, String trangThai) {
        this.maPhim = maPhim;   
        this.maLoaiPhim = maLoaiPhim;
        this.tenPhim = tenPhim;
        this.thoiLuong = thoiLuong;
        this.daoDien = daoDien;
        this.namSanXuat = namSanXuat;
        this.anhMauPhim = anhMauPhim;
        this.ngayKhoiChieu = ngayKhoiChieu;
        this.trangThai = trangThai;
    }

    // Alias để khớp naming trong một số form/BUS cũ
    public int getMaTheLoaiPhim() {
        return maLoaiPhim;
    }

    public void setMaTheLoaiPhim(int maTheLoaiPhim) {
        this.maLoaiPhim = maTheLoaiPhim;
    }

    public String getPosterURL() {
        return anhMauPhim;
    }

    public void setPosterURL(String posterURL) {
        this.anhMauPhim = posterURL;
    }

    public int getGioiHanTuoi() {
        return gioiHanTuoi;
    }

    public void setGioiHanTuoi(int gioiHanTuoi) {
        this.gioiHanTuoi = gioiHanTuoi;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public int getMaLoaiPhim() {
        return maLoaiPhim;
    }

    public void setMaLoaiPhim(int maLoaiPhim) {
        this.maLoaiPhim = maLoaiPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public int getNamSanXuat() {
        return namSanXuat;
    }

    public void setNamSanXuat(int namSanXuat) {
        this.namSanXuat = namSanXuat;
    }

    public String getAnhMauPhim() {
        return anhMauPhim;
    }

    public void setAnhMauPhim(String anhMauPhim) {
        this.anhMauPhim = anhMauPhim;
    }

    public Date getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(Date ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}