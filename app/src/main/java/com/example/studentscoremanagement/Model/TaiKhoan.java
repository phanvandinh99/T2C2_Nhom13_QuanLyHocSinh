package com.example.studentscoremanagement.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.studentscoremanagement.DBHelper;

public class TaiKhoan {
    private String tenTaiKhoan;
    private String matKhau;
    private String sdt;
    private byte[] anh;

    public TaiKhoan() {
    }

    public TaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau, String sdt) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.sdt=sdt;
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau, String sdt, byte[] anh) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.sdt = sdt;
        this.anh = anh;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getSdt() {
        return sdt;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public boolean checkLogin(DBHelper db){
        Cursor getInfor=db.GetData("SELECT "+DBHelper.COL_TAIKHOAN_SDT+", "+DBHelper.COL_TAIKHOAN_ANH+" FROM "+DBHelper.TB_TAIKHOAN
                +" WHERE "+DBHelper.COL_TAIKHOAN_TEN+"='"+this.tenTaiKhoan+"' AND "+DBHelper.COL_TAIKHOAN_MATKHAU+"='"+this.matKhau+"'");
        if(getInfor.moveToNext()){
            this.setSdt(getInfor.getString(0));
            this.setAnh(getInfor.getBlob(1));
            return true;
        }
        return false;
    }

    public void updateDataFromDataBase (DBHelper db) {
        Cursor getInfor=db.GetData("SELECT "+DBHelper.COL_TAIKHOAN_MATKHAU+", "+DBHelper.COL_TAIKHOAN_SDT+", "+DBHelper.COL_TAIKHOAN_ANH+" FROM "+DBHelper.TB_TAIKHOAN
                +" WHERE "+DBHelper.COL_TAIKHOAN_TEN+"='"+this.tenTaiKhoan+"'");

        if(getInfor.moveToNext()){
            setMatKhau(getInfor.getString(0));
            setSdt(getInfor.getString(1));
            setAnh(getInfor.getBlob(2));
        }
    }

    public void saveToDatabase (DBHelper db){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_TAIKHOAN_MATKHAU, matKhau);
        cv.put(DBHelper.COL_TAIKHOAN_SDT, sdt);
        cv.put(DBHelper.COL_TAIKHOAN_ANH, anh);
        database.update(DBHelper.TB_TAIKHOAN, cv, DBHelper.COL_TAIKHOAN_TEN+" = '"+tenTaiKhoan+"'", null);
    }
}
