package com.example.studentscoremanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentscoremanagement.Model.DiemMonHocDTO;
import java.util.ArrayList;
import java.util.List;

public class StudentEditorActivity extends AppCompatActivity {
    private EditText editTextMaHS, editTextNhapHo, editTextNhapTen, editTextNhapPhai, editTextNhapNgaySinh, editTextNhapLop;
    private RecyclerView recyclerViewDiem;
    private Button btnTruoc, btnSau, btnHuy, buttonLuu;
    private DBHelper dbHelper;
    private DiemAdapter diemAdapter;
    private List<DiemMonHocDTO> diemMonHocList;
    private List<Integer> dsHocSinh;
    private int currentIndex = -1;
    private String maHS = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_editor);

        // Khởi tạo các view
        editTextMaHS = findViewById(R.id.editTextMaHSs);
        editTextNhapHo = findViewById(R.id.editTextNhapHo);
        editTextNhapTen = findViewById(R.id.editTextNhapTen);
        editTextNhapPhai = findViewById(R.id.editTextNhapPhai);
        editTextNhapNgaySinh = findViewById(R.id.editTextNhapNgaySinh);
        editTextNhapLop = findViewById(R.id.editTextNhapLops);
        recyclerViewDiem = findViewById(R.id.recyclerViewDiems);
        btnTruoc = findViewById(R.id.btnTruoc);
        btnSau = findViewById(R.id.btnSau);
        btnHuy = findViewById(R.id.btnHuy);
        buttonLuu = findViewById(R.id.buttonLUU);

        dbHelper = new DBHelper(this);
        diemMonHocList = new ArrayList<>();
        dsHocSinh = new ArrayList<>();

        // Tải danh sách học sinh và môn học
        loadDanhSachHocSinh();
        loadMonHoc();

        // Thiết lập RecyclerView
        recyclerViewDiem.setLayoutManager(new LinearLayoutManager(this));
        diemAdapter = new DiemAdapter(diemMonHocList);
        recyclerViewDiem.setAdapter(diemAdapter);

        // Nhận mã học sinh từ Intent (nếu có)
        Intent intent = getIntent();
        if (intent.hasExtra("maHS")) {
            maHS = String.valueOf(intent.getIntExtra("maHS", -1));
            currentIndex = dsHocSinh.indexOf(Integer.parseInt(maHS));
            loadThongTinHocSinh(maHS);
        } else {
            clearForm(); // Thêm học sinh mới
        }

        // Sự kiện nút
        btnTruoc.setOnClickListener(v -> chuyenHocSinh(-1));
        btnSau.setOnClickListener(v -> chuyenHocSinh(1));
        btnHuy.setOnClickListener(v -> finish());
        buttonLuu.setOnClickListener(v -> luuHocSinhVaDiem());
    }

    private void loadDanhSachHocSinh() {
        Cursor cursor = dbHelper.GetData("SELECT " + DBHelper.COL_HOCSINH_MAHOCSINH + " FROM " + DBHelper.TB_HOCSINH, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        while (cursor.moveToNext()) {
            dsHocSinh.add(cursor.getInt(0));
        }
        cursor.close();
    }

    private void loadMonHoc() {
        diemMonHocList.clear();
        Cursor cursor = dbHelper.GetData("SELECT * FROM " + DBHelper.TB_MONHOC, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        while (cursor.moveToNext()) {
            String maMonHoc = String.valueOf(cursor.getInt(0));
            String tenMonHoc = cursor.getString(1);
            int heSo = cursor.getInt(2);
            diemMonHocList.add(new DiemMonHocDTO(maMonHoc, tenMonHoc, heSo, 0));
        }
        cursor.close();
    }

    private void loadThongTinHocSinh(String maHS) {
        Cursor cursor = dbHelper.GetData("SELECT * FROM " + DBHelper.TB_HOCSINH + " WHERE " + DBHelper.COL_HOCSINH_MAHOCSINH + " = " + maHS, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        if (cursor.moveToFirst()) {
            editTextMaHS.setText(maHS);
            editTextNhapHo.setText(cursor.getString(1));
            editTextNhapTen.setText(cursor.getString(2));
            editTextNhapPhai.setText(cursor.getString(3));
            editTextNhapNgaySinh.setText(cursor.getString(4));
            editTextNhapLop.setText(cursor.getString(5));
        }
        cursor.close();

        // Tải điểm
        for (DiemMonHocDTO diemMonHoc : diemMonHocList) {
            Cursor diemCursor = dbHelper.GetData("SELECT " + DBHelper.COL_DIEM_DIEM + " FROM " + DBHelper.TB_DIEM +
                    " WHERE " + DBHelper.COL_DIEM_MAHOCSINH + " = " + maHS + " AND " + DBHelper.COL_DIEM_MAMONHOC + " = " + diemMonHoc.getMaMH(), new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
            if (diemCursor.moveToFirst()) {
                diemMonHoc.setDiem(diemCursor.getFloat(0));
            } else {
                diemMonHoc.setDiem(0);
            }
            diemCursor.close();
        }
        diemAdapter.notifyDataSetChanged();
        updateButtonState();
    }

    private void chuyenHocSinh(int delta) {
        if (currentIndex + delta >= 0 && currentIndex + delta < dsHocSinh.size()) {
            currentIndex += delta;
            maHS = String.valueOf(dsHocSinh.get(currentIndex));
            loadThongTinHocSinh(maHS);
        }
    }

    private void updateButtonState() {
        btnTruoc.setEnabled(currentIndex > 0);
        btnSau.setEnabled(currentIndex < dsHocSinh.size() - 1);
    }

    private void clearForm() {
        maHS = "-1";
        editTextMaHS.setText("");
        editTextNhapHo.setText("");
        editTextNhapTen.setText("");
        editTextNhapPhai.setText("");
        editTextNhapNgaySinh.setText("");
        editTextNhapLop.setText("");
        for (DiemMonHocDTO diemMonHoc : diemMonHocList) {
            diemMonHoc.setDiem(0);
        }
        diemAdapter.notifyDataSetChanged();
        btnTruoc.setEnabled(false);
        btnSau.setEnabled(dsHocSinh.size() > 0);
    }

    private void luuHocSinhVaDiem() {
        String ho = editTextNhapHo.getText().toString().trim();
        String ten = editTextNhapTen.getText().toString().trim();
        String phai = editTextNhapPhai.getText().toString().trim();
        String ngaySinh = editTextNhapNgaySinh.getText().toString().trim();
        String maLop = editTextNhapLop.getText().toString().trim();

        if (ho.isEmpty() || ten.isEmpty() || phai.isEmpty() || ngaySinh.isEmpty() || maLop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin học sinh", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (maHS.equals("-1")) { // Thêm học sinh mới
            String sqlHocSinh = "INSERT INTO " + DBHelper.TB_HOCSINH + " (" +
                    DBHelper.COL_HOCSINH_HO + ", " +
                    DBHelper.COL_HOCSINH_TEN + ", " +
                    DBHelper.COL_HOCSINH_PHAI + ", " +
                    DBHelper.COL_HOCSINH_NGAYSINH + ", " +
                    DBHelper.COL_HOCSINH_MALOP + ") VALUES ('" +
                    ho + "', '" + ten + "', '" + phai + "', '" + ngaySinh + "', '" + maLop + "')";
            dbHelper.QueryData(sqlHocSinh);

            Cursor cursor = dbHelper.GetData("SELECT last_insert_rowid()", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
            cursor.moveToFirst();
            maHS = String.valueOf(cursor.getInt(0));
            cursor.close();
            dsHocSinh.add(Integer.parseInt(maHS));
            currentIndex = dsHocSinh.size() - 1;
            editTextMaHS.setText(maHS);
            Toast.makeText(this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
        } else { // Cập nhật học sinh hiện có
            String sqlUpdate = "UPDATE " + DBHelper.TB_HOCSINH + " SET " +
                    DBHelper.COL_HOCSINH_HO + " = '" + ho + "', " +
                    DBHelper.COL_HOCSINH_TEN + " = '" + ten + "', " +
                    DBHelper.COL_HOCSINH_PHAI + " = '" + phai + "', " +
                    DBHelper.COL_HOCSINH_NGAYSINH + " = '" + ngaySinh + "', " +
                    DBHelper.COL_HOCSINH_MALOP + " = '" + maLop + "' WHERE " +
                    DBHelper.COL_HOCSINH_MAHOCSINH + " = " + maHS;
            dbHelper.QueryData(sqlUpdate);
            Toast.makeText(this, "Cập nhật học sinh thành công", Toast.LENGTH_SHORT).show();
        }

        // Lưu hoặc cập nhật điểm
        for (DiemMonHocDTO diemMonHoc : diemAdapter.getDiemMonHocList()) {
            if (diemMonHoc.getDiem() >= 0) { // Lưu tất cả điểm, kể cả 0
                String sqlDiem = "INSERT OR REPLACE INTO " + DBHelper.TB_DIEM + " (" +
                        DBHelper.COL_DIEM_MAHOCSINH + ", " +
                        DBHelper.COL_DIEM_MAMONHOC + ", " +
                        DBHelper.COL_DIEM_DIEM + ") VALUES (" +
                        maHS + ", " + diemMonHoc.getMaMH() + ", " + diemMonHoc.getDiem() + ")";
                dbHelper.QueryData(sqlDiem);
            }
        }
        db.close();

        updateButtonState();
    }
}