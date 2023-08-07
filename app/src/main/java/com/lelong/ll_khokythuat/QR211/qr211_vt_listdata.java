package com.lelong.ll_khokythuat.QR211;

public class qr211_vt_listdata {
    String img02;
    String img03;
    String img01;
    String ten;
    double ima27;
    double img10;


    public void setImg02(String img02) {
        this.img02 = img02;
    }

    public void setImg03(String img03) {
        this.img03 = img03;
    }

    public void setImg01(String img01) {
        this.img01 = img01;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setIma27(double ima27) {
        this.ima27 = ima27;
    }

    public void setImg10(double img10) {
        this.img10 = img10;
    }

    public qr211_vt_listdata(String img02, String img03, String img01, String ten, double ima27, double img10) {
        this.img02 = img02;
        this.img03 = img03;
        this.img01 = img01;
        this.ten = ten;
        this.ima27 = ima27;
        this.img10 = img10;

        //this.checkBox = checkBox;
    }
    public String getImg02() {
        return img02;
    }

    public String getImg03() {
        return img03;
    }

    public String getImg01() {
        return img01;
    }

    public String getTen() {
        return ten;
    }

    public double getIma27() {
        return ima27;
    }

    public double getImg10() {
        return img10;
    }

   // public void setUses(int uses) {this.uses = uses;}


}

