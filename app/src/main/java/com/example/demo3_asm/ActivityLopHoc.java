package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ActivityLopHoc extends AppCompatActivity {
    public String DATABASE_NAME = "qlsv.sqlite";
    SQLiteDatabase databaseLopHoc;

    EditText edtMaLop, edtTenLop;
    Button btnAddLopHoc, btnEditLop, btnXemDsSv;
    ImageView ivBackLop;

    ListView lvLopHoc;
    ArrayList<ModalLopHoc> listLopHoc;
    AdapterLopHoc adapterLopHoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop_hoc);

        addControls();
        addEnvents();
        readDataLopHoc();

    }

    private void addEnvents() {
        lvLopHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtMaLop.setText(listLopHoc.get(i).getMaLopHoc());
                edtTenLop.setText(listLopHoc.get(i).getTenLopHoc());
            }
        });
        btnAddLopHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (batLoiFormLopHoc() == true) {
                    addLopHoc();
                }

            }
        });
        btnEditLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CapNhatLop();
            }
        });
        btnXemDsSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLopHoc.this, ActivityQuanLySinhVien.class);
                startActivity(intent);
            }
        });
        ivBackLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        lvLopHoc = findViewById(R.id.lvlophoc);
        listLopHoc = new ArrayList<>();
        adapterLopHoc = new AdapterLopHoc(this, listLopHoc);
        lvLopHoc.setAdapter(adapterLopHoc);

        edtMaLop = findViewById(R.id.edtmalop);
        edtTenLop = findViewById(R.id.edttenlop);
        btnAddLopHoc = findViewById(R.id.btnthemlop);
        btnEditLop = findViewById(R.id.btneditlop);
        btnXemDsSv = findViewById(R.id.btnfromqlsv);
        ivBackLop = findViewById(R.id.ivbacklophoc);

    }

    public void CapNhatLop() {
        ModalLopHoc lopHoc = new ModalLopHoc();

        String MaLopHoc = edtMaLop.getText().toString();
        String TenLopHoc = edtTenLop.getText().toString();

        ContentValues values = new ContentValues();

        values.put("colTenLop", TenLopHoc);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        int kq = database.update("tbLopHoc", values, "colMaLop=?", new String[]{MaLopHoc});
        if (kq == 0) {
            Toast.makeText(ActivityLopHoc.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityLopHoc.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
//        Cursor cs = database.rawQuery("select * from tbLopHoc",null);
//        if (cs.moveToFirst()){
//            do {
//                String mscu = cs.getString(0);
//                if (mscu.equals(MaLopHoc)) {
//                    int kq = database.update("tbLopHoc", values, "colMaLop=?", new String[]{MaLopHoc});
//                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Cập nhật thất bại - Không thể chỉnh sửa Mã SV", Toast.LENGTH_SHORT).show();
//                }
//            } while (cs.moveToNext());
//        }


        readDataLopHoc();

    }

    public void addLopHoc() {
        String malophoc = edtMaLop.getText().toString();
        String tenlophoc = edtTenLop.getText().toString();

        ContentValues values = new ContentValues();

        values.put("colMaLop", malophoc);
        values.put("colTenLop", tenlophoc);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from tbLopHoc ", null);
        if (cursor.moveToFirst()) {
            do {
                String macu = cursor.getString(0);
                if (malophoc.equals(macu)) {
                    Toast.makeText(this, "Trùng Mã Lớp", Toast.LENGTH_SHORT).show();
                } else {
                    int kq = (int) database.insert("tbLopHoc", null, values);
                    if (kq ==0){
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }

                }
            } while (cursor.moveToNext());

        }
        readDataLopHoc();


    }

    private boolean batLoiFormLopHoc() {
        String ma = edtMaLop.getText().toString();
        String ten = edtTenLop.getText().toString();
        if (ma.length() == 0 && ten.length() > 0) {
            Toast.makeText(this, "Bạn chưa nhập Mã Lớp", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ma.length() > 0 && ten.length() == 0) {
            Toast.makeText(this, "Bạn chưa nhập Tên Lớp", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ma.length() == 0 && ten.length() == 0) {
            Toast.makeText(this, "Mã Lớp và Tên Lớp không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private void readDataLopHoc() {
        databaseLopHoc = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = databaseLopHoc.rawQuery("select * from tbLopHoc", null);
//        cursor.moveToPosition(3);
//        Toast.makeText(this, cursor.getString(1), Toast.LENGTH_SHORT).show();
        listLopHoc.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String MaLop = cursor.getString(0);
            String TenLop = cursor.getString(1);

            listLopHoc.add(new ModalLopHoc(MaLop, TenLop));
        }
        cursor.close();

        adapterLopHoc.notifyDataSetChanged();
    }


}
