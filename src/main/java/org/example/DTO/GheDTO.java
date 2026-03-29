package org.example.DTO;

public class GheDTO {
    private int maGhe;
    private int maPhong;
    private int maLoaiGhe;
    private String hangGhe;
    private int soGhe;

    public GheDTO() {}

    public GheDTO(int maGhe, int maPhong, int maLoaiGhe, String hangGhe, int soGhe) {
        this.maGhe = maGhe;
        this.maPhong = maPhong;
        this.maLoaiGhe = maLoaiGhe;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
    }

    public int getMaGhe() { return maGhe; }
    public void setMaGhe(int maGhe) { this.maGhe = maGhe; }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public int getMaLoaiGhe() { return maLoaiGhe; }
    public void setMaLoaiGhe(int maLoaiGhe) { this.maLoaiGhe = maLoaiGhe; }

    public String getHangGhe() { return hangGhe; }
    public void setHangGhe(String hangGhe) { this.hangGhe = hangGhe; }

    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }
}