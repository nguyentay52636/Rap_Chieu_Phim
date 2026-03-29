package org.example.DTO;

public class LoaiGheDTO {
    private int maLoaiGhe;
    private String tenLoaiGhe;
    private int giaPhuThu;

    public LoaiGheDTO() {}

    public LoaiGheDTO(int maLoaiGhe, String tenLoaiGhe, int giaPhuThu) {
        this.maLoaiGhe = maLoaiGhe;
        this.tenLoaiGhe = tenLoaiGhe;
        this.giaPhuThu = giaPhuThu;
    }

    public int getMaLoaiGhe() { return maLoaiGhe; }
    public void setMaLoaiGhe(int maLoaiGhe) { this.maLoaiGhe = maLoaiGhe; }

    public String getTenLoaiGhe() { return tenLoaiGhe; }
    public void setTenLoaiGhe(String tenLoaiGhe) { this.tenLoaiGhe = tenLoaiGhe; }

    public int getGiaPhuThu() { return giaPhuThu; }
    public void setGiaPhuThu(int giaPhuThu) { this.giaPhuThu = giaPhuThu; }
}