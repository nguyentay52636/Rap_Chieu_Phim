package org.example.DTO;

public class GheDTO {

    private int maGhe;
    private int MaPhong;
    private int MaLoaiGhe;
    private int HangGhe;
    private int SoGhe;

    public GheDTO(int maGhe, int MaPhong, int MaLoaiGhe, int HangGhe, int SoGhe) {
        this.maGhe = maGhe;
        this.MaPhong = MaPhong;
        this.MaLoaiGhe = MaLoaiGhe;
        this.HangGhe = HangGhe;
        this.SoGhe = SoGhe;
    }

    public int getMaGhe() {
        return maGhe;
    }

    public int getMaPhong() {
        return MaPhong;
    }

    public int getMaLoaiGhe() {
        return MaLoaiGhe;
    }

    public int getHangGhe() {
        return HangGhe;
    }

    public int getSoGhe() {
        return SoGhe;
    }

}

