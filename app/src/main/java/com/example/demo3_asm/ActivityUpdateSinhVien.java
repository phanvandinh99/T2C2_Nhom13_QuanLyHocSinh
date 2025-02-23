package com.example.demo3_asm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityUpdateSinhVien extends AppCompatActivity {
    final String DATABASE_NAME = "qlsv.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    Button  btnLuuUpdateSv, btnQuayLaiUpdateSv,btnChupHinhUpdateSv, btnChonHinhUpdateSv;
    EditText edtMaUpdateSv, edtTenUpdateSv, edtSdtUpdateSv, edtEmailUpdateSv;
    ImageView imgHinhDaiDienUpdateSv;
    Spinner spnMaLopUpdateSv;
    //Activity context;
    String MaSv;

    public static final int ccc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sinh_vien);

        addControls();
        addEvents();
        initUI();


    }

    private void initUI() {
        Intent intent = getIntent();
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        //String MaSv = String.valueOf(intent.getIntExtra("masv", -1));
        MaSv = getIntent().getExtras().getString("masv");
        Cursor cursor = database.rawQuery("select * from tbSinhVien where colMaSV=?", new String[]{MaSv});
        //Cursor cursor = database.rawQuery("select * from tbSinhVien",null);
        cursor.moveToFirst();
        //Toast.makeText(this, cursor.getString(1), Toast.LENGTH_SHORT).show();
        String masv = cursor.getString(0);
        String tensv = cursor.getString(1);
        String malopsv = cursor.getString(2);
        String sdtsv = cursor.getString(3);
        String emailsv = cursor.getString(4);
        byte[] hinhdaidiensv = cursor.getBlob(5);

        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhdaidiensv, 0, hinhdaidiensv.length);
        imgHinhDaiDienUpdateSv.setImageBitmap(bitmap);

        edtMaUpdateSv.setText(masv);
        edtTenUpdateSv.setText(tensv);
        //edtMaLopSv.setText(malopsv);
        //spnMaLop.setAdapter();
        edtSdtUpdateSv.setText(sdtsv);
        edtEmailUpdateSv.setText(emailsv);


    }

    private void addControls() {
        btnChonHinhUpdateSv = findViewById(R.id.btnchonhinhupdatesv);
        btnChupHinhUpdateSv = findViewById(R.id.btnchuphinhupdatesv);
        btnLuuUpdateSv = findViewById(R.id.btnluusvupdate);
        btnQuayLaiUpdateSv = findViewById(R.id.btnquaylaisvupdate);
        edtMaUpdateSv = findViewById(R.id.edtmasvupdate);
        edtTenUpdateSv = findViewById(R.id.edttensvupdate);
//        edtMaLopSv = findViewById(R.id.edtmalopsvadd);
        spnMaLopUpdateSv = findViewById(R.id.spnmalopupdatesv);
        edtSdtUpdateSv = findViewById(R.id.edtsdtsvupdate);
        edtEmailUpdateSv = findViewById(R.id.edtemailsvupdate);
        imgHinhDaiDienUpdateSv = findViewById(R.id.imghinhupdatesv);
        spnMaLopUpdateSv = findViewById(R.id.spnmalopupdatesv);

        ganMaLopVaoSpiner();

    }

    public void addEvents() {
        btnChupHinhUpdateSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImg();
            }
        });
        btnChonHinhUpdateSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });
        btnLuuUpdateSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (batLoiFormUpdateSv() == true) {
                    updateSinhVien();
                    finish();
                }
            }
        });
        btnQuayLaiUpdateSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void takeImg() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void chooseImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinhDaiDienUpdateSv.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinhDaiDienUpdateSv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateSinhVien() {
        try {
            String ms = edtMaUpdateSv.getText().toString();
            String TenSv = edtTenUpdateSv.getText().toString();
//        String MaLopSv = edtMaLopSv.getText().toString();
            String MaLopSv = spnMaLopUpdateSv.getSelectedItem().toString();
            String SdtSv = edtSdtUpdateSv.getText().toString();
            String EmailSv = edtEmailUpdateSv.getText().toString();
            byte[] Anh = getByteArrayFromImageView(imgHinhDaiDienUpdateSv);
//        SQLiteDatabase db = Database.initDatabase(this,DATABASE_NAME);
//        SQLiteStatement statement = db.rawQuery("in")

            ContentValues contentValues = new ContentValues();
//        contentValues.put("colMaSV",ms);
            contentValues.put("colTenSv", TenSv);
            contentValues.put("colMaLopSv", MaLopSv);
            contentValues.put("colSdtSv", SdtSv);
            contentValues.put("colEmailSv", EmailSv);
            contentValues.put("colHinhSv", Anh);

            SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
            int kq = database.update("tbSinhVien", contentValues, "colMaSV=?", new String[]{ms});
            if (kq == 0) {
                Toast.makeText(ActivityUpdateSinhVien.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityUpdateSinhVien.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }

//        Cursor cursor = database.rawQuery("select * from tbSinhVien",null);
//        if (cursor.moveToFirst()) {
//            do {
//                String mscu = cursor.getString(0);
//                if (mscu.equals(ms)) {
//                    int kq = database.update("tbSinhVien",contentValues,"colMaSV=?",new String[]{ms});
//                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Cập nhật thất bại - Không thể chỉnh sửa Mã SV", Toast.LENGTH_SHORT).show();
//                }
//            } while (cursor.moveToNext());
//        }
//
            Intent intent = new Intent(this, ActivityQuanLySinhVien.class);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    //Hàm đổi data 1 ImageView sang dữ liệu Byte[]
    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void ganMaLopVaoSpiner() {
        ArrayList<String> arrmalop;
        arrmalop = layMaBangLop();
        ArrayAdapter<String> adapMalop = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrmalop);
        adapMalop.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// không có dòng này thì hiển thị không đẹp
        spnMaLopUpdateSv.setAdapter(adapMalop);
    }

    public boolean batLoiFormUpdateSv() {
        String masv, tensv, emailsv, sdtsv;
        masv = edtMaUpdateSv.getText().toString();
        tensv = edtTenUpdateSv.getText().toString();
        emailsv = edtEmailUpdateSv.getText().toString();
        sdtsv = edtSdtUpdateSv.getText().toString();
        if (masv.length() == 0 || tensv.length() == 0) {
            Toast.makeText(this, "Không được để trống Mã hoặc Tên Sinh Viên", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sdtsv.trim().length() > 0 && !Patterns.PHONE.matcher(sdtsv).matches()) {
            Toast.makeText(this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailsv.trim().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(emailsv).matches()) {
            Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;


    }

    private ArrayList<String> layMaBangLop() {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("select * from tbLopHoc", null);
        while (cursor.moveToNext()) {
            String malop = cursor.getString(0);
            arr.add(malop);
        }
        cursor.close();
        return arr;
    }




}
