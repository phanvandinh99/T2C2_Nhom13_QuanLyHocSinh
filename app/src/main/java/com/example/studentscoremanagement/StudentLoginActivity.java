package com.example.studentscoremanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentLoginActivity extends AppCompatActivity {
    private EditText txtStudentUsername, txtStudentPassword;
    private Button btnStudentLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        txtStudentUsername = findViewById(R.id.txtStudentUsername);
        txtStudentPassword = findViewById(R.id.txtStudentPassword);
        btnStudentLogin = findViewById(R.id.btnStudentLogin);
        dbHelper = new DBHelper(this);

        btnStudentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtStudentUsername.getText().toString().trim();
                String password = txtStudentPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(StudentLoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = dbHelper.GetData(
                        "SELECT * FROM " + DBHelper.TB_HOCSINH + " WHERE hocSinh_tenDangNhap = '" + username + "' AND hocSinh_matKhau = '" + password + "'",
                        null
                );

                if (cursor.moveToFirst()) {
                    // Get the actual student ID from the cursor - column 0 is the ID
                    int maHocSinh = cursor.getInt(0);  // The first column is hocSinh_maHocSinh
                    Toast.makeText(StudentLoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(StudentLoginActivity.this, StudentScoreActivity.class);
                    intent.putExtra("maHocSinh", maHocSinh); // Pass the actual student ID
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    Toast.makeText(StudentLoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            }
        });
    }
}