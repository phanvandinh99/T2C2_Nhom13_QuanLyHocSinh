package com.example.studentscoremanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.studentscoremanagement.Adapter.AdapterHocSinh;
import com.example.studentscoremanagement.DBHelper;
import com.example.studentscoremanagement.Model.HocSinh;
import com.example.studentscoremanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class DSSVFragment extends Fragment {
    private static final String ARG_CLASS_ID = "CLASS_ID";
    private String CLASS_ID;
    private String teacherName;
    DBHelper database;

    Button buttonBC, btnTruoc, btnSau;
    FloatingActionButton btnThem;
    ListView lvHocSinh;
    ArrayList<HocSinh> arrayHocSinh;
    ArrayList<String> arrayLop;
    AdapterHocSinh adapter;
    TextView textClassId, textGV;
    EditText edtTimKiem;
    ImageView imageTimKiem;

    public DSSVFragment() {}

    public static DSSVFragment newInstance(String param1) {
        DSSVFragment fragment = new DSSVFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLASS_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CLASS_ID = getArguments().getString(ARG_CLASS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dssv, container, false);
        setControl(view);
        setEvent();
        GetDataHocSinh();
        GetDataLop();
        loadTrangThaiButton();
        return view;
    }

    private void setControl(View view) {
        database = new DBHelper(getContext());
        arrayLop = new ArrayList<>();
        arrayHocSinh = new ArrayList<>();
        textGV = view.findViewById(R.id.textGV);
        buttonBC = view.findViewById(R.id.buttonBC);
        btnSau = view.findViewById(R.id.buttonSau);
        textClassId = view.findViewById(R.id.textLop);
        btnThem = view.findViewById(R.id.buttonThem);
        btnTruoc = view.findViewById(R.id.buttonTruoc);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        lvHocSinh = view.findViewById(R.id.listViewMSHS);
        imageTimKiem = view.findViewById(R.id.imageTimKiem);
        adapter = new AdapterHocSinh(getContext(), R.layout.activity_dssv_ds, arrayHocSinh, getActivity(), this);
        lvHocSinh.setAdapter(adapter);
    }

    private void setEvent() {
        btnThem.setOnClickListener(v -> ThemSinhVien());
        buttonBC.setOnClickListener(v -> {
            FragmentReport fragmentReport = FragmentReport.newInstance(CLASS_ID, textGV.getText().toString());
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fragmentReport);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        btnTruoc.setOnClickListener(v -> {
            String newClassID = CLASS_ID.substring(3);
            CLASS_ID = CLASS_ID.substring(0, 3) + (Integer.parseInt(newClassID) - 1);
            GetDataHocSinh();
            loadTrangThaiButton();
        });

        btnSau.setOnClickListener(v -> {
            String newClassID = CLASS_ID.substring(3);
            CLASS_ID = CLASS_ID.substring(0, 3) + (Integer.parseInt(newClassID) + 1);
            GetDataHocSinh();
            loadTrangThaiButton();
        });

        imageTimKiem.setOnClickListener(v -> {
            String noiDung = edtTimKiem.getText().toString().trim();
            Cursor dataHS = database.GetData("SELECT * FROM " + DBHelper.TB_HOCSINH +
                    " WHERE (hocSinh_maHocSinh LIKE '%" + noiDung + "%' OR hocSinh_ho LIKE '%" + noiDung + "%' OR hocSinh_ten LIKE '%" + noiDung + "%') AND " +
                    DBHelper.COL_HOCSINH_MALOP + "= '" + CLASS_ID + "'", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
            arrayHocSinh.clear();
            if (dataHS != null && dataHS.moveToFirst()) {
                do {
                    String id = dataHS.getString(0);
                    String ho = dataHS.getString(1);
                    String ten = dataHS.getString(2);
                    String phai = dataHS.getString(3);
                    String ngaySinh = dataHS.getString(4);
                    arrayHocSinh.add(new HocSinh(id, ho, ten, phai, ngaySinh));
                } while (dataHS.moveToNext());
            }
            dataHS.close();
            adapter.notifyDataSetChanged();
        });
    }

    private void GetDataHocSinh() {
        arrayHocSinh.clear();

        // Lấy tên giáo viên chủ nhiệm
        Cursor getTeacherName = database.GetData("SELECT " + DBHelper.COL_LOP_CHUNHIEM + " FROM " + DBHelper.TB_LOP + " WHERE " + DBHelper.COL_LOP_MALOP + "='" + CLASS_ID + "'", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        if (getTeacherName != null && getTeacherName.moveToFirst()) {
            teacherName = getTeacherName.getString(0);
        } else {
            teacherName = "Chưa có giáo viên"; // Xử lý khi không tìm thấy lớp
            Log.d("DSSVFragment", "No teacher found for class: " + CLASS_ID);
        }
        if (getTeacherName != null) getTeacherName.close();

        // Lấy danh sách học sinh
        Cursor dataHS = database.GetData("SELECT * FROM " + DBHelper.TB_HOCSINH + " WHERE " + DBHelper.COL_HOCSINH_MALOP + "='" + CLASS_ID + "'", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        if (dataHS != null && dataHS.moveToFirst()) {
            do {
                String id = dataHS.getString(0);
                String ho = dataHS.getString(1);
                String ten = dataHS.getString(2);
                String phai = dataHS.getString(3);
                String ngaySinh = dataHS.getString(4);
                arrayHocSinh.add(new HocSinh(id, ho, ten, phai, ngaySinh));
            } while (dataHS.moveToNext());
        } else {
            Toast.makeText(getContext(), "Lớp " + CLASS_ID + " chưa có học sinh", Toast.LENGTH_SHORT).show();
            Log.d("DSSVFragment", "No students found for class: " + CLASS_ID);
        }
        if (dataHS != null) dataHS.close();

        adapter.notifyDataSetChanged();
        textClassId.setText(CLASS_ID);
        textGV.setText(teacherName);
    }

    private void GetDataLop() {
        Cursor data = database.GetData("SELECT " + DBHelper.COL_LOP_MALOP + " FROM " + DBHelper.TB_LOP, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        arrayLop.clear();
        if (data != null && data.moveToFirst()) {
            do {
                arrayLop.add(data.getString(0));
            } while (data.moveToNext());
        }
        if (data != null) data.close();
    }

    private void ThemSinhVien() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.themhocsinh);

        EditText editHo = dialog.findViewById(R.id.editTextNhapHo);
        EditText editTen = dialog.findViewById(R.id.editTextNhapTen);
        EditText editPhai = dialog.findViewById(R.id.editTextNhapPhai);
        EditText editNSinh = dialog.findViewById(R.id.editTextNhapNgaySinh);
        EditText editTenDangNhap = dialog.findViewById(R.id.editTextNhapTenDangNhap);
        EditText editMatKhau = dialog.findViewById(R.id.editTextNhapMatKhau);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnThem = dialog.findViewById(R.id.buttonLUU);

        btnThem.setOnClickListener(v -> {
            String ho = editHo.getText().toString().trim();
            String ten = editTen.getText().toString().trim();
            String phai = editPhai.getText().toString().trim();
            String ngaysinh = editNSinh.getText().toString().trim();
            String tenDangNhap = editTenDangNhap.getText().toString().trim();
            String matKhau = editMatKhau.getText().toString().trim();

            if (ho.isEmpty() || ten.isEmpty() || phai.isEmpty() || ngaysinh.isEmpty() ||
                    tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    database.QueryData("INSERT INTO " + DBHelper.TB_HOCSINH + " VALUES (null, '" +
                            ho + "', '" + ten + "', '" + phai + "', '" + ngaysinh + "', '" +
                            CLASS_ID + "', '" + tenDangNhap + "', '" + matKhau + "')");
                    Toast.makeText(getContext(), "Đã Thêm", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataHocSinh();
                } catch (Exception e) {
                    Log.d("DSSVFragment", "Error adding student: " + e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void DialogXoa(String ten, String MaHS) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(getContext());
        dialogXoa.setMessage("Bạn có muốn xóa học sinh " + ten + " không?");
        dialogXoa.setPositiveButton("Có", (dialogInterface, i) -> {
            database.QueryData("DELETE FROM " + DBHelper.TB_HOCSINH + " WHERE " + DBHelper.COL_HOCSINH_MAHOCSINH + " = '" + MaHS + "'");
            Toast.makeText(getContext(), "Đã xóa " + ten, Toast.LENGTH_SHORT).show();
            GetDataHocSinh();
        });
        dialogXoa.setNegativeButton("Không", (dialogInterface, i) -> {});
        dialogXoa.show();
    }

    private void loadTrangThaiButton() {
        if (arrayLop.isEmpty()) {
            btnTruoc.setEnabled(false);
            btnSau.setEnabled(false);
        } else if (CLASS_ID.equals(arrayLop.get(0))) {
            btnTruoc.setEnabled(false);
            btnSau.setEnabled(true);
        } else if (CLASS_ID.equals(arrayLop.get(arrayLop.size() - 1))) {
            btnTruoc.setEnabled(true);
            btnSau.setEnabled(false);
        } else {
            btnTruoc.setEnabled(true);
            btnSau.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GetDataHocSinh(); // Cập nhật danh sách khi quay lại
    }
}