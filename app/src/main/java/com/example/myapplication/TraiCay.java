package com.example.myapplication;

import java.io.Serializable;

public class TraiCay implements Serializable {
    private String Ten;
    private String MoTa;
    private  String Hinh;

    public TraiCay() {}

    public TraiCay(String ten, String moTa, String hinh) {
        Ten = ten;
        MoTa = moTa;
        Hinh = hinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }
}
