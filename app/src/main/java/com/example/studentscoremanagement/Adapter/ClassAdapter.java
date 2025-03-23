package com.example.studentscoremanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studentscoremanagement.Model.Lop;
import com.example.studentscoremanagement.R;
import com.example.studentscoremanagement.fragment.ClassManagementFragment;

import java.util.ArrayList;

public class ClassAdapter extends ArrayAdapter<Lop> {
    private Context context;
    private int layout;
    private ArrayList<Lop> classList;
    private ClassManagementFragment fragment;

    public ClassAdapter(Context context, int resource, ArrayList<Lop> objects, ClassManagementFragment fragment) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.classList = objects;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        }

        TextView txtMaLop = convertView.findViewById(R.id.textViewMaLop);
        TextView txtChuNhiem = convertView.findViewById(R.id.textViewChuNhiem);
        ImageView imgEdit = convertView.findViewById(R.id.imageViewEdit);
        ImageView imgDelete = convertView.findViewById(R.id.imageViewDelete);

        Lop lop = classList.get(position);
        txtMaLop.setText(lop.getMaLop());
        txtChuNhiem.setText(lop.getChuNhiem());

        imgEdit.setOnClickListener(v -> fragment.showEditClassDialog(lop));
        imgDelete.setOnClickListener(v -> fragment.showDeleteClassDialog(lop.getMaLop(), lop.getChuNhiem()));

        return convertView;
    }
}