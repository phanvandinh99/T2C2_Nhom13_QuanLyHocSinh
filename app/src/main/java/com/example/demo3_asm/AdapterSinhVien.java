package com.example.demo3_asm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class AdapterSinhVien extends BaseAdapter {
    Activity context;
    ArrayList<ModalSinhVien> list;
    ModalSinhVien md = new ModalSinhVien();


    public AdapterSinhVien(Activity context, ArrayList<ModalSinhVien> list) {
        this.context = context;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.itemsinhvien,null);
        ImageView imgHinh = row.findViewById(R.id.imghinhsv);
        TextView txtMaSv = row.findViewById(R.id.txtmasv);
        final TextView txtTenSv  = row.findViewById(R.id.txttensv);
        TextView txtMaLopSv = row.findViewById(R.id.txtmalopsv);
        final TextView txtSdtSv = row.findViewById(R.id.txtsdtsv);
        TextView txtEmail = row.findViewById(R.id.txtemailsv);
        ImageButton btnGoi = row.findViewById(R.id.btngoisv);
        ImageButton btnNhanTin = row.findViewById(R.id.btnnhantinsv);
        ImageButton btnSuaSv = row.findViewById(R.id.btneditsv);
        ImageButton btnXoaSv = row.findViewById(R.id.btnxoasv);

        final ModalSinhVien modalSinhVien = list.get(position);

        txtMaSv.setText(modalSinhVien.MaSv+"");
        txtTenSv.setText(modalSinhVien.TenSv+"");
        txtMaLopSv.setText(modalSinhVien.MaLopSv+"");
        txtSdtSv.setText(modalSinhVien.SdtSv+"");
        txtEmail.setText(modalSinhVien.EmailSv+"");

        Bitmap bmHinhSv = (Bitmap) BitmapFactory.decodeByteArray(modalSinhVien.HinhSv,0,modalSinhVien.HinhSv.length);
        imgHinh.setImageBitmap(bmHinhSv);

        btnSuaSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ActivityUpdateSinhVien.class);
                intent.putExtra("masv",modalSinhVien.MaSv);
                context.startActivity(intent);

            }
        });
        btnXoaSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn muốn xóa '"+ txtTenSv.getText().toString()+"' ra khỏi danh sách sinh viên không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSv(modalSinhVien.MaSv);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        btnNhanTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogsms = new Dialog(context);
                dialogsms.setContentView(R.layout.dialogsms);

                TextView txtDgSmsTenSv;
                final EditText edtDgSmsSdt,edtDgSmsNoiDung;
                Button btnDgSmsGui = dialogsms.findViewById(R.id.btndgsmgui);
                Button btnDgSmsBack = dialogsms.findViewById(R.id.btndgsmback);

                txtDgSmsTenSv = dialogsms.findViewById(R.id.txtdgsmstensv);
                edtDgSmsSdt = dialogsms.findViewById(R.id.edtdgsmssdt);
                edtDgSmsNoiDung = dialogsms.findViewById(R.id.edtdgsmsnoidung);

                txtDgSmsTenSv.setText(txtTenSv.getText().toString());
                edtDgSmsSdt.setText(txtSdtSv.getText().toString());

                dialogsms.show();

                btnDgSmsGui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.putExtra("sms_body",edtDgSmsNoiDung.getText().toString());
                        intent.setData(Uri.parse("sms:"+edtDgSmsSdt.getText().toString()));

                        context.startActivity(intent);


                    }
                });
                btnDgSmsBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogsms.dismiss();
                    }
                });

            }
        });

        btnGoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogcall);
                TextView txtdgcalten = dialog.findViewById(R.id.txtdgcallten);
                final EditText edtdgcallsdt = dialog.findViewById(R.id.edtdgcallsdt);
                Button btndgcallgoi = dialog.findViewById(R.id.btndgcallgoi);
                Button btndgcallback = dialog.findViewById(R.id.btndgcallback);

                txtdgcalten.setText(txtTenSv.getText().toString());
                edtdgcallsdt.setText(txtSdtSv.getText().toString());

                btndgcallgoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkPermission(Manifest.permission.CALL_PHONE)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + edtdgcallsdt.getText().toString()));
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context,"Permission call phone",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btndgcallback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return row;
    }
    private boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_GRANTED;
    }

    private void showDialogSms() {

    }

    private void deleteSv(String MaSv) {
        //ActivityQuanLySinhVien ac = new ActivityQuanLySinhVien();
        SQLiteDatabase database = Database.initDatabase(context,"qlsv.sqlite");
        database.delete("tbSinhVien","colMaSV=?",new String[]{MaSv});
        Cursor cursor = database.rawQuery("select * from tbSinhVien",null);
        list.clear();
        while (cursor.moveToNext()){
            String masv = cursor.getString(0);
            String tensv = cursor.getString(1);
            String malopsv = cursor.getString(2);
            String sdtsv = cursor.getString(3);
            String emailsv = cursor.getString(4);
            byte[] hinhdaidiensv = cursor.getBlob(5);

            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhdaidiensv, 0, hinhdaidiensv.length);
            list.add(new ModalSinhVien(masv, tensv, malopsv, sdtsv, emailsv, hinhdaidiensv));
        }
        notifyDataSetChanged();
        //ac.readDataSinhVien();
    }
}
