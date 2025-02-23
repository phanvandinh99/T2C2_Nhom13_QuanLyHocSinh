package com.example.demo3_asm;

public class ModalSinhVien {
    public String MaSv;
    public String TenSv;
    public String MaLopSv;
    public String SdtSv;
    public String EmailSv;
    public byte[] HinhSv;
    public  ModalSinhVien(){

    }

    public ModalSinhVien(String maSv, String tenSv, String maLopSv, String sdtSv, String emailSv, byte[] hinhSv) {
        MaSv = maSv;
        TenSv = tenSv;
        MaLopSv = maLopSv;
        SdtSv = sdtSv;
        EmailSv = emailSv;
        HinhSv = hinhSv;
    }

    public String getMaSv() {
        return MaSv;
    }

    public void setMaSv(String maSv) {
        MaSv = maSv;
    }

    public String getTenSv() {
        return TenSv;
    }

    public void setTenSv(String tenSv) {
        TenSv = tenSv;
    }

    public String getMaLopSv() {
        return MaLopSv;
    }

    public void setMaLopSv(String maLopSv) {
        MaLopSv = maLopSv;
    }

    public String getSdtSv() {
        return SdtSv;
    }

    public void setSdtSv(String sdtSv) {
        SdtSv = sdtSv;
    }

    public String getEmailSv() {
        return EmailSv;
    }

    public void setEmailSv(String emailSv) {
        EmailSv = emailSv;
    }

    public byte[] getHinhSv() {
        return HinhSv;
    }

    public void setHinhSv(byte[] hinhSv) {
        HinhSv = hinhSv;
    }
}
