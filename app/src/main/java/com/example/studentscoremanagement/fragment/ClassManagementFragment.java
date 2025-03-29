package com.example.studentscoremanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.studentscoremanagement.Adapter.ClassAdapter;
import com.example.studentscoremanagement.DBHelper;
import com.example.studentscoremanagement.Model.Lop;
import com.example.studentscoremanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ClassManagementFragment extends Fragment {
    private ListView listViewClasses;
    private FloatingActionButton btnAddClass;
    private ArrayList<Lop> classList;
    private ClassAdapter adapter;
    private DBHelper dbHelper;

    public ClassManagementFragment() {}

    public static ClassManagementFragment newInstance() {
        return new ClassManagementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_management, container, false);

        listViewClasses = view.findViewById(R.id.listViewClasses);
        btnAddClass = view.findViewById(R.id.btnAddClass);
        dbHelper = new DBHelper(getContext());
        classList = new ArrayList<>();

        adapter = new ClassAdapter(getContext(), R.layout.class_item, classList, this);
        listViewClasses.setAdapter(adapter);

        loadClasses();

        btnAddClass.setOnClickListener(v -> showAddClassDialog());

        return view;
    }

    private void loadClasses() {
        classList.clear();
        Cursor cursor = dbHelper.GetData("SELECT * FROM " + DBHelper.TB_LOP, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        while (cursor.moveToNext()) {
            String maLop = cursor.getString(0);
            String chuNhiem = cursor.getString(1);
            classList.add(new Lop(maLop, chuNhiem));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void showAddClassDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_class);

        EditText editMaLop = dialog.findViewById(R.id.editTextMaLop);
        EditText editChuNhiem = dialog.findViewById(R.id.editTextChuNhiem);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            String maLop = editMaLop.getText().toString().trim();
            String chuNhiem = editChuNhiem.getText().toString().trim();
            if (maLop.isEmpty() || chuNhiem.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.QueryData("INSERT INTO " + DBHelper.TB_LOP + " VALUES ('" + maLop + "', '" + chuNhiem + "')");
                Toast.makeText(getContext(), "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                loadClasses();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void showEditClassDialog(Lop lop) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_class);

        EditText editMaLop = dialog.findViewById(R.id.editTextMaLop);
        EditText editChuNhiem = dialog.findViewById(R.id.editTextChuNhiem);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        editMaLop.setText(lop.getMaLop());
        editMaLop.setEnabled(false); // Không cho sửa mã lớp
        editChuNhiem.setText(lop.getChuNhiem());

        btnSave.setOnClickListener(v -> {
            String chuNhiem = editChuNhiem.getText().toString().trim();
            if (chuNhiem.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên chủ nhiệm", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.QueryData("UPDATE " + DBHelper.TB_LOP + " SET " + DBHelper.COL_LOP_CHUNHIEM + " = '" + chuNhiem +
                        "' WHERE " + DBHelper.COL_LOP_MALOP + " = '" + lop.getMaLop() + "'");
                Toast.makeText(getContext(), "Cập nhật lớp thành công", Toast.LENGTH_SHORT).show();
                loadClasses();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void showDeleteClassDialog(String maLop, String chuNhiem) {
        new AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn xóa lớp " + maLop + " (Chủ nhiệm: " + chuNhiem + ") không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    dbHelper.QueryData("DELETE FROM " + DBHelper.TB_LOP + " WHERE " + DBHelper.COL_LOP_MALOP + " = '" + maLop + "'");
                    Toast.makeText(getContext(), "Đã xóa lớp " + maLop, Toast.LENGTH_SHORT).show();
                    loadClasses();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}