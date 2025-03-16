package com.example.studentscoremanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyActivity extends AppCompatActivity {

    TextView tvMess;
    EditText txtCode;
    Button btnComfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        setControl();
        setEvent();
    }

    private void setEvent() {
        Intent getDaTa=getIntent();
        String username=getDaTa.getExtras().getString(DBHelper.COL_TAIKHOAN_TEN);
        String sdt=getDaTa.getExtras().getString(DBHelper.COL_TAIKHOAN_SDT);
        String code=getDaTa.getExtras().getString(LoginActivity.VERIFY_CODE);
        tvMess.setText("Nhập mã xác minh đã được gửi tới SĐT của bạn "+sdt.substring(0,2)+"******"+sdt.substring(sdt.length()-2));
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputCode=txtCode.getText().toString();

                //kiểm tra người dùng nhập mã xác nhận có đúng ko
                if(inputCode.equals(code)){
                    finish();
                    Intent intent=new Intent(VerifyActivity.this,MainActivity.class);//update fragment here

                    intent.putExtra(DBHelper.COL_TAIKHOAN_TEN,username);
                    startActivity(intent);
                }else {
                    Toast.makeText(VerifyActivity.this, "Mã xác thực không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setControl() {
        tvMess=findViewById(R.id.tvSDT);
        txtCode=findViewById(R.id.txtMaXacThuc);
        btnComfirm=findViewById(R.id.btnConfirmVerify);
    }
}