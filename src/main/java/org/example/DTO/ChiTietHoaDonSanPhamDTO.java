package org.example.DTO;

public class ChiTietHoaDonSanPhamDTO {
    private int maHoaDon;
    private int maSanPham;
    private int soLuong;
    private int donGia;
    private int thanhTien;

    public ChiTietHoaDonSanPhamDTO() {
    }

    public ChiTietHoaDonSanPhamDTO(int maHoaDon, int maSanPham, int soLuong, int donGia, int thanhTien) {
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaSanPham() { return maSanPham; }
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public int getDonGia() { return donGia; }
    public void setDonGia(int donGia) { this.donGia = donGia; }

    public int getThanhTien() { return thanhTien; }
    public void setThanhTien(int thanhTien) { this.thanhTien = thanhTien; }
}