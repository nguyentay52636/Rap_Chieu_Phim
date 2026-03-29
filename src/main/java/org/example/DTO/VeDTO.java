package org.example.DTO;

public class VeDTO {
    // ========================================================
    // PHẦN 1: CÁC BIẾN GỐC (Đã giữ nguyên maSuatChieuPhim)
    // ========================================================
    private int maVe;
    private int maGhe;
    private int maSuatChieuPhim;
    private int giaVe;
    private String trangThai;

    // ========================================================
    // PHẦN 2: CÁC BIẾN MỞ RỘNG (Dùng để hiển thị lên JTable)
    // ========================================================
    private String khachHang;
    private String tenPhim;
    private String tenPhong;
    private String tenGhe;
    private String ngayChieu;
    private String gioBatDau;

    // ========================================================
    // CONSTRUCTORS (Hàm tạo)
    // ========================================================

    public VeDTO() {
    }

    public VeDTO(int maVe, int maGhe, int maSuatChieuPhim, int giaVe, String trangThai) {
        this.maVe = maVe;
        this.maGhe = maGhe;
        this.maSuatChieuPhim = maSuatChieuPhim;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
    }

    // ========================================================
    // GETTER & SETTER CHO TOÀN BỘ CÁC BIẾN
    // ========================================================

    // --- Của phần 1 (Biến gốc) ---
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }

    public int getMaGhe() { return maGhe; }
    public void setMaGhe(int maGhe) { this.maGhe = maGhe; }

    public int getMaSuatChieuPhim() { return maSuatChieuPhim; }
    public void setMaSuatChieuPhim(int maSuatChieuPhim) { this.maSuatChieuPhim = maSuatChieuPhim; }

    public int getGiaVe() { return giaVe; }
    public void setGiaVe(int giaVe) { this.giaVe = giaVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- Của phần 2 (Biến mở rộng JTable) ---
    public String getKhachHang() { return khachHang; }
    public void setKhachHang(String khachHang) { this.khachHang = khachHang; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getTenGhe() { return tenGhe; }
    public void setTenGhe(String tenGhe) { this.tenGhe = tenGhe; }

    public String getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(String ngayChieu) { this.ngayChieu = ngayChieu; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }
}