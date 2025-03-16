package com.example.studentscoremanagement.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.studentscoremanagement.Model.DiemHocSinhDTO;
import com.example.studentscoremanagement.Model.DiemMonHocDTO;
import com.example.studentscoremanagement.Model.HocSinh;
import com.example.studentscoremanagement.R;

import java.util.ArrayList;

public class ReportItemAdapter extends ArrayAdapter<DiemHocSinhDTO> {

    Context context;
    int layoutId;
    ArrayList<DiemHocSinhDTO> diemHocSinhDTOS;


    public ReportItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DiemHocSinhDTO> objects) {
        super(context, resource, objects);
        this.context=context;
        this.layoutId=resource;
        this.diemHocSinhDTOS=objects;
    }

    @Override
    public int getCount() {
        return diemHocSinhDTOS.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Ánh xạ
        convertView= LayoutInflater.from(context).inflate(layoutId,null);
        TextView tvSTTRpt=convertView.findViewById(R.id.tvSTTRpt);
        TextView tvIdStudentRpt=convertView.findViewById(R.id.tvIdStudentRpt);
        TextView tvNameStudentRpt=convertView.findViewById(R.id.tvNameStudentRpt);
        TextView tvGenderStudentRpt=convertView.findViewById(R.id.tvGenderStudentRpt);
        TextView tvBirthStudentRpt=convertView.findViewById(R.id.tvBirthStudentRpt);
        TextView tvSumSB=convertView.findViewById(R.id.tvSumSubject);
        TextView tvAvg=convertView.findViewById(R.id.tvAvg);
        TableLayout tbListScore=convertView.findViewById(R.id.tbListScore);


        //set data hs
        DiemHocSinhDTO diemHocSinhDTO=diemHocSinhDTOS.get(position);
        tvSTTRpt.setText(String.valueOf(position+1));
        tvIdStudentRpt.setText(diemHocSinhDTO.getHocSinh().getMaHS());
        tvNameStudentRpt.setText(diemHocSinhDTO.getHocSinh().getHo()+" "+diemHocSinhDTO.getHocSinh().getTen());
        tvGenderStudentRpt.setText(diemHocSinhDTO.getHocSinh().getPhai());
        tvBirthStudentRpt.setText(diemHocSinhDTO.getHocSinh().getNgaySinh());


        if(getCount()>0){
            Log.d("print","học sinh: "+diemHocSinhDTO.getHocSinh().getTen());
        }
        //set data cho tableLayout mon hoc
        ArrayList<DiemMonHocDTO> diemMonHocDTOs=diemHocSinhDTO.getDiemMonHocDTOS();
        int sttSubjectScore=1;
        float scoreAvg=0;
        int heso=0;
        for(DiemMonHocDTO diem:diemMonHocDTOs){
            TextView tvSTTSubject=new TextView(context);
            TextView tvSubjectName=new TextView(context);
            TextView tvHeSo=new TextView(context);
            TextView tvScore=new TextView(context);
            TableRow tableRow=new TableRow(context);

            //set STT
            tvSTTSubject.setText(String.valueOf(sttSubjectScore));
            tvSTTSubject.setGravity(Gravity.CENTER);

            //set tên MH
            tvSubjectName.setText(diem.getTenMH());

            //set Heso
            tvHeSo.setText(String.valueOf(diem.getHeSo()));
            tvHeSo.setGravity(Gravity.CENTER);

            //set Diem
            String score=(diem.getDiem()==-1)?".":String.valueOf(diem.getDiem());
            tvScore.setText(score);
            tvScore.setGravity(Gravity.CENTER);

            tableRow.addView(tvSTTSubject);
            tableRow.addView(tvSubjectName);
            tableRow.addView(tvHeSo);
            tableRow.addView(tvScore);

            //set custom style
            Drawable drawableBackground= AppCompatResources.getDrawable(context, R.drawable.custom_table_report);
            tableRow.setBackground(drawableBackground);
            tableRow.setPadding(10,10,10,10);

            //add Row
            tbListScore.addView(tableRow);
            sttSubjectScore++;
            heso+=diem.getHeSo();
            scoreAvg+=diem.getDiem()==-1?0:(diem.getDiem()*diem.getHeSo());

        }
        scoreAvg=(float) Math.round((scoreAvg/heso) * 10) / 10;

        tvSumSB.setText("Tổng số môn học: "+diemMonHocDTOs.size());
        tvAvg.setText("Điểm trung bình: "+scoreAvg);
        return convertView;
    }
}
