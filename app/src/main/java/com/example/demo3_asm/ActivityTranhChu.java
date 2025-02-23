package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTranhChu extends AppCompatActivity {
    Button btnQuanLyLop,btnQuanLySv,btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranh_chu);
        btnQuanLyLop = findViewById(R.id.btnquanlylophoc);
        btnQuanLySv = findViewById(R.id.btnquanlysinhvien);
        btnThoat = findViewById(R.id.btnthoat);

        btnQuanLyLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTranhChu.this,ActivityLopHoc.class);
                startActivity(intent);
            }
        });
        btnQuanLySv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTranhChu.this,ActivityQuanLySinhVien.class);
                startActivity(intent);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
