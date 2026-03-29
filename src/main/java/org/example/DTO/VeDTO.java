package org.example.DTO;

public class VeDTO 
{
   
    private int maVe;
    private int maGhe;
    private int maSuatChieu;
    private int giaVe;
    private String trangThai; 

    // --- CÁC BIẾN MỞ RỘNG 
    private String tenPhim;       
    private String ngayChieu;     
    private String gioBatDau;     
    private String tenPhong;      
    private String tenGhe; // Ghép từ HangGhe và SoGhe (VD: A1, B2)

    
    public VeDTO() {}


    public VeDTO(int maVe, int maGhe, int maSuatChieu, int giaVe, String trangThai, 
                 String tenPhim, String ngayChieu, String gioBatDau, String tenPhong, String tenGhe) 
    {
        this.maVe = maVe;
        this.maGhe = maGhe;
        this.maSuatChieu = maSuatChieu;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
        
        this.tenPhim = tenPhim;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.tenPhong = tenPhong;
        this.tenGhe = tenGhe;
    }

    public int getMaVe() { return maVe; }
    public int getMaGhe() { return maGhe; }
    public int getMaSuatChieu() { return maSuatChieu; }
    public int getGiaVe() { return giaVe; }
    public String getTrangThai() { return trangThai; }
    public String getTenPhim() { return tenPhim; }
    public String getNgayChieu() { return ngayChieu; }
    public String getGioBatDau() { return gioBatDau; }
    public String getTenPhong() { return tenPhong; }
    public String getTenGhe() { return tenGhe; }

    public void setMave(int maVe) { this.maVe = maVe; }
    public void setMaGhe(int maGhe) { this.maGhe = maGhe; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }
    public void setGiaVe(int giaVe) { this.giaVe = giaVe; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public void setNgayChieu(String ngayChieu) { this.ngayChieu = ngayChieu; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public void setTenGhe(String tenGhe) { this.tenGhe = tenGhe; }
}