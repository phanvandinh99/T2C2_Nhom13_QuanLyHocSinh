package com.example.demo3_asm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityQuanLySinhVien extends AppCompatActivity {
    public String DATABASE_NAME = "qlsv.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    Button  btnLuuSvAdd, btnQuayLaiSvadd, btnNhapMoiSvAdd;
    EditText edtMaSvadd, edtTenSvadd, edtSdtSvadd, edtEmailSvadd;
    EditText edtSearchSv;
    ImageView imgHinhDaiDienadd, ivBackSinhVien,btnChupHinhAddSv, btnChonHinhAddSv;
    Spinner spnMaLopSvAdd;

    SQLiteDatabase database;
    ImageView btnAddSv, btnRefresh, btnSearch;
    public ListView lvSinhVien;
    public ArrayList<ModalSinhVien> list;
    public AdapterSinhVien adapterSinhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_sinh_vien);

        addControls();
        addEvents();
        readDataSinhVien();

    }

    private void addControls() {
        lvSinhVien = findViewById(R.id.lvsinhvien);
        btnAddSv = findViewById(R.id.btnaddsv);
        btnRefresh = findViewById(R.id.btnrefresh);
        btnSearch = findViewById(R.id.btnsearchsv);
        edtSearchSv = findViewById(R.id.edtsearchsv);
        ivBackSinhVien = findViewById(R.id.ivbacksinhvien);
        list = new ArrayList<>();
        adapterSinhVien = new AdapterSinhVien(this, list);
        lvSinhVien.setAdapter(adapterSinhVien);


    }

    private void addEvents() {
        btnAddSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataSinhVien();
                edtSearchSv.setText("");
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thuchientim();
            }
        });
        ivBackSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        edtSearchSv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                thuchientim();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogaddsv);

        btnChonHinhAddSv = dialog.findViewById(R.id.btnchonhinhaddsv);
        btnChupHinhAddSv = dialog.findViewById(R.id.btnchuphinhaddsv);
        btnLuuSvAdd = dialog.findViewById(R.id.btnluusvadd);
        btnQuayLaiSvadd = dialog.findViewById(R.id.btnquaylaisvadd);
        edtMaSvadd = dialog.findViewById(R.id.edtmasvadd);
        edtTenSvadd = dialog.findViewById(R.id.edttensvadd);
