package com.example.demo3_asm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTaiKhoan extends BaseAdapter {
    Activity context;
    ArrayList<ModalUser> list;

    public AdapterTaiKhoan(Activity context, ArrayList<ModalUser> list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.itemtaikhoan,null);
        TextView txtUserlv = row.findViewById(R.id.txtlvuser);
        TextView txtPasslv = row.findViewById(R.id.txtlvpass);
        ModalUser md = list.get(i);
        txtUserlv.setText(md.User);
        txtPasslv.setText(md.Passs);
        return row;
    }
}
