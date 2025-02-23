package com.example.demo3_asm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdapterLopHoc extends BaseAdapter {
    Activity context;
    List<ModalLopHoc> objects;
    ArrayList<ModalLopHoc> listLopHoc;
    TextView txtMaLop,txtTenLop,txtStt;
    ImageView btnEditLop,btnDeleteLop;

    public AdapterLopHoc(Activity context, ArrayList<ModalLopHoc> list) {
        this.context = context;
        this.listLopHoc = list;
    }

    @Override
    public int getCount() {
        return listLopHoc.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowlop = inflater.inflate(R.layout.itemlophoc,null);
        txtMaLop = rowlop.findViewById(R.id.txtmalop);
        txtTenLop = rowlop.findViewById(R.id.txttenlop);
        txtStt = rowlop.findViewById(R.id.txtsttlop);
//        btnEditLop = rowlop.findViewById(R.id.btneditlopadap);
        btnDeleteLop = rowlop.findViewById(R.id.btndeletelop);

        final ModalLopHoc  lh = listLopHoc.get(position);

        txtStt.setText(1+position+"");
        txtTenLop.setText(lh.TenLopHoc);
        txtMaLop.setText(lh.MaLopHoc);
        btnDeleteLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn muốn xóa lớp '"+ lh.getTenLopHoc() +"?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        xoaLop(lh);
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


        return rowlop;
    }
    public void xoaLop(ModalLopHoc lh){
        SQLiteDatabase database = Database.initDatabase(context,"qlsv.sqlite");
        int kq = database.delete("tbLopHoc","colMaLop=?",new String[]{lh.MaLopHoc});
        if (kq==0){
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
        }
        Cursor cursor = database.rawQuery("select * from tbLopHoc",null);
        listLopHoc.clear();
        while (cursor.moveToNext()){
            String malop = cursor.getString(0);
            String tenlop = cursor.getString(1);

            listLopHoc.add(new ModalLopHoc(malop, tenlop));
        }
        notifyDataSetChanged();

    }


}
