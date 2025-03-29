package com.example.studentscoremanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscoremanagement.Model.TaiKhoan;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnStudentLogin; // Thêm btnStudentLogin
    EditText txtUserName, txtPass;
    DBHelper database;
    String phoneNumber, codeVerify;

    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    public final int REQUEST_CODE_FOR_PERMISSIONS = 654;
    private static final String LOG_TAG = "StudentScoreMn";

    public static final String USER_INFOR = "USER_INFOR";
    public static final String VERIFY_CODE = "VERIFY_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        askPermissionAndSendSMS();
        addControl();
        setEvent();
    }

    private void setEvent() {
        database = new DBHelper(this);

        // Sự kiện cho nút đăng nhập admin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUserName.getText().toString();
                String pass = txtPass.getText().toString();
                TaiKhoan tk = new TaiKhoan(username, pass);
                if (tk.checkLogin(database)) {
                    phoneNumber = tk.getSdt();
                    codeVerify = randomVerifyCode();

                    // Gửi tin nhắn
                    sendSMS_by_smsManager();

                    Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                    intent.putExtra(DBHelper.COL_TAIKHOAN_TEN, tk.getTenTaiKhoan());
                    intent.putExtra(DBHelper.COL_TAIKHOAN_SDT, tk.getSdt());
                    intent.putExtra(VERIFY_CODE, codeVerify);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sự kiện cho nút đăng nhập sinh viên
        btnStudentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        txtUserName = findViewById(R.id.txtTK);
        txtPass = findViewById(R.id.txtMK);
        btnLogin = findViewById(R.id.btnLogin);
        btnStudentLogin = findViewById(R.id.btnStudentLogin); // Khởi tạo nút mới
    }

    private void askPermissionAndSendSMS() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_CODE_SEND_SMS
                );
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOR_PERMISSIONS);
            }
        }
    }

    private void sendSMS_by_smsManager() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    "Mã xác thực: " + codeVerify,
                    null,
                    null);

            Log.i(LOG_TAG, "Mã xác thực đã được gửi đến SĐT của bạn!");
            Toast.makeText(getApplicationContext(), "Mã xác thực đã được gửi đến SĐT của bạn!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Gửi mã xác thực thất bại...", ex);
            Toast.makeText(getApplicationContext(), "Gửi mã xác thực thất bại... " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public String randomVerifyCode() {
        String code = "";
        Random rand = new Random();
        for (int i = 1; i <= 6; i++) {
            int tempIntCode = rand.nextInt(10);
            code += tempIntCode;
        }
        return code;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Permission granted!");
                    Toast.makeText(this, "Đã cấp quyền gửi SMS!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(LOG_TAG, "Đã từ chối quyền gửi SMS!");
                }
                break;
            }
        }
    }
}