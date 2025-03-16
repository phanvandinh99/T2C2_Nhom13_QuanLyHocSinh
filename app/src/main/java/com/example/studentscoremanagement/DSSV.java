package com.example.studentscoremanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.Adapter.AdapterHocSinh;
import com.example.studentscoremanagement.Model.HocSinh;

import java.util.ArrayList;

public class DSSV extends AppCompatActivity {

    DBHelper database;

    Button btnThem,buttonBC, buttonTruoc, buttonSau;
    ListView lvHocSinh;
    ArrayList<HocSinh> arrayHocSinh;
    AdapterHocSinh adapter;
    TextView textGV, textLop;

    String idClass;

    public static final String CLASS_ID="CLASS_ID";
    public static final String TEACHER_NAME="TEACHER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dssv);



        getClassId();
        //lvHocSinh.setAdapter(adapter);
        setControl();
        setEvent();
        GetDataHocSinh();
    }

    private void getClassId() {
        Intent intent=getIntent();
        idClass=intent.getStringExtra(ChooseClassActivity.ID_CLASS);
    }

    private void setControl() {
        database=new DBHelper(DSSV.this);
        lvHocSinh = (ListView) findViewById(R.id.listViewMSHS);
        btnThem=findViewById(R.id.buttonThem);
        buttonBC=findViewById(R.id.buttonBC);
        arrayHocSinh = new ArrayList<>();
        textGV=findViewById(R.id.textGV);
        textLop=findViewById(R.id.textLop);
        buttonTruoc=findViewById(R.id.buttonTruoc);
        buttonSau = findViewById(R.id.buttonSau);
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
                Intent intent=new Intent(DSSV.this,ReportActivity.class);
                intent.putExtra(CLASS_ID,idClass);
                intent.putExtra(TEACHER_NAME,textGV.getText());
                startActivity(intent);
            }
        });

    }

    // để data ra mh
    private void GetDataHocSinh(){
        Cursor dataHS = database.GetData("SELECT * FROM "+DBHelper.TB_HOCSINH+" WHERE "+DBHelper.COL_HOCSINH_MALOP+"='"+idClass+"'");
        dataHS.moveToFirst();
        do
        {
            String id = dataHS.getString(0);
            //Toast.makeText(this, id , Toast.LENGTH_SHORT).show();
            String ho = dataHS.getString(1);
            //Toast.makeText(this, ho , Toast.LENGTH_SHORT).show();
            String ten = dataHS.getString(2);
            //Toast.makeText(this, ten , Toast.LENGTH_SHORT).show();
            String phai = dataHS.getString(3);
            //Toast.makeText(this, phai , Toast.LENGTH_SHORT).show();
            String ngaySinh = dataHS.getString(4);
            //Toast.makeText(this, ngaySinh , Toast.LENGTH_SHORT).show();
            arrayHocSinh.add(new HocSinh(id, ho, ten, phai, ngaySinh));
        }while (dataHS.moveToNext());
        Toast.makeText(this, String.valueOf(arrayHocSinh.get(0).getTen()) , Toast.LENGTH_SHORT).show();
        //adapter.notifyDataSetChanged();
//        adapter = new AdapterHocSinh(DSSV.this, R.layout.activity_dssv_ds, arrayHocSinh, null);
//        lvHocSinh.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.buttonThem){
            ThemSinhVien();
        }
        return super.onOptionsItemSelected(item);
    }


    private  void ThemSinhVien(){
        Dialog dialog = new Dialog(this);
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
                    Toast.makeText(DSSV.this, "Vui lòng nhập không để trống!", Toast.LENGTH_SHORT).show();
                }else {
                   // database.QueryData("INSERT INTO " + DBHelper.TB_HOCSINH+" VALUES ( "+DBHelper.COL_HOCSINH_MAHOCSINH+" = '"+mhs +"', " +
                       //     " "+DBHelper.COL_HOCSINH_HO+" ='"+ho+"', "+DBHelper.COL_HOCSINH_TEN+" ='"+ten+"', "+DBHelper.COL_HOCSINH_PHAI+" ='"+phai+"', "+DBHelper.COL_HOCSINH_NGAYSINH+" ='"+ngaysinh+"')");
                    Toast.makeText(DSSV.this, "Đã Thêm", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataHocSinh();
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
        Dialog dialog = new Dialog(this);
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
            private SQLiteOpenHelper database;

            @Override
            public void onClick(View view) {
                String tenMoi = editTen.getText().toString().trim();
                String hoMoi = editHo.getText().toString().trim();
                String phaiMoi = editPhai.getText().toString().trim();
                String nSinhMoi = editNSinh.getText().toString().trim();
                //DSSV.this.database.QueryData("UPDATE FROM "+DBHelper.TB_HOCSINH +" SET  "+DBHelper.COL_HOCSINH_TEN+" = '"+ hoMoi+"', "+DBHelper.COL_HOCSINH_TEN+" = '"+ tenMoi+"'," +
                  //      " "+DBHelper.COL_HOCSINH_PHAI+" = '"+ phaiMoi+"' , "+DBHelper.COL_HOCSINH_NGAYSINH+" = '"+ nSinhMoi+"'  WHERE "+DBHelper.COL_HOCSINH_MAHOCSINH+" = '"+ MaHSMoi +"' ");
                Toast.makeText(DSSV.this, "ĐÃ CẬP NHẬT", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataHocSinh();
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
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa học sinh " + ten + " không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM "+DBHelper.TB_HOCSINH +" WHERE "+ DBHelper.COL_HOCSINH_MAHOCSINH+" = '" + MaHS + "'");
                Toast.makeText(DSSV.this, "Đã xóa " + ten, Toast.LENGTH_SHORT).show();
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
}