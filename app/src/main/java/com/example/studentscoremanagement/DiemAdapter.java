package com.example.studentscoremanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentscoremanagement.Model.DiemMonHocDTO;
import java.util.List;

public class DiemAdapter extends RecyclerView.Adapter<DiemAdapter.ViewHolder> {
    private List<DiemMonHocDTO> diemMonHocList;

    public DiemAdapter(List<DiemMonHocDTO> diemMonHocList) {
        this.diemMonHocList = diemMonHocList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mon_hoc_diem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiemMonHocDTO diemMonHoc = diemMonHocList.get(position);
        holder.textViewTenMonHoc.setText(diemMonHoc.getTenMH());
        holder.editTextDiem.setText(diemMonHoc.getDiem() > 0 ? String.valueOf(diemMonHoc.getDiem()) : "");
        holder.editTextDiem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                try {
                    float diem = s.toString().isEmpty() ? 0 : Float.parseFloat(s.toString());
                    diemMonHoc.setDiem(diem);
                } catch (NumberFormatException e) {
                    diemMonHoc.setDiem(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return diemMonHocList.size();
    }

    public List<DiemMonHocDTO> getDiemMonHocList() {
        return diemMonHocList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTenMonHoc;
        EditText editTextDiem;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTenMonHoc = itemView.findViewById(R.id.textViewTenMonHoc);
            editTextDiem = itemView.findViewById(R.id.editTextDiem);
        }
    }
}