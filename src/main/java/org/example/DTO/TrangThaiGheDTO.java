package org.example.DTO;

public class TrangThaiGheDTO {
    private int maGhe; // MỚI THÊM: Bắt buộc phải có để lưu vé
    private String hangGhe;
    private int soGhe;
    private String trangThai;

    public TrangThaiGheDTO(int maGhe, String hangGhe, int soGhe, String trangThai) {
        this.maGhe = maGhe;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
    }

    public int getMaGhe() { return maGhe; }
    public String getHangGhe() { return hangGhe; }
    public int getSoGhe() { return soGhe; }
    public String getTrangThai() { return trangThai; }
}