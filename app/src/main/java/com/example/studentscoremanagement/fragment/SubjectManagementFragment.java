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

import com.example.studentscoremanagement.Adapter.SubjectAdapter;
import com.example.studentscoremanagement.DBHelper;
import com.example.studentscoremanagement.Model.MonHoc;
import com.example.studentscoremanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SubjectManagementFragment extends Fragment {
    private ListView listViewSubjects;
    private FloatingActionButton btnAddSubject;
    private ArrayList<MonHoc> subjectList;
    private SubjectAdapter adapter;
    private DBHelper dbHelper;

    public SubjectManagementFragment() {}

    public static SubjectManagementFragment newInstance() {
        return new SubjectManagementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_management, container, false);

        listViewSubjects = view.findViewById(R.id.listViewSubjects);
        btnAddSubject = view.findViewById(R.id.btnAddSubject);
        dbHelper = new DBHelper(getContext());
        subjectList = new ArrayList<>();

        adapter = new SubjectAdapter(getContext(), R.layout.subject_item, subjectList, this);
        listViewSubjects.setAdapter(adapter);

        loadSubjects();

        btnAddSubject.setOnClickListener(v -> showAddSubjectDialog());

        return view;
    }

    private void loadSubjects() {
        subjectList.clear();
        Cursor cursor = dbHelper.GetData("SELECT * FROM " + DBHelper.TB_MONHOC);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String maMH = cursor.getString(0);
                String tenMH = cursor.getString(1);
                int heSo = cursor.getInt(2);
                subjectList.add(new MonHoc(maMH, tenMH, heSo));
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddSubjectDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_subject);

        EditText editMaMH = dialog.findViewById(R.id.editTextMaMH);
        EditText editTenMH = dialog.findViewById(R.id.editTextTenMH);
        EditText editHeSo = dialog.findViewById(R.id.editTextHeSo);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            String maMH = editMaMH.getText().toString().trim();
            String tenMH = editTenMH.getText().toString().trim();
            String heSoStr = editHeSo.getText().toString().trim();
            if (maMH.isEmpty() || tenMH.isEmpty() || heSoStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                int heSo = Integer.parseInt(heSoStr);
                dbHelper.QueryData("INSERT INTO " + DBHelper.TB_MONHOC + " VALUES ('" + maMH + "', '" + tenMH + "', " + heSo + ")");
                Toast.makeText(getContext(), "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
                loadSubjects();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void showEditSubjectDialog(MonHoc monHoc) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_subject);

        EditText editMaMH = dialog.findViewById(R.id.editTextMaMH);
        EditText editTenMH = dialog.findViewById(R.id.editTextTenMH);
        EditText editHeSo = dialog.findViewById(R.id.editTextHeSo);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        editMaMH.setText(monHoc.getMaMH());
        editMaMH.setEnabled(false); // Không cho sửa mã môn học
        editTenMH.setText(monHoc.getTenMH());
        editHeSo.setText(String.valueOf(monHoc.getHeSo()));

        btnSave.setOnClickListener(v -> {
            String tenMH = editTenMH.getText().toString().trim();
            String heSoStr = editHeSo.getText().toString().trim();
            if (tenMH.isEmpty() || heSoStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                int heSo = Integer.parseInt(heSoStr);
                dbHelper.QueryData("UPDATE " + DBHelper.TB_MONHOC + " SET " + DBHelper.COL_MONHOC_TENMONHOC + " = '" + tenMH + "', " +
                        DBHelper.COL_MONHOC_HESO + " = " + heSo + " WHERE " + DBHelper.COL_MONHOC_MAMONHOC + " = '" + monHoc.getMaMH() + "'");
                Toast.makeText(getContext(), "Cập nhật môn học thành công", Toast.LENGTH_SHORT).show();
                loadSubjects();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void showDeleteSubjectDialog(String maMH, String tenMH) {
        new AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn xóa môn học " + tenMH + " (Mã: " + maMH + ") không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    dbHelper.QueryData("DELETE FROM " + DBHelper.TB_MONHOC + " WHERE " + DBHelper.COL_MONHOC_MAMONHOC + " = '" + maMH + "'");
                    Toast.makeText(getContext(), "Đã xóa môn học " + tenMH, Toast.LENGTH_SHORT).show();
                    loadSubjects();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}