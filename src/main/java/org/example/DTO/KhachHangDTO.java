package org.example.DTO;

import java.time.LocalDate;

public class KhachHangDTO {
    private int maKH;
    private String hoTen;
    private String sDT;
    private LocalDate ngaySinh;
    private int diemTichLuy;
    private String hangThanhVien;

    public KhachHangDTO(){

    }

    public KhachHangDTO(String hoTen, String sDT, LocalDate ngaySinh, int diemTichLuy, String hangThanhVien) {
        this.hoTen = hoTen;
        this.sDT = sDT;
        this.ngaySinh = ngaySinh;
        this.diemTichLuy = diemTichLuy;
        this.hangThanhVien = hangThanhVien;
    }

    public KhachHangDTO(int maKH, String hoTen, String sDT, LocalDate ngaySinh, int diemTichLuy, String hangThanhVien) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sDT = sDT;
        this.ngaySinh = ngaySinh;
        this.diemTichLuy = diemTichLuy;
        this.hangThanhVien = hangThanhVien;
    }

    public KhachHangDTO(KhachHangDTO khachHangDTO){
        this.maKH = khachHangDTO.maKH;
        this.hoTen = khachHangDTO.hoTen;
        this.sDT = khachHangDTO.sDT;
        this.ngaySinh = khachHangDTO.ngaySinh;
        this.diemTichLuy = khachHangDTO.diemTichLuy;
        this.hangThanhVien = khachHangDTO.hangThanhVien;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSDT() {
        return sDT;
    }

    public void setSDT(String sDT) {
        this.sDT = sDT;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getHangThanhVien() {
        return hangThanhVien;
    }

    public void setHangThanhVien(String hangThanhVien) {
        this.hangThanhVien = hangThanhVien;
    }
}
