package com.lelong.ll_khokythuat.QR211;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.lelong.ll_khokythuat.Create_Table;
import com.lelong.ll_khokythuat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class qr211_search extends AppCompatActivity {

    Cursor cursor_1, cursor_2;
    private Create_Table createTable = null;
    String[] station = new String[0];
    ListView lv_dsdata1;
    String g_server = "";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
    String ID, conf_kho,l_vtri;
    private qr211_upload_data qr211_upload_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr211_search);

        Bundle getbundle = getIntent().getExtras();
        ID = getbundle.getString("ID");
        conf_kho = getbundle.getString("KHO");
        g_server = getbundle.getString("SERVER");

        //qr211_upload_data = new qr211_upload_data(this, g_server, conf_kho);
        //qr211_upload_data.load_data_ime();

        GridView gridView = findViewById(R.id.gridView_DS);
        List<String> data = new ArrayList<>();

        createTable = new Create_Table(this);
        createTable.open();

        cursor_1 = createTable.getAll_stand_01(conf_kho);
        cursor_1.moveToFirst();
        int num = cursor_1.getCount();
        station = new String[num];

        for (int i = 0; i < num; i++) {

            try {
                @SuppressLint("Range") String basic02 = cursor_1.getString(cursor_1.getColumnIndex("basic02"));
                //int number = Integer.parseInt(basic02);
                String g_basic02 = basic02;
                l_vtri = g_basic02;
                station[i] = g_basic02;

            } catch (Exception e) {
                String err = e.toString();
            }
            data.add(l_vtri);
            cursor_1.moveToNext();
        }
        GridView_Adapter adapter = new GridView_Adapter(this, data, ID, conf_kho, lv_dsdata1, g_server);
        gridView.setAdapter(adapter);
    }


}