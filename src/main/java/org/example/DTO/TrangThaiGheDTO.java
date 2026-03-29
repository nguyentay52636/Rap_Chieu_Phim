package org.example.DTO;

public class TrangThaiGheDTO 
{
    private String hangGhe;
    private int soGhe;
    private String trangThai;

    public TrangThaiGheDTO(String hangGhe, int soGhe, String trangThai) {
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
    }

    public String getHangGhe() { return hangGhe; }
    public int getSoGhe() { return soGhe; }
    public String getTrangThai() { return trangThai; }
}