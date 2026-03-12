package org.example.DTO;

public class VeDTO {

    private int maVe;
    private int maGhe;
    private int maSuatChieuPhim;
    private int giaVe;
    private String trangThai;

    public VeDTO(int maVe, int maGhe, int maSuatChieuPhim, int giaVe, String trangThai) {
        this.maVe = maVe;
        this.maGhe = maGhe;
        this.maSuatChieuPhim = maSuatChieuPhim;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
    }

    public int getMaVe() {
        return maVe;
    }

    public int getMaGhe() {
        return maGhe;
    }

    public int getMaSuatChieuPhim() {
        return maSuatChieuPhim;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public String getTrangThai() {
        return trangThai;
    }
}

