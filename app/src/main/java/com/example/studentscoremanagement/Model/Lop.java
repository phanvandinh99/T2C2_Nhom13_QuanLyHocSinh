package com.example.studentscoremanagement.Model;

import android.database.Cursor;

import com.example.studentscoremanagement.DBHelper;

import java.util.ArrayList;

public class Lop {
    private String maLop;
    private String chuNhiem;

    public Lop() {
    }

    public Lop(String maLop, String chuNhiem) {
        this.maLop = maLop;
        this.chuNhiem = chuNhiem;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getChuNhiem() {
        return chuNhiem;
    }

    public void setChuNhiem(String chuNhiem) {
        this.chuNhiem = chuNhiem;
    }

    public static ArrayList<String> getAllClassId (DBHelper database)
    {
        ArrayList<String> data = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT " + DBHelper.COL_LOP_MALOP + " FROM " + DBHelper.TB_LOP, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        cursor.moveToFirst();

        do {
            data.add(cursor.getString(0));
        }while (cursor.moveToNext());

        return data;
    }
}
