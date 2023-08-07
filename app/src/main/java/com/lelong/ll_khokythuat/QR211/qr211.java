package com.lelong.ll_khokythuat.QR211;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lelong.ll_khokythuat.CheckAppUpdate;
import com.lelong.ll_khokythuat.Create_Table;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class qr211 extends AppCompatActivity {
    private Create_Table createTable = null;
    private Create_Table db = null;
    Button btn211_01, btn211_02;
    private CheckAppUpdate checkAppUpdate = null;
    JSONArray tjsonupload, jsonupload;
    JSONObject ujobject;
    String g_server;
    String ID;
    Spinner cbx_kho;
    Cursor cursor_1, cursor_2;
    String[] station = new String[0];
    ArrayAdapter<String> stationlist;
    Button btn_dconf;
    private qr211_upload_data qr211_upload_data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle getbundle = getIntent().getExtras();
        setContentView(R.layout.qr211_activity);
        g_server = getbundle.getString("SERVER");
        ID = getbundle.getString("ID");

        btn211_01 = findViewById(R.id.btn211_01);
        btn211_02 = findViewById(R.id.btn211_02);

        btn211_01.setOnClickListener(btnlistener);
        btn211_02.setOnClickListener(btnlistener);

        createTable = new Create_Table(this);
        createTable.open();

        createTable.del_setup();
        getcode_up();

        qr211_upload_data = new qr211_upload_data(qr211.this, g_server);
        qr211_upload_data.load_data_ime();

    }

    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn211_01: {

                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.qr211_dialog01);
                    cbx_kho = dialog.findViewById(R.id.cbx_dkho);
                    createTable = new Create_Table(dialog.getContext());
                    createTable.open();
                    check_plant(v.getContext());
                    cbx_kho.getSelectedItem().toString();

                    btn_dconf = dialog.findViewById(R.id.btn_dconf);
                    btn_dconf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String conf_kho = cbx_kho.getSelectedItem().toString();

                            //qr211_upload_data = new qr211_upload_data(qr211.this, g_server, conf_kho);
                            //qr211_upload_data.load_data_ime();

                            Intent R211_01 = new Intent();
                            R211_01.setClass(qr211.this, qr211_search.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ID", ID);
                            bundle.putString("KHO", conf_kho);
                            bundle.putString("SERVER", g_server);
                            R211_01.putExtras(bundle);
                            startActivity(R211_01);
                        }
                    });
                    dialog.show();
                    break;
                }

                case R.id.btn211_02: {
                    Intent R211_02 = new Intent();
                    R211_02.setClass(qr211.this, qr211_search2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", ID);
                    bundle.putString("SERVER", g_server);
                    R211_02.putExtras(bundle);
                    startActivity(R211_02);
                    break;
                }
            }

        }
    };

    private void getcode_up() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/getDataSetup.php?item=search1");
                if (!res.equals("FALSE")) {
                    try {
                        JSONArray jsonarray = new JSONArray(res);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObject = jsonarray.getJSONObject(i);
                            String g_sdata01 = jsonObject.getString("IMD01"); //Kho
                            createTable.insSetup_data(g_sdata01);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Looper.loop();
            }

        }).start();

    }

    private String getcodedata(String s) {
        try {
            HttpURLConnection conn = null;
            URL url = new URL(s);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(999999);
            conn.setReadTimeout(999999);
            conn.setDoInput(true); //允許輸入流，即允許下載
            conn.setDoOutput(true); //允許輸出流，即允許上傳
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String jsonstring = reader.readLine();
            reader.close();
            if (!jsonstring.equals("FALSE")) {
                return jsonstring;
            } else {
                return "FALSE";
            }
        } catch (Exception e) {
            return "FALSE";
        }
    }

    private void check_plant(Context dialog1) {
        cursor_1 = createTable.getAll_setup_01();
        cursor_1.moveToFirst();
        int num = cursor_1.getCount();
        station = new String[num];
        for (int i = 0; i < num; i++) {

            try {
                @SuppressLint("Range") String setup01 = cursor_1.getString(cursor_1.getColumnIndex("setup01"));

                String g_setup01 = setup01;
                station[i] = g_setup01;

            } catch (Exception e) {
                String err = e.toString();
            }
            cursor_1.moveToNext();
        }
        stationlist = new ArrayAdapter<>(dialog1, android.R.layout.simple_spinner_item, station);
        cbx_kho.setAdapter(stationlist);
        cbx_kho.setAdapter(stationlist);
        cbx_kho.setSelection(0);
    }

}