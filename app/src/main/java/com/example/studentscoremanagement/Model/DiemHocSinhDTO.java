package com.example.studentscoremanagement.Model;

import java.util.ArrayList;

public class DiemHocSinhDTO {
    private HocSinh hocSinh;
    private ArrayList<DiemMonHocDTO> diemMonHocDTOS;

    public DiemHocSinhDTO() {
    }

    public DiemHocSinhDTO(HocSinh hocSinh) {
        this.hocSinh=hocSinh;
    }

    public DiemHocSinhDTO(HocSinh hocSinh, ArrayList<DiemMonHocDTO> diemMonHocDTOS) {
        this.hocSinh = hocSinh;
        this.diemMonHocDTOS = diemMonHocDTOS;
    }

    public HocSinh getHocSinh() {
        return hocSinh;
    }

    public ArrayList<DiemMonHocDTO> getDiemMonHocDTOS() {
        return diemMonHocDTOS;
    }

    public void setHocSinh(HocSinh hocSinh) {
        this.hocSinh = hocSinh;
    }

    public void setDiemMonHocDTOS(ArrayList<DiemMonHocDTO> diemMonHocDTOS) {
        this.diemMonHocDTOS = diemMonHocDTOS;
    }
}
