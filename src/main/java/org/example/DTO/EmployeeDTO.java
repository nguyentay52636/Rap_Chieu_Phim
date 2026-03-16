package org.example.DTO;

import java.sql.Date;

public class EmployeeDTO {
    private int maNV;
    private String hoTen;
    private Date ngaySinh;
    private Date ngayVaoLam;
    private double luongCoBan;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int maNV, String hoTen, Date ngaySinh, Date ngayVaoLam, double luongCoBan) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
    }

    // --- GETTER & SETTER ---
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
}