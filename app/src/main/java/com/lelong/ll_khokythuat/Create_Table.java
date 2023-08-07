package com.lelong.ll_khokythuat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Create_Table {

    private Context mCtx = null;
    String DATABASE_NAME = "MainDB.db";
    public SQLiteDatabase db = null;

    //thiết lập dữ liệu xưởng
    String TB_setup_data_file = "setup_data_file";
    String setup01 = "setup01"; //Kho

    String TB_basic_data_file = "basic_data_file";
    String basic01 = "basic01"; //Kho
    String basic02 = "basic02"; //Vị trí lưu

    String CREATE_TB_setup_data_file = "CREATE TABLE IF NOT EXISTS " + TB_setup_data_file + " ("
            + setup01 + " TEXT)";

    String CREATE_TB_basic_data_file = "CREATE TABLE IF NOT EXISTS " + TB_basic_data_file + " ("
            + basic01 + " TEXT," + basic02 + " TEXT )";

    public Create_Table(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() throws SQLException {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
        try {
            db.execSQL(CREATE_TB_setup_data_file);
            db.execSQL(CREATE_TB_basic_data_file);
        } catch (Exception e) {

        }
    }
    //Kho
    public String insSetup_data(String g_setup01) {
        try {
            ContentValues args = new ContentValues();
            args.put(setup01, g_setup01);
            db.insert(TB_setup_data_file, null, args);
            return "TRUE";
        } catch (Exception e) {
            return "FALSE";
        }
    }
    //Kho + v.tri
    public String insBasic_data(String g_basic01,String g_basic02) {
        try {
            ContentValues args = new ContentValues();
            args.put(basic01, g_basic01);
            args.put(basic02, g_basic02);
            db.insert(TB_basic_data_file, null, args);
            return "TRUE";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public void close() {
        try {
            String DROP_TABLE_TB_setup_data_file = "DROP TABLE IF EXISTS " + TB_setup_data_file;
            db.execSQL(DROP_TABLE_TB_setup_data_file);
            String DROP_TABLE_TB_basic_data_file = "DROP TABLE IF EXISTS " + TB_basic_data_file;
            db.execSQL(DROP_TABLE_TB_basic_data_file);
            db.close();
        } catch (Exception e) {

        }
    }

    //del setup_file trên máy
    public void del_setup() {
        db.execSQL("delete from " + TB_setup_data_file);
    }

    //del basic_file trên máy
    public void del_basic() {
        db.execSQL("delete from " + TB_basic_data_file);
    }

    //kiểm tra dữ liệu show lên dialog
    public Cursor getAll_setup_01() {
        Cursor a;
        try {

            //SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT distinct setup01 FROM setup_data_file ORDER BY 1";
            return db.rawQuery(selectQuery, null);

        } catch (Exception e) {
            return null;
        }
    }

    //Lấy dữ liệu show lên stand
    public Cursor getAll_stand_01(String l_kho) {
        Cursor a;
        try {

            //SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT basic02 FROM basic_data_file WHERE basic01='" + l_kho + "'  ORDER BY 1";
            return db.rawQuery(selectQuery, null);

        } catch (Exception e) {
            return null;
        }
    }


}
