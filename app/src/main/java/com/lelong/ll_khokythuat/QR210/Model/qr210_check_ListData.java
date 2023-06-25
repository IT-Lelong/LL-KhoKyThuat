package com.lelong.ll_khokythuat.QR210.Model;

import java.io.Serializable;

public class qr210_check_ListData implements Serializable {
    int inb03;
    private String inb04,ta_ima02_1,ta_ima021_1;
    private String inb05,inb06,inb07;
    int img10,inb09;
    String inb08;
    private boolean scan;

    public qr210_check_ListData(int inb03, String inb04, String ta_ima02_1, String ta_ima021_1, String inb05, String inb06, String inb07, int img10, int inb09, String inb08, boolean scan) {
        this.inb03 = inb03;
        this.inb04 = inb04;
        this.ta_ima02_1 = ta_ima02_1;
        this.ta_ima021_1 = ta_ima021_1;
        this.inb05 = inb05;
        this.inb06 = inb06;
        this.inb07 = inb07;
        this.img10 = img10;
        this.inb09 = inb09;
        this.inb08 = inb08;
        this.scan = scan;
    }

    public int getInb03() {
        return inb03;
    }

    public void setInb03(int inb03) {
        this.inb03 = inb03;
    }

    public String getInb04() {
        return inb04;
    }

    public void setInb04(String inb04) {
        this.inb04 = inb04;
    }

    public String getTa_ima02_1() {
        return ta_ima02_1;
    }

    public void setTa_ima02_1(String ta_ima02_1) {
        this.ta_ima02_1 = ta_ima02_1;
    }

    public String getTa_ima021_1() {
        return ta_ima021_1;
    }

    public void setTa_ima021_1(String ta_ima021_1) {
        this.ta_ima021_1 = ta_ima021_1;
    }

    public String getInb05() {
        return inb05;
    }

    public void setInb05(String inb05) {
        this.inb05 = inb05;
    }

    public String getInb06() {
        return inb06;
    }

    public void setInb06(String inb06) {
        this.inb06 = inb06;
    }

    public String getInb07() {
        return inb07;
    }

    public void setInb07(String inb07) {
        this.inb07 = inb07;
    }

    public int getImg10() {
        return img10;
    }

    public void setImg10(int img10) {
        this.img10 = img10;
    }

    public int getInb09() {
        return inb09;
    }

    public void setInb09(int inb09) {
        this.inb09 = inb09;
    }

    public String getInb08() {
        return inb08;
    }

    public void setInb08(String inb08) {
        this.inb08 = inb08;
    }

    public boolean isScan() {
        return scan;
    }

    public void setScan(boolean scan) {
        this.scan = scan;
    }
}
