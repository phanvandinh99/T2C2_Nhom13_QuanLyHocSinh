package com.example.studentscoremanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.Model.TaiKhoan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class UserInforActivity extends AppCompatActivity implements EventListener{
    TaiKhoan user;

    Button btnUpdateInfor;
    LinearLayout llLogout;
    ImageView imUserAvatar;
    TextView txtAccount;
    EditText txtPassword, txtPhoneNumber;

    private static final int PICK_IMAGE = 100;
    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);

        database = new DBHelper(this);

        setControl();
        setEvent();
        setDataByBundle();
    }

    private void setDataByBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(LoginActivity.USER_INFOR);
        if (bundle == null)
        {
            Toast.makeText(this, "bundle is null", Toast.LENGTH_SHORT).show();
            return;
        }
        user = new TaiKhoan(bundle.getString(DBHelper.COL_TAIKHOAN_TEN));
        user.updateDataFromDataBase(database);

        txtAccount.setText("", TextView.BufferType.EDITABLE);
        txtPassword.setText("", TextView.BufferType.EDITABLE);
        txtPhoneNumber.setText("", TextView.BufferType.EDITABLE);

        txtAccount.setText(user.getTenTaiKhoan(), TextView.BufferType.EDITABLE);
        txtPassword.setText(user.getMatKhau(), TextView.BufferType.EDITABLE);
        txtPhoneNumber.setText(user.getSdt(), TextView.BufferType.EDITABLE);

        byte[] avatar = user.getAnh();
        if (avatar == null)
        {
            //set default avatar
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            imUserAvatar.setImageBitmap(bitmap);
        }
    }

    private void setEvent() {
        imUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInforActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog cdd=new CustomDialog(UserInforActivity.this, UserInforActivity.this);
                cdd.show();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bm == null)
            {
                Toast.makeText(this, "image is null", Toast.LENGTH_SHORT).show();
                return;
            }

            //convert image to byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();

            //set image to user
            user.setAnh(buffer);

            //display the image into avatar
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAnh(), 0, user.getAnh().length);
            imUserAvatar.setImageBitmap(bitmap);
        }
    }

    private void setControl() {
        btnUpdateInfor = findViewById(R.id.btnUdateInfor);
        llLogout = findViewById(R.id.llLogout);
        imUserAvatar = findViewById(R.id.imUserAvatarInUserInfor);
        txtAccount = findViewById(R.id.txtAccount);
        txtPassword = findViewById(R.id.txtPassword);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
    }

    @Override
    public void doSomething() {
        user.setMatKhau(txtPassword.getText().toString());
        user.setSdt(txtPhoneNumber.getText().toString());

        //save data
        user.saveToDatabase(database);

        //update successfully
        Toast.makeText(this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
    }
}