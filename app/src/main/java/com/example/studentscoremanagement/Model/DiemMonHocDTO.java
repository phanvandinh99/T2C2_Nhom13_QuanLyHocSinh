package com.example.studentscoremanagement.Model;

public class DiemMonHocDTO {
    private String maMH;
    private String tenMH;
    private int heSo;
    private float diem;

    public DiemMonHocDTO() {
    }

    public DiemMonHocDTO(String maMH, String tenMH, int heSo, float diem) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.heSo = heSo;
        this.diem = diem;
    }

    public String getMaMH() {
        return maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public int getHeSo() {
        return heSo;
    }

    public float getDiem() {
        return diem;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public void setHeSo(int heSo) {
        this.heSo = heSo;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }
}
