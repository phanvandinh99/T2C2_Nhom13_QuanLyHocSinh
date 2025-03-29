package com.example.studentscoremanagement;

public class Score {
    private String tenMonHoc;
    private float diem;

    public Score(String tenMonHoc, float diem) {
        this.tenMonHoc = tenMonHoc;
        this.diem = diem;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public float getDiem() {
        return diem;
    }
}