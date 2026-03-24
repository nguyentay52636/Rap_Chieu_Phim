package org.example.DTO;

import java.time.LocalDateTime;

public class SuatChieuPhimDTO {

    private int maSuatChieu;
    private int maPhim;
    private int maPhong;
    private LocalDateTime gioBatDau;
    private LocalDateTime gioKetThuc;
    private double giaVeGoc;

    // Constructor mặc định (Không tham số)
    public SuatChieuPhimDTO() {
    }

    // Constructor đầy đủ tham số
    public SuatChieuPhimDTO(int maSuatChieu, int maPhim, int maPhong,
                            LocalDateTime gioBatDau, LocalDateTime gioKetThuc, double giaVeGoc) {
        this.maSuatChieu = maSuatChieu;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.giaVeGoc = giaVeGoc;
    }

    // --- GETTERS ---
    public int getMaSuatChieu() {
        return maSuatChieu;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public LocalDateTime getGioBatDau() {
        return gioBatDau;
    }

    public LocalDateTime getGioKetThuc() {
        return gioKetThuc;
    }

    public double getGiaVeGoc() {
        return giaVeGoc;
    }

    // --- SETTERS ---
    public void setMaSuatChieu(int maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }

    public void setGioBatDau(LocalDateTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public void setGioKetThuc(LocalDateTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public void setGiaVeGoc(double giaVeGoc) {
        this.giaVeGoc = giaVeGoc;
    }
}