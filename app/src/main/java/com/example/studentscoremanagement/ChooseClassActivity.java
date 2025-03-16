package com.example.studentscoremanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentscoremanagement.Model.Lop;
import com.example.studentscoremanagement.Model.TaiKhoan;

import java.util.ArrayList;

public class ChooseClassActivity extends AppCompatActivity {
    ArrayList<String> dataString = new ArrayList<>();

    Button btnConfirm;
    ImageButton ibtInfor;
    Spinner spnClass;
    DBHelper database;

    public static final String ID_CLASS="ID_CLASS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);

        addControl();
        setEvent();
    }

    private void setEvent() {
        Initialize();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataString);
        spnClass.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(ChooseClassActivity.this, "Lớp: "+ spnClass.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseClassActivity.this, DSSV.class);
                intent.putExtra(ID_CLASS,spnClass.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        ibtInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tempIntent = getIntent();
                Bundle tempBundle = tempIntent.getBundleExtra(LoginActivity.USER_INFOR);
                TaiKhoan user = new TaiKhoan(tempBundle.getString(DBHelper.COL_TAIKHOAN_TEN), tempBundle.getString(DBHelper.COL_TAIKHOAN_MATKHAU),
                        tempBundle.getString(DBHelper.COL_TAIKHOAN_SDT));
//                Log.d("print", "choose class activity " + tempIntent.getByteArrayExtra(DBHelper.COL_TAIKHOAN_ANH));
                Intent intent = new Intent(ChooseClassActivity.this, UserInforActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString(DBHelper.COL_TAIKHOAN_TEN,user.getTenTaiKhoan());
                bundle.putString(DBHelper.COL_TAIKHOAN_MATKHAU,user.getMatKhau());
                bundle.putString(DBHelper.COL_TAIKHOAN_SDT,user.getSdt());
//                bundle.putByteArray(DBHelper.COL_TAIKHOAN_ANH,user.getAnh());
                intent.putExtra(LoginActivity.USER_INFOR,bundle);
                startActivity(intent);

//                intent.putExtra(LoginActivity.USER_INFOR,bundle);
//
//                startActivity(intent);
            }
        });
    }

    private void Initialize() {
        database=new DBHelper(this);
        ArrayList<String> classes = Lop.getAllClassId(database);
        for (int i = 0; i < classes.size(); i++) {
            dataString.add(classes.get(i));
        }
    }

    private void addControl() {
        btnConfirm = findViewById(R.id.btnConfirm);
        ibtInfor = findViewById(R.id.ibtInfor);
        spnClass = findViewById(R.id.spnClass);
    }

}