package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDangKi extends AppCompatActivity {
    public String DATABASE_NAME = "qlsv.sqlite";
    EditText edtUserFormDangKi, edtPassFormDangki, edtRePassFormDangKi;
    Button btnFormDangKi;
    TextView txtDsTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        edtUserFormDangKi = findViewById(R.id.edtuserformdangki);
        edtPassFormDangki = findViewById(R.id.edtpassformdangki);
        edtRePassFormDangKi = findViewById(R.id.edtrepassformdangki);
        btnFormDangKi = findViewById(R.id.btnformdangki);
        txtDsTK = findViewById(R.id.txthienthidsdangki);

        btnFormDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUserFormDangKi.getText().toString();
                String pass = edtPassFormDangki.getText().toString();
                String repass = edtRePassFormDangKi.getText().toString();


                SQLiteDatabase db = Database.initDatabase(ActivityDangKi.this, DATABASE_NAME);
                ContentValues vl = new ContentValues();
                vl.put("colUser", user);
                vl.put("colPass", pass);
                if (errorFormDangKi()==true){
                    if (duyetThongTinDangKi(edtUserFormDangKi.getText().toString())==true){
                        if (pass.equals(repass)) {
                            int kq = (int) db.insert("tbTaiKhoan", null, vl);
                            if (kq == 0) {
                                Toast.makeText(ActivityDangKi.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                            } else {
                                finish();
                                Toast.makeText(ActivityDangKi.this, "Đăng kí Thành công", Toast.LENGTH_SHORT).show();
                            }
                            edtPassFormDangki.setText("");
                            edtRePassFormDangKi.setText("");
                            edtUserFormDangKi.setText("");
                            finish();
                        } else {
                            Toast.makeText(ActivityDangKi.this, "Password và Re Password không trùng nhau", Toast.LENGTH_SHORT).show();
                            edtRePassFormDangKi.setText("");
                        }
                    }

                }


            }
        });
        txtDsTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDangKi.this, ActivytiDsTaiKhoan.class);
                startActivity(intent);
            }
        });
    }
    public boolean errorFormDangKi(){
        String user = edtUserFormDangKi.getText().toString();
        String pass = edtPassFormDangki.getText().toString();
        String repass = edtRePassFormDangKi.getText().toString();
        if ( user.length()==0 || pass.length()==0 || repass.length()==0){
            Toast.makeText(this, "Không đươc để trống", Toast.LENGTH_SHORT).show();
            return false;
        }else

        return true;
    }
    public boolean duyetThongTinDangKi(String tendk){
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from tbTaiKhoan", null);
//        cursor.moveToFirst();
//        Toast.makeText(ActivityLogin.this, cursor.getString(1), Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) {
            do {
                String usercu = cursor.getString(1);
                if (tendk.equals(usercu)) {
                    Toast.makeText(this, " Username đã được đăng kí", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } while (cursor.moveToNext());
        }
        return true;
    }
}
