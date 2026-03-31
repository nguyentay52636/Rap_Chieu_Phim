package org.example.DTO;

import java.sql.Timestamp;

public class HoaDonDTO {

    private int maHoaDon;
    private int maKH;
    private int maNV;
    private String tenKhachHang;
    private String tenNhanVien;
    private Timestamp ngayLapHoaDon;
    private int tongTienVe;
    private int tongTienSanPham;
    private int tongThanhToan;

    public HoaDonDTO() {
    }

    public HoaDonDTO(int maHoaDon, int maKH, int maNV, Timestamp ngayLapHoaDon,
                     int tongTienVe, int tongTienSanPham, int tongThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.tongTienVe = tongTienVe;
        this.tongTienSanPham = tongTienSanPham;
        this.tongThanhToan = tongThanhToan;
    }

    public HoaDonDTO(int maHoaDon, int maKH, int maNV, String tenKhachHang, String tenNhanVien,
                     Timestamp ngayLapHoaDon, int tongTienVe, int tongTienSanPham, int tongThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maKH = maKH;
        this.maNV = maNV;
        this.tenKhachHang = tenKhachHang;
        this.tenNhanVien = tenNhanVien;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.tongTienVe = tongTienVe;
        this.tongTienSanPham = tongTienSanPham;
        this.tongThanhToan = tongThanhToan;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public Timestamp getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(Timestamp ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public int getTongTienVe() {
        return tongTienVe;
    }

    public void setTongTienVe(int tongTienVe) {
        this.tongTienVe = tongTienVe;
    }

    public int getTongTienSanPham() {
        return tongTienSanPham;
    }

    public void setTongTienSanPham(int tongTienSanPham) {
        this.tongTienSanPham = tongTienSanPham;
    }

    public int getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(int tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    @Override
    public String toString() {
        return "HoaDonDTO{" +
                "maHoaDon=" + maHoaDon +
                ", maKH=" + maKH +
                ", maNV=" + maNV +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", ngayLapHoaDon=" + ngayLapHoaDon +
                ", tongTienVe=" + tongTienVe +
                ", tongTienSanPham=" + tongTienSanPham +
                ", tongThanhToan=" + tongThanhToan +
                '}';
    }
}
