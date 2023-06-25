package com.lelong.ll_khokythuat.QR210.Model;

public class qr210_vt_listData {
    String img01;
    String ima02;
    String ta_ima02_1;
    String ta_ima021_1;
    String img02;
    String img03;
    String img04;
    String img09;
    int img10;
    int uses;
    //boolean checkBox = false;

    //public qr210_vt_listData(String img01, String ima02, String ta_ima02_1, String ta_ima021_1, String img02, String img03, String img04, String img09, int img10, int uses, boolean checkBox) {
    public qr210_vt_listData(String img01, String ima02, String ta_ima02_1, String ta_ima021_1, String img02, String img03, String img04, String img09, int img10, int uses) {
        this.img01 = img01;
        this.ima02 = ima02;
        this.ta_ima02_1 = ta_ima02_1;
        this.ta_ima021_1 = ta_ima021_1;
        this.img02 = img02;
        this.img03 = img03;
        this.img04 = img04;
        this.img09 = img09;
        this.img10 = img10;
        this.uses = uses;
        //this.checkBox = checkBox;
    }

    public String getImg01() {
        return img01;
    }

    public String getIma02() {
        return ima02;
    }

    public String getTa_ima02_1() {
        return ta_ima02_1;
    }

    public String getTa_ima021_1() {
        return ta_ima021_1;
    }

    public String getImg02() {
        return img02;
    }

    public String getImg03() {
        return img03;
    }

    public String getImg04() {
        return img04;
    }

    public String getImg09() {
        return img09;
    }

    public int getImg10() {
        return img10;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }
    /*public boolean isSelected() {
        return checkBox;
    }*/

/*public void setChecked(boolean checkBox) {
        this.checkBox = checkBox;
    }*/


}
