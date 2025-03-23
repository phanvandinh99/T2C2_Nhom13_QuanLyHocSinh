package com.example.studentscoremanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.example.studentscoremanagement.Model.HocSinh;
import com.example.studentscoremanagement.R;
import com.example.studentscoremanagement.StudentEditorActivity;
import com.example.studentscoremanagement.fragment.DSSVFragment;
import java.util.ArrayList;

public class AdapterHocSinh extends ArrayAdapter<HocSinh> {
    Context context;
    int layout;
    ArrayList<HocSinh> hocSinhList;
    FragmentActivity dssvActivity;
    DSSVFragment dssvFragment;

    public AdapterHocSinh(@NonNull Context context, int resource, @NonNull ArrayList<HocSinh> objects, FragmentActivity dssv, DSSVFragment dssvFragment) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.hocSinhList = objects;
        this.dssvActivity = dssv;
        this.dssvFragment = dssvFragment;
    }

    @Override
    public int getCount() {
        return hocSinhList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout, null);

        ImageView imgDelete = convertView.findViewById(R.id.imageViewDelete);
        ImageView imgEdit = convertView.findViewById(R.id.imageViewEdit);

        TextView txtid = convertView.findViewById(R.id.textViewMSHS);
        TextView txtHo = convertView.findViewById(R.id.textViewHO);
        TextView txtTen = convertView.findViewById(R.id.textViewTEN);
        TextView txtPhai = convertView.findViewById(R.id.textViewPHAI);
        TextView txtNgaySinh = convertView.findViewById(R.id.textViewNS);

        HocSinh hocSinh = hocSinhList.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Giữ nguyên logic mở StudentManagerFragment nếu cần
            }
        });

        txtid.setText(hocSinh.getMaHS());
        txtHo.setText(hocSinh.getHo());
        txtTen.setText(hocSinh.getTen());
        txtPhai.setText(hocSinh.getPhai());
        txtNgaySinh.setText(hocSinh.getNgaySinh());

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentEditorActivity.class);
                intent.putExtra("maHS", Integer.parseInt(hocSinh.getMaHS()));
                context.startActivity(intent);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dssvFragment.DialogXoa(hocSinh.getTen(), hocSinh.getMaHS());
            }
        });

        return convertView;
    }
}