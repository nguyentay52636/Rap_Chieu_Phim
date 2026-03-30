package org.example.DTO;

public class TheLoaiPhimDTO {
    private int maLoaiPhim;
    private String tenLoaiPhim;

    public TheLoaiPhimDTO(int maLoaiPhim, String tenLoaiPhim) {
        this.maLoaiPhim = maLoaiPhim;
        this.tenLoaiPhim = tenLoaiPhim;
    }

    public TheLoaiPhimDTO() {
    }

    public int getMaLoaiPhim() {
        return maLoaiPhim;
    }

    public void setMaLoaiPhim(int maLoaiPhim) {
        this.maLoaiPhim = maLoaiPhim;
    }

    public String getTenLoaiPhim() {
        return tenLoaiPhim;
    }

    public void setTenLoaiPhim(String tenLoaiPhim) {
        this.tenLoaiPhim = tenLoaiPhim;
    }
}
