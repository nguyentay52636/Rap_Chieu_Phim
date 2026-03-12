package org.example.DTO;

public class SanPhamDTO {
    private int maSanPham;
    private String tenSanPham;
    private String hinhAnh;
    private int giaBan;
    private String kichThuoc;
    private int soLuong;
    private String trangThai;

    // Constructor rỗng
    public SanPhamDTO() {
    }

    // Constructor đầy đủ tham số (Khớp 100% với file DAO của ông)
    public SanPhamDTO(int maSanPham, String tenSanPham, String hinhAnh, int giaBan, String kichThuoc, int soLuong, String trangThai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.giaBan = giaBan;
        this.kichThuoc = kichThuoc;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    // --- Getters và Setters ---
    public int getMaSanPham() { return maSanPham; }
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getGiaBan() { return giaBan; }
    public void setGiaBan(int giaBan) { this.giaBan = giaBan; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}