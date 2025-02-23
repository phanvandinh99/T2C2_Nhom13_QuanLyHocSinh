package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class DemoSpriner extends AppCompatActivity {
    Spinner spnDemo;
    public String DATABASE_NAME = "qlsv.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_spriner);

        spnDemo = findViewById(R.id.spndemo);

        ArrayList<String> arrmalop;
        arrmalop = layMaBangLop();
        ArrayAdapter<String> adapMalop = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrmalop);
        adapMalop.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// không có dòng này thì hiển thị không đẹp
        spnDemo.setAdapter(adapMalop);

    }

    public ArrayList<String> layMaBangLop() {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = db.rawQuery("select * from tbLopHoc", null);
        while (cursor.moveToNext()) {
            String malop = cursor.getString(0);
            arr.add(malop);
        }
        cursor.close();
        return arr;
    }

}
