package org.example.DTO;

import java.sql.Date;

public class HoaDonDTO {

    private int maHoaDon;
    private int maKH;
    private int maNV;
    private Date ngayBan;
    private int soLuongVe;
    private int tongTienVe;
    private int tongTienSanPham;
    private Integer maKhuyenMai;
    private int tongTienGiam;
    private int tongThanhToan;

    public HoaDonDTO(int maHoaDon, int maKH, int maNV, Date ngayBan,
                     int soLuongVe, int tongTienVe, int tongTienSanPham,
                     Integer maKhuyenMai, int tongTienGiam, int tongThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayBan = ngayBan;
        this.soLuongVe = soLuongVe;
        this.tongTienVe = tongTienVe;
        this.tongTienSanPham = tongTienSanPham;
        this.maKhuyenMai = maKhuyenMai;
        this.tongTienGiam = tongTienGiam;
        this.tongThanhToan = tongThanhToan;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public int getMaKH() {
        return maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public Date getNgayBan() {
        return ngayBan;
    }

    public int getSoLuongVe() {
        return soLuongVe;
    }

    public int getTongTienVe() {
        return tongTienVe;
    }

    public int getTongTienSanPham() {
        return tongTienSanPham;
    }

    public Integer getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public int getTongTienGiam() {
        return tongTienGiam;
    }

    public int getTongThanhToan() {
        return tongThanhToan;
    }
}

