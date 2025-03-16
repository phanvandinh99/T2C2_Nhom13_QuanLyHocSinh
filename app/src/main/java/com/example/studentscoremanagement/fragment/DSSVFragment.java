package com.example.studentscoremanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.Adapter.AdapterHocSinh;
import com.example.studentscoremanagement.DBHelper;
//import com.example.studentscoremanagement.DSSV;
import com.example.studentscoremanagement.Model.HocSinh;
import com.example.studentscoremanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DSSVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DSSVFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLASS_ID = "CLASS_ID";

    // TODO: Rename and change types of parameters
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

    public DSSVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment fragment_dssv.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dssv, container, false);

        setControl(view);
        setEvent();
        GetDataHocSinh();
        GetDataLop();
        loadTrangThaiButton();
        return view;
    }

    private void setControl(View view) {

        database=new DBHelper(getContext());

        arrayLop = new ArrayList<>();
        arrayHocSinh = new ArrayList<>();
        textGV=view.findViewById(R.id.textGV);
        buttonBC=view.findViewById(R.id.buttonBC);
        btnSau = view.findViewById(R.id.buttonSau);
        textClassId=view.findViewById(R.id.textLop);
        btnThem= view.findViewById(R.id.buttonThem);
        btnTruoc = view.findViewById(R.id.buttonTruoc);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        lvHocSinh = view.findViewById(R.id.listViewMSHS);
        imageTimKiem = view.findViewById(R.id.imageTimKiem);
        adapter = new AdapterHocSinh(getContext(), R.layout.activity_dssv_ds, arrayHocSinh, getActivity(),this);
        lvHocSinh.setAdapter(adapter);
    }

    private void setEvent() {

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSinhVien();
            }
        });
        buttonBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentReport fragmentReport=FragmentReport.newInstance(CLASS_ID,textGV.getText().toString());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragmentReport);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newClassID = CLASS_ID.substring(3);
                CLASS_ID = CLASS_ID.substring(0, 3) + (Integer.parseInt(newClassID) - 1);
                GetDataHocSinh();
                loadTrangThaiButton();
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newClassID = CLASS_ID.substring(3);
                CLASS_ID = CLASS_ID.substring(0, 3) + (Integer.parseInt(newClassID) + 1);
                GetDataHocSinh();
                loadTrangThaiButton();
            }
        });

        imageTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noiDung = edtTimKiem.getText().toString().trim();

                Cursor dataHS = database.GetData("SELECT * FROM " + DBHelper.TB_HOCSINH+
                                " WHERE (hocSinh_maHocSinh LIKE '%" + noiDung + "%' OR hocSinh_ho LIKE '%"
                                + noiDung + "%' OR hocSinh_ten LIKE '%" + noiDung + "%') AND "
                        + DBHelper.COL_HOCSINH_MALOP + "= '" + CLASS_ID + "'" );

                arrayHocSinh.clear();
                while (dataHS.moveToNext())
                {
                    String id = dataHS.getString(0);
                    String ho = dataHS.getString(1);
                    String ten = dataHS.getString(2);
                    String phai = dataHS.getString(3);
                    String ngaySinh = dataHS.getString(4);
                    arrayHocSinh.add(new HocSinh(id, ho, ten, phai, ngaySinh));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    // để data ra mh
    private void GetDataHocSinh(){
        Cursor getTeacherName = database.GetData("SELECT "+DBHelper.COL_LOP_CHUNHIEM+" FROM "+DBHelper.TB_LOP+" WHERE "+DBHelper.COL_LOP_MALOP+"='"+CLASS_ID+"'");//
        getTeacherName.moveToFirst();
        do
        {
            teacherName = getTeacherName.getString(0);
        }while (getTeacherName.moveToNext());
        Cursor dataHS = database.GetData("SELECT * FROM "+DBHelper.TB_HOCSINH+" WHERE "+DBHelper.COL_HOCSINH_MALOP+"='"+CLASS_ID+"'");//nghe ko bn ko nghe==>ông out meet r

        arrayHocSinh.clear();
        dataHS.moveToFirst();
        do
        {
            String id = dataHS.getString(0);
            String ho = dataHS.getString(1);
            String ten = dataHS.getString(2);
            String phai = dataHS.getString(3);
            String ngaySinh = dataHS.getString(4);
            arrayHocSinh.add(new HocSinh(id, ho, ten, phai, ngaySinh));
        }while (dataHS.moveToNext());
        adapter.notifyDataSetChanged();

        textClassId.setText(CLASS_ID);
        textGV.setText(teacherName);

    }

    private void GetDataLop(){
        String lop;

        Cursor data = database.GetData("SELECT " + DBHelper.COL_LOP_MALOP + " FROM " + DBHelper.TB_LOP);
        while(data.moveToNext())
        {
            lop = data.getString(0);
            arrayLop.add(lop);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.buttonThem){
            ThemSinhVien();
        }
        return super.onOptionsItemSelected(item);
    }



    private void ThemSinhVien(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.themhocsinh);

        EditText editHo = (EditText) dialog.findViewById(R.id.editTextNhapHo);
        EditText editTen = (EditText) dialog.findViewById(R.id.editTextNhapTen);
        EditText editPhai = (EditText) dialog.findViewById(R.id.editTextNhapPhai);
        EditText editNSinh = (EditText) dialog.findViewById(R.id.editTextNhapNgaySinh);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHUY);
        Button btnThem = (Button) dialog.findViewById(R.id.buttonLUU);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ho = editHo.getText().toString();
                String ten = editTen.getText().toString();
                String phai = editPhai.getText().toString();
                String ngaysinh= editNSinh.getText().toString();
                if( ho.equals("") || ten.equals("") || phai.equals("") ||ngaysinh.equals("")){
                    Toast.makeText(getContext(), "Vui lòng nhập không để trống!", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        database.QueryData("INSERT INTO " + DBHelper.TB_HOCSINH+" VALUES ( "+null+", '" +
                                ho+"', '"+ten+"', '"+phai+"', '"+ngaysinh+"', '"+CLASS_ID+"')");
                        Toast.makeText(getContext(), "Đã Thêm", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        GetDataHocSinh();
                    }catch (Exception e){
                       Log.d("print",e.getMessage());
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


    public void DialogSua(String MaHS, String Ho, String Ten, String Phai, String NgaySinh){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.themhocsinh);

        EditText editHo = (EditText) dialog.findViewById(R.id.editTextNhapHo);
        EditText editTen = (EditText) dialog.findViewById(R.id.editTextNhapTen);
        EditText editPhai = (EditText) dialog.findViewById(R.id.editTextNhapPhai);
        EditText editNSinh = (EditText) dialog.findViewById(R.id.editTextNhapNgaySinh);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHUY);
        Button btnThem = (Button) dialog.findViewById(R.id.buttonLUU);


        editHo.setText(Ho);
        editTen.setText(Ten);
        editPhai.setText(Phai);
        editNSinh.setText(NgaySinh);

        btnThem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String tenMoi = editTen.getText().toString().trim();
                String hoMoi = editHo.getText().toString().trim();
                String phaiMoi = editPhai.getText().toString().trim();
                String nSinhMoi = editNSinh.getText().toString().trim();
                try {
                    database.QueryData("UPDATE "+DBHelper.TB_HOCSINH +" SET  "+DBHelper.COL_HOCSINH_HO+" = '"+ hoMoi+"', "+DBHelper.COL_HOCSINH_TEN+" = '"+ tenMoi+"'," +
                            " "+DBHelper.COL_HOCSINH_PHAI+" = '"+ phaiMoi+"' , "+DBHelper.COL_HOCSINH_NGAYSINH+" = '"+ nSinhMoi+"'  WHERE "+DBHelper.COL_HOCSINH_MAHOCSINH+" ="+MaHS+"");
                    Toast.makeText(getContext(), "ĐÃ CẬP NHẬT", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataHocSinh();
                }catch (Exception e){
                    Log.d("print",e.getMessage());
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


    public void DialogXoa( String ten, String MaHS){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(getContext());
        dialogXoa.setMessage("Bạn có muốn xóa học sinh " + ten + " không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM "+DBHelper.TB_HOCSINH +" WHERE "+ DBHelper.COL_HOCSINH_MAHOCSINH+" = '" + MaHS + "'");
                Toast.makeText(getContext(), "Đã xóa " + ten, Toast.LENGTH_SHORT).show();
                GetDataHocSinh();
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogXoa.show();
    }

    private void loadTrangThaiButton() {

        if(CLASS_ID.equals(arrayLop.get(0)))
        {
            btnTruoc.setEnabled(false);
        }
        else if(CLASS_ID.equals(arrayLop.get(arrayLop.size()-1)))
        {
            btnSau.setEnabled(false);
        }
        else
        {
            btnTruoc.setEnabled(true);
            btnSau.setEnabled(true);
        }
    }
}