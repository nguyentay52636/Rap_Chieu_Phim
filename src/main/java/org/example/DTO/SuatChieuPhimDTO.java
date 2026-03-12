package org.example.DTO;

import java.sql.Date;

public class SuatChieuPhimDTO {

    private int maSuatChieuPhim;
    private int maPhim;
    private int maPhong;
    private int maSuatChieu;
    private Date ngayChieu;
    private int giaVeGoc;

    public SuatChieuPhimDTO(int maSuatChieuPhim, int maPhim, int maPhong,
                            int maSuatChieu, Date ngayChieu, int giaVeGoc) {
        this.maSuatChieuPhim = maSuatChieuPhim;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
        this.maSuatChieu = maSuatChieu;
        this.ngayChieu = ngayChieu;
        this.giaVeGoc = giaVeGoc;
    }

    public int getMaSuatChieuPhim() {
        return maSuatChieuPhim;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public int getMaSuatChieu() {
        return maSuatChieu;
    }

    public Date getNgayChieu() {
        return ngayChieu;
    }

    public int getGiaVeGoc() {
        return giaVeGoc;
    }
}

