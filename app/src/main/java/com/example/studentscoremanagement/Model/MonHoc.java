package com.example.studentscoremanagement.Model;

public class MonHoc {
    private String maMH;
    private String tenMH;
    private int heSo;

    public MonHoc() {
    }

    public MonHoc(String maMH, String tenMH, int heSo) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.heSo = heSo;
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

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public void setHeSo(int heSo) {
        this.heSo = heSo;
    }
}
