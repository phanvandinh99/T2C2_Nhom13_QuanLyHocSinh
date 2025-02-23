package com.example.demo3_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {
    public String DATABASE_NAME = "qlsv.sqlite";
    SQLiteDatabase database;

    EditText edtUserName, edtPass;
    Button btnLogin;
    TextView txtDangKi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtDangKi = findViewById(R.id.txtdangki);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BatLoiForm() == true) {
                    String user = edtUserName.getText().toString();
                    String pass = edtPass.getText().toString();
                    boolean bl = duyetThongTinDangNhap(user, pass);
                    if (bl) {
                        Intent intent = new Intent(ActivityLogin.this, ActivityTranhChu.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ActivityLogin.this, "Thông tin đăng nhập sai!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivityDangKi.class);
                startActivity(intent);
            }
        });
    }

    public boolean duyetThongTinDangNhap(String namedb, String passdb) {
        database = Database.initDatabase(this, DATABASE_NAME);
        Log.d("a","duyetThongTinDangNhap: " + passdb);
        Cursor cursor = database.rawQuery("select * from tbTaiKhoan", null);
        if (cursor.moveToFirst()) {
            do {
                String namecu = cursor.getString(1);
                if (namedb.equals(namecu)) {
                    String passcu = cursor.getString(2);
                    if (passcu.equals(passdb)) {
                        return true;
                    } else return false;
                }
            } while (cursor.moveToNext());
        }
        return false;

    }

    public boolean BatLoiForm() {
        String user = edtUserName.getText().toString();
        String pass = edtPass.getText().toString();
        if (user.length() == 0 && pass.length() > 0) {
            Toast.makeText(this, "Bạn chưa nhập Username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (user.length() > 0 && pass.length() == 0) {
            Toast.makeText(this, "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (user.length() == 0 && pass.length() == 0) {
            Toast.makeText(this, "Bạn chưa nhập Username và Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
