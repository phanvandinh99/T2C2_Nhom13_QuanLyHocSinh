package com.example.studentscoremanagement.Model;

import android.database.Cursor;

import com.example.studentscoremanagement.DBHelper;

import java.util.ArrayList;

public class HocSinh {
    private String maHS;
    private String ho;
    private String ten;
    private String phai;
    private String ngaySinh;
    private String maLop;

    public HocSinh() {
    }

    public HocSinh(String maHS, String ho, String ten, String phai, String ngaySinh, String maLop) {
        this.maHS = maHS;
        this.ho = ho;
        this.ten = ten;
        this.phai = phai;
        this.ngaySinh = ngaySinh;
        this.maLop = maLop;
    }

    public HocSinh(String maHS, String ho, String ten, String phai, String ngaySinh) {
        this.maHS = maHS;
        this.ho = ho;
        this.ten = ten;
        this.phai = phai;
        this.ngaySinh = ngaySinh;
    }

    public String getMaHS() {
        return maHS;
    }

    public String getHo() {
        return ho;
    }

    public String getTen() {
        return ten;
    }

    public String getPhai() {
        return phai;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setPhai(String phai) {
        this.phai = phai;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public DiemHocSinhDTO getStudentScore(DBHelper db){
        DiemHocSinhDTO diemHocSinhDTO=new DiemHocSinhDTO(HocSinh.this);
        ArrayList<DiemMonHocDTO> diemMonHocDTOS=new ArrayList<>();
//        Cursor cursor=db.GetData("SELECT "+DBHelper.COL_DIEM_MAMONHOC+", "+DBHelper.COL_MONHOC_TENMONHOC+", "
//                +DBHelper.COL_MONHOC_HESO+", "+DBHelper.COL_DIEM_DIEM
//                +" FROM "+DBHelper.TB_MONHOC+", "+DBHelper.TB_DIEM+
//                " WHERE "+DBHelper.COL_DIEM_MAHOCSINH+" ="+this.maHS+" AND "+DBHelper.COL_DIEM_MAMONHOC+"="+DBHelper.COL_MONHOC_MAMONHOC);
        Cursor cursor=db.GetData("SELECT "+DBHelper.COL_DIEM_MAMONHOC+", "+DBHelper.COL_MONHOC_TENMONHOC+", "
                +DBHelper.COL_MONHOC_HESO+", "+DBHelper.COL_DIEM_DIEM
                +" FROM "+DBHelper.TB_MONHOC+" LEFT OUTER  JOIN "+DBHelper.TB_DIEM+" ON "+DBHelper.COL_MONHOC_MAMONHOC+"="+DBHelper.COL_DIEM_MAMONHOC+
                " AND "+DBHelper.COL_DIEM_MAHOCSINH+" ="+this.maHS, new String[]{String.valueOf(maHS)});
        cursor.moveToFirst();

        do {
            if(cursor.getCount()==0){
                break ;
            }
            DiemMonHocDTO diemMonHocDTO=new DiemMonHocDTO();
            diemMonHocDTO.setMaMH(cursor.getString(0));
            diemMonHocDTO.setTenMH(cursor.getString(1));
            diemMonHocDTO.setHeSo(cursor.getInt(2));
            if(cursor.getString(3)!=null ){
                diemMonHocDTO.setDiem(Float.parseFloat(cursor.getString(3)));
            }else {
                diemMonHocDTO.setDiem(-1);
            }


            diemMonHocDTOS.add(diemMonHocDTO);
        }while (cursor.moveToNext());

        diemHocSinhDTO.setDiemMonHocDTOS(diemMonHocDTOS);

        return diemHocSinhDTO;
    }
}
