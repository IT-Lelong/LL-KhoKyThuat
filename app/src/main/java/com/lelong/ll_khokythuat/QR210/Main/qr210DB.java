package com.lelong.ll_khokythuat.QR210.Main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class qr210DB {
    public SQLiteDatabase db = null;
    private final static String DATABASE_NAME = "qr210DB.db";
    private final static String TABLE_NAME1 = "qra_table";
    private final static String TABLE_NAME2 = "qrb_table";
    private final static String TABLE_NAME3 = "img_table";
    private final static String TABLE_NAME4 = "del_table";


    private final static String qraprog = "qraprog"; //單頭程式
    private final static String qra01 = "qra01"; //單據
    private final static String qra02 = "qra02"; //單據日期
    private final static String qra03 = "qra03"; //部門編號
    private final static String qra04 = "qra04"; //部門名稱
    private final static String qra05 = "qra05"; //單據過帳狀況

    private final static String qrbprog = "qrbprog"; //單身程式
    private final static String qrb01 = "qrb01"; //單據
    private final static String qrb02 = "qrb02"; //項目
    private final static String qrb03 = "qrb03"; //料號
    private final static String qrb04 = "qrb04"; //品名
    private final static String qrb05 = "qrb05"; //規格
    private final static String qrb06 = "qrb06"; //倉庫
    private final static String qrb07 = "qrb07"; //儲位
    private final static String qrb08 = "qrb08"; //批號
    private final static String qrb09 = "qrb09"; //發料數量
    private final static String qrb10 = "qrb10"; //單位
    private final static String qrb11 = "qrb11"; //打鉤
    private final static String qrb12 = "qrb12"; //倉庫掃描策略
    private final static String qrb13 = "qrb13"; //庫存數量


    private final static String imgprog = "imgprog"; //程式
    private final static String imgmct = "imgmct"; //單據
    private final static String img01 = "img01"; //料號
    private final static String imgten = "imgten"; //名稱
    private final static String imgqc = "imgqc"; //規格
    private final static String img02 = "img02"; //倉庫
    private final static String img03 = "img03"; //儲位
    private final static String img04 = "img04"; //批號
    private final static String img09 = "img09"; //單位
    private final static String img10 = "img10"; //數量
    private final static String imgsachluoc = "imgsachluoc"; //倉庫掃描策略

    private final static String dprog = "dprog"; //單據程式
    private final static String dmct = "dmct"; //單據
    private final static String d01 = "d01"; //已刪除的筆資料項目

    private final static String j01 = "j01"; //工單單號
    private final static String j02 = "j02"; //料號
    private final static String j03 = "j03"; //數量
    private final static String j04 = "j04"; //上傳人員
    private final static String j05 = "j05"; //發料單號
    private final static String j06 = "j06"; //移轉單號
    private final static String j07 = "j07"; //入庫單號


    private final static String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + " (" + qraprog + " TEXT," + qra01 + " TEXT PRIMARY KEY," + qra02 + " DATE," + qra03 + " TEXT," + qra04 + " TEXT," + qra05 + " TEXT)";

    private final static String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + " (" + qrbprog + " TEXT," + qrb01 + " TEXT ," + qrb02 + " INTEGER ," + qrb03 + " TEXT," + qrb04 + " TEXT," + qrb05 + " TEXT,"
            + qrb06 + " TEXT," + qrb07 + " TEXT," + qrb08 + " TEXT," + qrb09 + " INTEGER," + qrb10 + " TEXT," + qrb11 + " TEXT," + qrb12 + " TEXT," + qrb13 + " INTEGER,"
            + " PRIMARY KEY(qrb01,qrb02)" + ")";

    private final static String CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + " (" + imgprog + " TEXT," + imgmct + " TEXT," + img01 + " TEXT," + imgten + " TEXT," + imgqc + " TEXT,"
            + img02 + " TEXT," + img03 + " TEXT," + img04 + " TEXT," + img09 + " TEXT," + img10 + " INTEGER," + imgsachluoc + " TEXT,"
            + " PRIMARY KEY(imgmct,img02,img03,img04)" + ")";

    private final static String CREATE_TABLE4 = "CREATE TABLE " + TABLE_NAME4 + " (" + dprog + " TEXT," + dmct + " TEXT," + d01 + " TEXT, "
            + " PRIMARY KEY(dmct,d01)" + ")";

    private Context mCtx = null;

    public qr210DB(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() throws SQLException {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
        try {
            db.execSQL(CREATE_TABLE1);
        } catch (Exception e) {
        }
        try {
            db.execSQL(CREATE_TABLE2);
        } catch (Exception e) {
        }
        try {
            db.execSQL(CREATE_TABLE3);
        } catch (Exception e) {
        }
        try {
            db.execSQL(CREATE_TABLE4);
        } catch (Exception e) {
        }

    }

    public void close() {
        try {
            final String DROP_TABLE1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
            db.execSQL(DROP_TABLE1);
            final String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
            db.execSQL(DROP_TABLE2);
            final String DROP_TABLE3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
            db.execSQL(DROP_TABLE3);
            final String DROP_TABLE4 = "DROP TABLE IF EXISTS " + TABLE_NAME4;
            db.execSQL(DROP_TABLE4);
            db.close();
        } catch (Exception e) {

        }
    }

    ////////////////////////////////INSERT (S)//////////////////////////////////////
    public long ins_img(String xprog, String xmct, String ximg01, String ximgten, String ximgqc,
                        String ximg02, String ximg03, String ximg04, String ximg09, int ximg10, String ximgsachluoc) {
        try {
            ContentValues argsA = new ContentValues();
            argsA.put(imgprog, xprog);
            argsA.put(imgmct, xmct);
            argsA.put(img01, ximg01);
            argsA.put(imgten, ximgten);
            argsA.put(imgqc, ximgqc);
            argsA.put(img02, ximg02);
            argsA.put(img03, ximg03);
            argsA.put(img04, ximg04);
            argsA.put(img09, ximg09);
            argsA.put(img10, ximg10);
            argsA.put(imgsachluoc, ximgsachluoc);
            db.insert(TABLE_NAME3, null, argsA);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public long ins_deltable(String xprog, String xmct, String xd01) {
        try {
            ContentValues argsD = new ContentValues();
            argsD.put(dprog, xprog);
            argsD.put(dmct, xmct);
            argsD.put(d01, xd01);
            db.insert(TABLE_NAME4, null, argsD);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public long append(String xprog, String xqra01, String xqra02, String xqra03, String xqra04,String xqra05,
                       String xqrb01, double xqrb02, String xqrb03, String xqrb04, String xqrb05, String xqrb06,
                       String xqrb07, String xqrb08, double xqrb09, String xqrb10, String xqrb11, String xqrb12, double xqrb13) {
        try {
            ContentValues argsA = new ContentValues();
            ContentValues argsB = new ContentValues();
            argsB.put(qrbprog, xprog);
            argsB.put(qrb01, xqrb01);
            argsB.put(qrb02, xqrb02);
            argsB.put(qrb03, xqrb03);
            argsB.put(qrb04, xqrb04);
            argsB.put(qrb05, xqrb05);
            argsB.put(qrb06, xqrb06);
            argsB.put(qrb07, xqrb07);
            argsB.put(qrb08, xqrb08);
            argsB.put(qrb09, xqrb09);
            argsB.put(qrb10, xqrb10);
            argsB.put(qrb11, xqrb11);
            argsB.put(qrb12, xqrb12);
            argsB.put(qrb13, xqrb13);
            Cursor mCursor = db.query(TABLE_NAME1, new String[]{qra01, qra02, qra03, qra04},
                    qra01 + "=?", new String[]{xqra01}, null, null, null, null);
            if (mCursor.getCount() > 0) {
                //db.execSQL("UPDATE " + TABLE_NAME1 + " SET qra02=qra02+1,qra03="+xqra03+",qra04="+xqra04+" WHERE qra01='" + xqra01 +"'");
                db.insert(TABLE_NAME2, null, argsB);
                return 1;
            } else {
                argsA.put(qraprog, xprog);
                argsA.put(qra01, xqra01);
                argsA.put(qra02, String.valueOf(xqra02));
                argsA.put(qra03, xqra03);
                argsA.put(qra04, xqra04);
                argsA.put(qra04, xqra04);
                argsA.put(qra05, xqra05);
                db.insert(TABLE_NAME1, null, argsA);
                db.insert(TABLE_NAME2, null, argsB);
                return 1;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    ////////////////////////////////INSERT (E)//////////////////////////////////////

    ////////////////////////////////UPDATE (S)//////////////////////////////////////

    public long upd_qrb011(String upd_qrb01, String upd_qrb03, int upd_qrb02) {
        try {
            Cursor mCursor = db.query(TABLE_NAME2, new String[]{qrb01, qrb02, qrb03},
                    qrb01 + "=? AND " + qrb03 + "=? AND " + qrb02 + "=?", new String[]{upd_qrb01, upd_qrb03, String.valueOf(upd_qrb02)}, null, null, null, null);
            if (mCursor.getCount() > 0) {
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb11='" + "true" + "' WHERE qrb01='" + upd_qrb01 + "' AND qrb03='" + upd_qrb03 + "' AND qrb02='" + upd_qrb02 + "' ");
                return 1;
            } else {
                Toast.makeText(mCtx, upd_qrb03 + " 不符合", Toast.LENGTH_LONG).show();
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //Cập nhật lại số lượng qrb09 khi mã sách lược là 3: qrb09 + 1
    public long upd_qrb09(String upd_qrb01, int upd_qrb02) {
        try {
            Cursor mCursor = db.query(TABLE_NAME2, new String[]{qrb01, qrb02, qrb03, qrb09},
                    qrb01 + "=? AND " + qrb02 + "=?", new String[]{upd_qrb01, String.valueOf(upd_qrb02)}, null, null, null, null);
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                int res = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("qrb09")));
                res = res + 1;
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb09='" + res + "' WHERE qrb01='" + upd_qrb01 + "' AND qrb02='" + upd_qrb02 + "' ");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //update lại stt trên giao diện (S)
    public int upd_qrb02(String xprog, String xqrb01) {
        try {
            String TAG = "log_upd";
            Cursor upd_qrb02_cur = getAll_2(xprog);
            upd_qrb02_cur.moveToFirst();
            if (upd_qrb02_cur.getCount() > 0) {
                int qrb02_stt = 1;
                do {
                    Log.d(TAG, "log_upd:  " + upd_qrb02_cur.getString(2) + "  do  " + upd_qrb02_cur.getString(1) + "  " + qrb02_stt);
                    db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb02='" + qrb02_stt + "' WHERE qrbprog='" + xprog + "'  AND qrb01='" + xqrb01 + "' AND qrb02='" + upd_qrb02_cur.getString(3) + "' ");
                    qrb02_stt += 1;
                } while (upd_qrb02_cur.moveToNext());
                {
                }
            }
            upd_qrb02_cur.close();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
    //update lại stt trên giao diện (E)

    public long upd_qrb09(String upd_qrb01, String upd_qrb02, int upd_qrb09) {
        try {
            Cursor mCursor = db.query(TABLE_NAME2, new String[]{qrb01, qrb02, qrb03, qrb09},
                    qrb01 + "=? AND " + qrb02 + "=?", new String[]{upd_qrb01, upd_qrb02}, null, null, null, null);
            if (mCursor.getCount() > 0) {
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb09='" + upd_qrb09 + "' WHERE qrb01='" + upd_qrb01 + "' AND qrb02='" + upd_qrb02 + "' ");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public void upd_qrb_item_data(String g_mct, int max, String g_string_1, String g_string_2, String g_string_3) {
        int re_qrb09 = -1;
        //nếu sách lược quét là 3 thì khi update dữ liệu sl phát mặc định 1 ngược lại 0
        if (g_string_3.equals("3")) {
            re_qrb09 = 1;
        } else {
            re_qrb09 = 0;
        }

        db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb04='" + g_string_1 + "' , qrb05='" + g_string_2 + "' , qrb12='" + g_string_3 + "', qrb09='" + re_qrb09 + "' " +
                " WHERE  qrb01='" + g_mct + "' AND qrb02='" + max + "' ");
    }

    public void upd_qrb_kho(String g_qrb01, int g_qrb02, String g_qrb06, String g_qrb07, String g_qrb08, String g_qrb13, String g_qrb10, String g_qrb12) {
        try {
            if (g_qrb12.equals("3")) {
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb06='" + g_qrb06 + "' , qrb07='" + g_qrb07 + "', qrb08='" + g_qrb08 + "', qrb10='" + g_qrb10 + "', qrb13=" + g_qrb13 + " " +
                        " WHERE  qrb01='" + g_qrb01 + "' AND qrb02='" + g_qrb02 + "' ");
            } else {
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrb06='" + g_qrb06 + "' , qrb07='" + g_qrb07 + "', qrb08='" + g_qrb08 + "', qrb10='" + g_qrb10 + "', qrb13=" + g_qrb13 + ", qrb09=" + 0 + "  " +
                        " WHERE  qrb01='" + g_qrb01 + "' AND qrb02='" + g_qrb02 + "' ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*public int upd_qrbuse(String xprog, String xqrb01, String xqrb02, String xqrb03, String xqrbuse, String xqrb12) {
        if (xqrb12.equals("3")) {
            try {
                //String TAG = "log_upd_qrbuse";
                //Log.d(TAG, noi dung);
                db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrbuse='" + xqrbuse
                        + "' WHERE qrbprog='" + xprog
                        + "'   AND qrb01='" + xqrb01
                        + "'   AND qrb02='" + xqrb02
                        + "'   AND qrb03='" + xqrb03
                        + "' ");
                return 1;
            } catch (Exception e) {
                return 0;
            }
        } else { //策略 = 1,2
            try {
                //String TAG = "log_upd_qrbuse";
                Cursor upd_qrb02_cur = getAll_2(xprog);
                upd_qrb02_cur.moveToFirst();
                if (upd_qrb02_cur.getCount() > 0) {
                    int qrb02_stt = 1;
                    do {
                        //Log.d(TAG, "log_upd:  " + upd_qrb02_cur.getString(2) + "  do  " + upd_qrb02_cur.getString(1) + "  " + qrb02_stt);
                        db.execSQL("UPDATE " + TABLE_NAME2 + " SET qrbuse='" + qrb02_stt
                                + "' WHERE qrbprog='" + xprog
                                + "'  AND qrb01='" + xqrb01
                                + "'  AND qrb02='" + upd_qrb02_cur.getString(1) + "' ");
                        qrb02_stt += 1;
                    } while (upd_qrb02_cur.moveToNext());
                    {
                    }
                }
                upd_qrb02_cur.close();
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }
    }*/

    ////////////////////////////////UPDATE (E)//////////////////////////////////////

    ////////////////////////////////DEL (S)//////////////////////////////////////

    public void del_db(String prog) {
        try {
            final String DEL_TABLE1 = "DELETE FROM " + TABLE_NAME1 + " WHERE qraprog = '" + prog + "'";
            db.execSQL(DEL_TABLE1);
            final String DEL_TABLE2 = "DELETE FROM " + TABLE_NAME2 + " WHERE qrbprog = '" + prog + "'";
            db.execSQL(DEL_TABLE2);
            final String DEL_TABLE3 = "DELETE FROM " + TABLE_NAME3 + " WHERE imgprog = '" + prog + "'";
            db.execSQL(DEL_TABLE3);
            final String DEL_TABLE4 = "DELETE FROM " + TABLE_NAME4 + " WHERE dprog = '" + prog + "'";
            db.execSQL(DEL_TABLE4);
        } catch (Exception e) {
        }
    }

    public int del_db_img(String prog) {
        try {
            final String DEL_TABLE3 = "DELETE FROM " + TABLE_NAME3 + " WHERE imgprog = '" + prog + "'";
            db.execSQL(DEL_TABLE3);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int del_db_deltable(String prog) {
        try {
            final String DEL_TABLE4 = "DELETE FROM " + TABLE_NAME4 + " WHERE dprog = '" + prog + "'";
            db.execSQL(DEL_TABLE4);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int del_img(String xmct, String ximg02, String ximg03, String ximg04, int ximg10) {
        try {
            db.delete(TABLE_NAME3,
                    imgmct + "=? AND " + img02 + "=? AND " + img03 + "=? AND " + img04 + "=? AND " + img10 + "=? ",
                    new String[]{xmct, ximg02, ximg03, ximg04, String.valueOf(ximg10)});
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int del_qrb(String xmct, String xqrb01, String xqrb02) {
        try {
            db.delete(TABLE_NAME2,
                    qrbprog + "=? AND " + qrb01 + "=? AND " + qrb02 + "=? ",
                    new String[]{xmct, xqrb01, xqrb02});
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    ////////////////////////////////DEL (E)//////////////////////////////////////

    ////////////////////////////////QUERY (S)//////////////////////////////////////

    public Cursor getAll_1(String prog) {
        return db.query(TABLE_NAME1, new String[]{"rowid _id", qra01, qra02, qra03, qra04, qra05}
                , qraprog + "=?", new String[]{prog}, null, null, qra01, null);
    }

    public Cursor getAll_2(String prog) {
        return db.query(TABLE_NAME2, new String[]{"rowid _id", qrbprog, qrb01, qrb02, qrb03, qrb04, qrb05, qrb06, qrb07, qrb08, qrb09, qrb10, qrb11, qrb12, qrb13}
                , qrbprog + "=?", new String[]{prog}, null, null, qrb02, null);
    }

    public Cursor getAll_3(String prog) {
        return db.query(TABLE_NAME3, new String[]{"rowid _id", imgmct, img01, imgten, imgqc, img02, img03, img04, img09, img10, imgsachluoc}
                , imgprog + "=?", new String[]{prog}, null, null, null, null);
    }

    //del table
    public Cursor getAll_4(String prog) {
        return db.query(TABLE_NAME4, new String[]{dprog,dmct, d01}
                , dprog + "=?", new String[]{prog}, null, null, null, null);
    }

    //上傳資料準備
    public Cursor getAll_qra_qrb(String g_user, String g_date, String upload, String confirmID) {
        try {
            return db.rawQuery("SELECT qraprog,qra01,qra02,qra03,qrb02,qrb03,qrb06,qrb07,qrb08,qrb09,qrb10,'"
                    + g_user + "' as qruser, '"
                    + g_date + "' as qrdate,'"
                    + upload + "' as qrupload,'"
                    + confirmID + "' as qrconfirmID, "
                    + "qrb11 "
                    + " FROM " + TABLE_NAME1 + "," + TABLE_NAME2 + " WHERE qra01=qrb01 ORDER BY qrb01,qrb02", null);
        } catch (Exception e) {
            return null;
        }
    }

    public Cursor max_qrb02(String prog, String qrb01) {
        return db.query(TABLE_NAME2, new String[]{"MAX(" + qrb02 + ") AS MAX"}, null, null, null, null, null);
    }

    ////////////////////////////////QUERY (E)//////////////////////////////////////

    ////////////////////////////////CHECK (S)//////////////////////////////////////

    //Kiểm tra MVL khi được quét (S)
    //Kiểm tra danh sách có tồn tại MVL được quét
    public boolean check_qrb(String mct, String mvl) {
        boolean result = false;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb03 + "=? ", new String[]{mct, mvl},
                null, null, null, null);

        if (res != null && res.getCount() > 0) {
            result = true; //Trong danh sách CÓ tồn tại MVL được quét
        } else {
            result = false; //Trong danh sách KHÔNG CÓ tồn tại MVL được quét
        }
        return result;
    }

    //Kiểm tra vật liệu CHƯA check dự bị
    public int check_qrb011(String q_qrb01, String q_qrb03, boolean q_qrb011) {
        int q_qrb02 = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb03 + "=? AND " + qrb11 + "= '" + q_qrb011 + "'", new String[]{q_qrb01, q_qrb03}, null, null, null, null);
        res.moveToFirst();
        if (res != null && res.getCount() > 0) {
            q_qrb02 = Integer.parseInt(res.getString(res.getColumnIndex("qrb02")));
        } else {
            q_qrb02 = 0;
        }
        return q_qrb02;
    }

    //Kiểm tra dòng check đã có dữ liệu kho chưa
    public int check_qrb06(String q_qrb01, String q_qrb03) {
        int c_qrb02 = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb03 + "=? AND " + qrb06 + " IS NULL ", new String[]{q_qrb01, q_qrb03}, null, null, null, null);
        res.moveToFirst();
        if (res.getCount() > 0 && res.getString(res.getColumnIndex("qrb02")) != null) {
            c_qrb02 = Integer.parseInt(res.getString(res.getColumnIndex("qrb02")));
        } else {
            c_qrb02 = 0;
        }
        return c_qrb02;
    }

    public int check_239(String mct, int hangmuc, String mvl, String re_kho, String re_vitri, String re_solo) {
        int result = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb03 + "=? AND " + qrb02 + "<>? AND " + qrb06 + "=? AND " + qrb07 + "=? AND " + qrb08 + "=? ",
                new String[]{mct, mvl, String.valueOf(hangmuc), re_kho, re_vitri, re_solo},
                null, null, null, null);

        if (res != null && res.getCount() > 0) {
            result = 0; //fail
        } else {
            result = 1; //ok
        }
        return result;
    }
    //Kiểm tra MVL khi được quét (E)


    //Kiểm tra qrb_file có vật liệu CHƯA check hay không- trả về số dòng chưa check
    public int check_qrb011_trans(String q_qrb01, boolean q_qrb011) {
        int q_count = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb11 + "= '" + q_qrb011 + "'", new String[]{q_qrb01}, null, null, null, null);
        res.moveToFirst();
        if (res != null && res.getCount() > 0) {
            q_count = res.getCount();
        } else {
            q_count = 0;
        }
        return q_count;
    }

    //Kiểm tra qrb_file có vật liệu nào chưa chọn kho
    public int check_qrb06_trans(String q_qrb01) {
        int q_count = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb06 + " IS NULL ", new String[]{q_qrb01}, null, null, null, null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            q_count = res.getCount();
        } else {
            q_count = 0;
        }
        return q_count;
    }

    //Kiểm tra qrb_file có vật liệu nào chưa hập số lượng
    public int check_qrb09_trans(String q_qrb01) {
        int q_count = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb09 + " <= 0 ", new String[]{q_qrb01}, null, null, null, null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            q_count = res.getCount();
        } else {
            q_count = 0;
        }
        return q_count;
    }

    //Kiểm tra qrb_file có kho nào không có tồn kho
    public int check_qrb13_trans(String q_qrb01) {
        int q_count = -1;
        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb13 + " = 0 ", new String[]{q_qrb01}, null, null, null, null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            q_count = res.getCount();
        } else {
            q_count = 0;
        }
        return q_count;
    }

    //check tồn kho nhỏ hơn sl phát liệu
    public int check_qrb09_qrb13(String q_qrb01) {
        int q_count = -1;

        Cursor res = db.query(TABLE_NAME2, new String[]{"rowid _id", qrb02},
                qrb01 + "=? AND " + qrb13 + " < " + qrb09, new String[]{q_qrb01}, null, null, null, null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            q_count = res.getCount();
        } else {
            q_count = 0;
        }

        return q_count;
    }

    ////////////////////////////////CHECK (E)//////////////////////////////////////


}