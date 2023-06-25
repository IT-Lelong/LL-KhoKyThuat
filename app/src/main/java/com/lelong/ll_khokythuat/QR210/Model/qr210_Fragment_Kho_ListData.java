package com.lelong.ll_khokythuat.QR210.Model;

import java.io.Serializable;

public class qr210_Fragment_Kho_ListData implements Serializable {
    private String tv_fm_kho_hangmuc,tv_fm_kho_kho,tv_fm_kho_vitri,tv_fm_kho_solo,tv_fm_kho_soluong,tv_fm_kho_donvi;
    boolean checkBox = false;

    public qr210_Fragment_Kho_ListData(String tv_fm_kho_hangmuc, String tv_fm_kho_kho, String tv_fm_kho_vitri, String tv_fm_kho_solo, String tv_fm_kho_soluong, String tv_fm_kho_donvi, boolean checkBox) {
        this.tv_fm_kho_hangmuc = tv_fm_kho_hangmuc;
        this.tv_fm_kho_kho = tv_fm_kho_kho;
        this.tv_fm_kho_vitri = tv_fm_kho_vitri;
        this.tv_fm_kho_solo = tv_fm_kho_solo;
        this.tv_fm_kho_soluong = tv_fm_kho_soluong;
        this.tv_fm_kho_donvi = tv_fm_kho_donvi;
        this.checkBox = checkBox;
    }

    public String getTv_fm_kho_hangmuc() {
        return tv_fm_kho_hangmuc;
    }

    public void setTv_fm_kho_hangmuc(String tv_fm_kho_hangmuc) {
        this.tv_fm_kho_hangmuc = tv_fm_kho_hangmuc;
    }

    public String getTv_fm_kho_kho() {
        return tv_fm_kho_kho;
    }

    public void setTv_fm_kho_kho(String tv_fm_kho_kho) {
        this.tv_fm_kho_kho = tv_fm_kho_kho;
    }

    public String getTv_fm_kho_vitri() {
        return tv_fm_kho_vitri;
    }

    public void setTv_fm_kho_vitri(String tv_fm_kho_vitri) {
        this.tv_fm_kho_vitri = tv_fm_kho_vitri;
    }

    public String getTv_fm_kho_solo() {
        return tv_fm_kho_solo;
    }

    public void setTv_fm_kho_solo(String tv_fm_kho_solo) {
        this.tv_fm_kho_solo = tv_fm_kho_solo;
    }

    public String getTv_fm_kho_soluong() {
        return tv_fm_kho_soluong;
    }

    public void setTv_fm_kho_soluong(String tv_fm_kho_soluong) {
        this.tv_fm_kho_soluong = tv_fm_kho_soluong;
    }

    public String getTv_fm_kho_donvi() {
        return tv_fm_kho_donvi;
    }

    public void setTv_fm_kho_donvi(String tv_fm_kho_donvi) {
        this.tv_fm_kho_donvi = tv_fm_kho_donvi;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
}
