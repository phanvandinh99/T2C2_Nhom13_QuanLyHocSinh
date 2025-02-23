package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivytiDsTaiKhoan extends AppCompatActivity {
    public String DATABASE_NAME = "qlsv.sqlite";
    AdapterTaiKhoan adapterTaiKhoan;

    ListView lvDsTk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activyti_ds_tai_khoan);
        lvDsTk = findViewById(R.id.lvdstaikhoan);

        ArrayList<ModalUser> list = new ArrayList<>();
        adapterTaiKhoan = new AdapterTaiKhoan(this,list);
        lvDsTk.setAdapter(adapterTaiKhoan);

        SQLiteDatabase db = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = db.rawQuery("select * from tbTaiKhoan", null);
        list.clear();
        while (cursor.moveToNext()){
            String u = cursor.getString(1);
            String p = cursor.getString(2);
            list.add(new ModalUser(u,p));

        }cursor.close();
        adapterTaiKhoan.notifyDataSetChanged();


    }
}
