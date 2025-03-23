package com.example.studentscoremanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studentscoremanagement.Model.MonHoc;
import com.example.studentscoremanagement.R;
import com.example.studentscoremanagement.fragment.SubjectManagementFragment;

import java.util.ArrayList;

public class SubjectAdapter extends ArrayAdapter<MonHoc> {
    private Context context;
    private int layout;
    private ArrayList<MonHoc> subjectList;
    private SubjectManagementFragment fragment;

    public SubjectAdapter(Context context, int resource, ArrayList<MonHoc> objects, SubjectManagementFragment fragment) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.subjectList = objects;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        }

        TextView txtMaMH = convertView.findViewById(R.id.textViewMaMH);
        TextView txtTenMH = convertView.findViewById(R.id.textViewTenMH);
        TextView txtHeSo = convertView.findViewById(R.id.textViewHeSo);
        ImageView imgEdit = convertView.findViewById(R.id.imageViewEdit);
        ImageView imgDelete = convertView.findViewById(R.id.imageViewDelete);

        MonHoc monHoc = subjectList.get(position);
        txtMaMH.setText(monHoc.getMaMH());
        txtTenMH.setText(monHoc.getTenMH());
        txtHeSo.setText(String.valueOf(monHoc.getHeSo()));

        imgEdit.setOnClickListener(v -> fragment.showEditSubjectDialog(monHoc));
        imgDelete.setOnClickListener(v -> fragment.showDeleteSubjectDialog(monHoc.getMaMH(), monHoc.getTenMH()));

        return convertView;
    }
}