//        edtMaLopSvadd = dialog.findViewById(R.id.edtmalopsvadd);
        edtSdtSvadd = dialog.findViewById(R.id.edtsdtsvadd);
        edtEmailSvadd = dialog.findViewById(R.id.edtemailsvadd);
        btnNhapMoiSvAdd = dialog.findViewById(R.id.btnnhapmoisvadd);
        imgHinhDaiDienadd = dialog.findViewById(R.id.imghinhaddsv);
        spnMaLopSvAdd = dialog.findViewById(R.id.spnmalopaddsv);
        ganMaLopVaoSpiner();


        btnChupHinhAddSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImg();
            }
        });

        btnChonHinhAddSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });

        btnLuuSvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (batLoiFormAddSv() == true) {
                    addSinhVien();
                }
            }
        });


        btnNhapMoiSvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMaSvadd.setText("");
                edtTenSvadd.setText("");
                edtSdtSvadd.setText("");
                edtEmailSvadd.setText("");
                imgHinhDaiDienadd.setImageResource(R.drawable.logoong);

            }
        });
        btnQuayLaiSvadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                readDataSinhVien();
            }
        });

        dialog.show();
    }

    public boolean batLoiFormAddSv() {
        String masv, tensv, emailsv, sdtsv;
        masv = edtMaSvadd.getText().toString();
        tensv = edtTenSvadd.getText().toString();
        emailsv = edtEmailSvadd.getText().toString();
        sdtsv = edtSdtSvadd.getText().toString();
        if (masv.length() == 0 || tensv.length() == 0) {
            Toast.makeText(this, "Không được để trống Mã hoặc Tên Sinh Viên", Toast.LENGTH_SHORT).show();
            return false;
        }  else if (sdtsv.trim().length() > 0 && !Patterns.PHONE.matcher(sdtsv).matches()) {
            Toast.makeText(this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }else if (emailsv.trim().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(emailsv).matches()) {
            Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }else
        return true;


    }

    private void ganMaLopVaoSpiner() {
        ArrayList<String> arrmalop;
        arrmalop = layMaBangLop();
        ArrayAdapter<String> adapMalop = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrmalop);
        adapMalop.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// không có dòng này thì hiển thị không đẹp
        spnMaLopSvAdd.setAdapter(adapMalop);
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

    public void readDataSinhVien() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from tbSinhVien", null);
        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String MaSv = cursor.getString(0);
            String TenSv = cursor.getString(1);
            String MaLopSv = cursor.getString(2);
            String SdtSv = cursor.getString(3);
            String EmailSv = cursor.getString(4);
            byte[] HinhSv = cursor.getBlob(5);

            list.add(new ModalSinhVien(MaSv, TenSv, MaLopSv, SdtSv, EmailSv, HinhSv));
        }
        adapterSinhVien.notifyDataSetChanged();

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
            imgHinhDaiDienadd.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinhDaiDienadd.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void thuchientim() {
        ArrayList<String> arrayList = timTenSinhVien(edtSearchSv.getText().toString());
        adapterSinhVien = new AdapterSinhVien(this, list);
        lvSinhVien.setAdapter(adapterSinhVien);

    }

    public ArrayList<String> timTenSinhVien(String chuoitim) {
        ArrayList<String> arrTim = new ArrayList<>();
        SQLiteDatabase db = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from tbSinhVien " +
                        "where colTenSv like '%" + chuoitim + "%'" +
                        "or colSdtSv like '%" + chuoitim + "%'" +
                        "or colEmailSv like '%" + chuoitim + "%'" +
                        "or colMaLopSv like '%" + chuoitim + "%'",
                null);
        list.clear();
        while (cursor.moveToNext()) {
            String masv = cursor.getString(0);
            String tensv = cursor.getString(1);
            String malopsv = cursor.getString(2);
            String sdtsv = cursor.getString(3);
            String emailsv = cursor.getString(4);
            byte[] hinhdaidiensv = cursor.getBlob(5);

            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhdaidiensv, 0, hinhdaidiensv.length);
            list.add(new ModalSinhVien(masv, tensv, malopsv, sdtsv, emailsv, hinhdaidiensv));
            ModalSinhVien sinhVien = new ModalSinhVien(masv, tensv, malopsv, sdtsv, emailsv, hinhdaidiensv);
            arrTim.add(sdtsv);
        }

        cursor.close();
        return arrTim;


    }

    private void addSinhVien() {
        String ms = edtMaSvadd.getText().toString();
        String TenSv = edtTenSvadd.getText().toString();
//        String MaLopSv = edtMaLopSvadd.getText().toString();
        String MaLopSv = spnMaLopSvAdd.getSelectedItem().toString();
        String SdtSv = edtSdtSvadd.getText().toString();
        String EmailSv = edtEmailSvadd.getText().toString();
        byte[] Anh = getByteArrayFromImageView(imgHinhDaiDienadd);

        ContentValues contentValues = new ContentValues();
        contentValues.put("colMaSV", ms);
        contentValues.put("colTenSv", TenSv);
        contentValues.put("colMaLopSv", MaLopSv);
        contentValues.put("colSdtSv", SdtSv);
        contentValues.put("colEmailSv", EmailSv);
        contentValues.put("colHinhSv", Anh);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        int kq = (int) database.insert("tbSinhVien", null, contentValues);
        if (kq == 0) {
            Toast.makeText(ActivityQuanLySinhVien.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityQuanLySinhVien.this, "Thêm Thành công", Toast.LENGTH_SHORT).show();
        }
        readDataSinhVien();
    }

    //Hàm đổi 1 ImageView sang dữ liệu Byte
    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }


}
