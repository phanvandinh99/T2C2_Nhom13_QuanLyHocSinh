package com.example.studentscoremanagement.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.DBHelper;
import com.example.studentscoremanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentManagerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "maHS";

    // TODO: Rename and change types of parameters
    private int maHS;

    int tongMH = 0;
    int tongHeSo = 0;
    float tongDiem = 0;
    String maLop;

    DBHelper database;
    TableLayout tbL;
    Button btnTruoc, btnSau;
    TextView tvMaHS, tvHoTen, tvNgaySinh, tvLop, tvPhai, tvTongMH, tvDiemTB;
    List<String> dsHS = new ArrayList<>();

    public StudentManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StudentManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentManagerFragment newInstance(int param1) {
        StudentManagerFragment fragment = new StudentManagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maHS = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_manager, container, false);

        addControl(view);
        setEvent();

        return view;
    }

    private void addControl(View view) {
        tvMaHS = view.findViewById(R.id.tvMaHS);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvLop = view.findViewById(R.id.tvLop);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvPhai = view.findViewById(R.id.tvPhai);
        btnTruoc = view.findViewById(R.id.btnTruoc);
        btnSau = view.findViewById(R.id.btnSau);
        tbL = (TableLayout) view.findViewById(R.id.tbDiem);
        tvTongMH = view.findViewById(R.id.tvTongMH);
        tvDiemTB = view.findViewById(R.id.tvDiemTB);
    }

    private void setEvent() {
        database = new DBHelper(getContext());

//        Intent i = getIntent();
//        String value = i.getStringExtra("maHS");
//        maHS = Integer.parseInt(value);

        layThongTin(database);
        layMonHoc(database);
        layDSLop(database);
        loadTrangThaiButton();
        loadDiemTrungBinh();



        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maHS -= 1;
                layThongTin(database);
                tbL.removeViewsInLayout(1, tongMH);
                layMonHoc(database);
                loadTrangThaiButton();
                loadDiemTrungBinh();
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maHS += 1;
                layThongTin(database);
                tbL.removeViewsInLayout(1, tongMH);
                layMonHoc(database);
                loadTrangThaiButton();
                loadDiemTrungBinh();
            }
        });
    }

    private void loadDiemTrungBinh() {
        float diemTB = tongDiem / tongHeSo;
        String diemLamTron = String.format("%.1f", diemTB);
        if(diemLamTron.equals("NaN"))
        {
            tvDiemTB.setText(".");
        }
        else
            tvDiemTB.setText(diemLamTron);
        tongDiem = 0;
        tongHeSo = 0;
    }

    private void loadTrangThaiButton() {
        tvTongMH.setText(""+tongMH);

        if(maHS == Integer.parseInt(dsHS.get(0)))
        {
            btnTruoc.setEnabled(false);
        }
        else if(maHS == Integer.parseInt(dsHS.get(dsHS.size()-1)))
        {
            btnSau.setEnabled(false);
        }
        else
        {
            btnTruoc.setEnabled(true);
            btnSau.setEnabled(true);
        }
    }


    public void layThongTin(DBHelper db){
        String ho, ten, phai, ngaysinh, malop = "";
        Cursor data =db.GetData("SELECT "+ DBHelper.COL_HOCSINH_HO +", "+DBHelper.COL_HOCSINH_TEN +
                ", " + DBHelper.COL_HOCSINH_PHAI + ", " + DBHelper.COL_HOCSINH_NGAYSINH + ", " + DBHelper.COL_HOCSINH_MALOP +
                " FROM "+ DBHelper.TB_HOCSINH
                +" WHERE "+ DBHelper.COL_HOCSINH_MAHOCSINH + "= " + maHS, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        if(data.moveToNext()){
            ho = data.getString(0);
            ten = data.getString(1);
            phai = data.getString(2);
            ngaysinh = data.getString(3);
            malop = data.getString(4);
            maLop = malop;
            tvMaHS.setText(""+maHS);
            tvHoTen.setText(ho + " " + ten);
            tvLop.setText(malop);
            tvNgaySinh.setText(ngaysinh);
            tvPhai.setText(phai);
        }

    }

    private void layMonHoc(DBHelper db) {
        String maMH, tenMH;
        String diem;
        int tongSoMH = 0;
        String heSo;

        Cursor data = db.GetData("SELECT " + DBHelper.COL_MONHOC_MAMONHOC + ", " + DBHelper.COL_MONHOC_TENMONHOC
                + ", " + DBHelper.COL_MONHOC_HESO + " FROM " + DBHelper.TB_MONHOC, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        while(data.moveToNext())
        {
            tongSoMH += 1 ;
            maMH = data.getString(0);
            tenMH = data.getString(1);
            heSo = data.getString(2);
            tongHeSo += Integer.parseInt(heSo);
            TableRow tbRow = new TableRow(getContext());

            TextView tv = new TextView(getContext());
            tv.setText(maMH);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            tv.setBackgroundResource(R.drawable.vien_den);
            tv.setPadding(0,20,0,20);
            tbRow.addView(tv);

            TextView tv2 = new TextView(getContext());
            tv2.setText(tenMH);
            tv2.setTextColor(Color.WHITE);
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(20);
            tv2.setBackgroundResource(R.drawable.vien_den);
            tv2.setPadding(0,20,0,20);
            tbRow.addView(tv2);

            Cursor dataDiem = db.GetData("SELECT " + DBHelper.COL_DIEM_DIEM + " FROM " + DBHelper.TB_DIEM
                    + " WHERE " + DBHelper.COL_DIEM_MAHOCSINH + " = " + maHS +
                    " AND " + DBHelper.COL_DIEM_MAMONHOC + " = " + maMH, new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});

            if(dataDiem.moveToNext())
            {
                diem = ""+dataDiem.getFloat(0);
                tongDiem += dataDiem.getFloat(0)*Integer.parseInt(heSo);

            }
            else
            {
                diem = ".";
            }

            TextView tv3 = new TextView(getContext());
            tv3.setText("" + diem);
            tv3.setTextColor(Color.WHITE);
            tv3.setGravity(Gravity.CENTER);
            tv3.setTextSize(20);
            tv3.setBackgroundResource(R.drawable.vien_den);
            tv3.setPadding(0,20,0,20);
            int id = Integer.parseInt(maMH);
            String diemNhap = diem;
            tv3.setId(id);
            tv3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dialogNhapDiem(diemNhap, id);
                    return false;
                }
            });
            tbRow.addView(tv3);
            tbL.addView(tbRow);
        }
        tongMH = tongSoMH;
    }

    private void layDSLop(DBHelper db) {
        Cursor dsLopData = db.GetData("SELECT " + DBHelper.COL_HOCSINH_MAHOCSINH + " FROM " + DBHelper.TB_HOCSINH
                + " WHERE " + DBHelper.COL_HOCSINH_MALOP + " = '" + maLop + "'", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        while(dsLopData.moveToNext())
        {
            String data = dsLopData.getString(0);
            dsHS.add(data);
        }
    }


    private void dialogNhapDiem(String diem, int maMH){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhap_diem);

        EditText edtNhapDiem = (EditText) dialog.findViewById(R.id.edtNhapDiem);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        if(diem.equals("."))
            edtNhapDiem.setText("");
        else
            edtNhapDiem.setText(diem);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diemMoi = edtNhapDiem.getText().toString().trim();
                if(TextUtils.isEmpty(diemMoi))
                {
                    Toast.makeText(getContext(), "Vui lòng nhập điểm cho môn học", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Float diemMoiFloat = isFloat(diemMoi);
                    if(diemMoiFloat == -1)
                    {
                        Toast.makeText(getContext(), "Điểm nhập phải thuộc khoảng từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    }
                    else if (diemMoiFloat == -2)
                    {
                        Toast.makeText(getContext(), "Vui lòng nhập đúng định dạng điểm", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Nếu chưa có điểm thì insert điểm mới vào
                        if(diem == "."){
                            insertDiem(database, maMH , diemMoiFloat);
                            Toast.makeText(getContext(), "Nhập điểm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        // Nếu có điểm rồi thì update điểm
                        else
                        {
                            updateDiem(database, maMH, diemMoiFloat);
                            Toast.makeText(getContext(), "Sửa điểm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private float isFloat(String diemString)
    {
        try {
            float diemFloat = Float.parseFloat(diemString);
            if(diemFloat >= 0 && diemFloat <=10 )
            {
                return diemFloat;
            }
            else
            {
                return -1;
            }

        }
        catch (NumberFormatException e)
        {
        }
        return -2;
    }

    private void insertDiem(DBHelper db, int maMH, float diemMoi)
    {
        db.QueryData("INSERT INTO " + DBHelper.TB_DIEM + " VALUES (" + maHS + ", " +  maMH + ", " + diemMoi + ")" );
        tbL.removeViewsInLayout(1, 7);
        layMonHoc(database);
        loadDiemTrungBinh();
    }

    private void updateDiem(DBHelper db, int maMH, float diemMoi)
    {
        db.QueryData("UPDATE " + DBHelper.TB_DIEM + " SET " + DBHelper.COL_DIEM_DIEM + " = " + diemMoi
                + " WHERE " + DBHelper.COL_DIEM_MAHOCSINH + " = " + maHS + " AND " + DBHelper.COL_DIEM_MAMONHOC + " = " + maMH);
        tbL.removeViewsInLayout(1, 7);
        layMonHoc(database);
        loadDiemTrungBinh();
    }
}