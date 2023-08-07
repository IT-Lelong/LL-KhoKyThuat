package com.lelong.ll_khokythuat.QR211;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lelong.ll_khokythuat.Create_Table;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class qr211_search2 extends AppCompatActivity {

    Cursor cursor_1, cursor_2;
    private Create_Table createTable = null;
    String[] station = new String[0];
    ListView lv_dsdata1;
    String g_server = "";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
    String ID, conf_kho, l_vtri;
    EditText edt_mvl2;
    Spinner cbx_kho2, cbx_vtri2;
    Button btn_Search;
    ListView lv_search2;
    ArrayList<qr211_vt_listdata> qr211_vt_listdata;
    GridView_Adapter3 GridView_Adapter3;
    ArrayAdapter<String> stationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle getbundle = getIntent().getExtras();
        setContentView(R.layout.activity_qr211_search2);
        g_server = getbundle.getString("SERVER");

        edt_mvl2 = findViewById(R.id.edt_mvl2);
        cbx_kho2 = findViewById(R.id.cbx_kho2);
        cbx_vtri2 = findViewById(R.id.cbx_vtri2);
        btn_Search = findViewById(R.id.btn_Search);
        lv_search2 = findViewById(R.id.lv_search2);

        createTable = new Create_Table(this);
        createTable.open();

        check_kho(this);
        cbx_kho2.getSelectedItem().toString();
        String s_kho2 = cbx_kho2.getSelectedItem().toString();
        check_region(this, s_kho2);

        cbx_kho2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cbx_kho2.getSelectedItem().toString();
                String s_xuong = cbx_kho2.getSelectedItem().toString();
                check_region(qr211_search2.this, s_xuong);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_mvl = edt_mvl2.getText() != null ? edt_mvl2.getText().toString() : "";
                String s_kho = cbx_kho2.getSelectedItem() != null ? cbx_kho2.getSelectedItem().toString() : "";
                String s_vtri = cbx_vtri2.getSelectedItem() != null ? cbx_vtri2.getSelectedItem().toString() : "";
                getcode(s_mvl, s_kho, s_vtri);

            }

        });
    }

    private void getcode(String g_mvl, String g_kho, String vtri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/get_Datashow2.php?item=" + g_kho + "&item2=" + vtri + "&item3=" + g_mvl);
                if (res.length() > 0 && !res.equals("FALSE")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(res);

                                qr211_vt_listdata = new ArrayList<qr211_vt_listdata>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String img02 = jsonObject.getString("IMG02");
                                    String img03 = jsonObject.getString("IMG03");
                                    String img01 = jsonObject.getString("IMG01");
                                    String ten = jsonObject.getString("TEN");
                                    //int ima27 = Integer.parseInt(jsonObject.getString("IMA27"));
                                    double ima27 = Double.parseDouble(jsonObject.getString("IMA27"));
                                    //double img10 = Integer.parseInt(jsonObject.getString("IMG10"));
                                    double img10 = Double.parseDouble(jsonObject.getString("IMG10"));
                                    //SaveCode = g_kho;
                                    qr211_vt_listdata.add(new qr211_vt_listdata(img02, img03, img01, ten, ima27, img10));
                                }
                                //lv_kho.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                GridView_Adapter3 = new GridView_Adapter3(qr211_search2.this, R.layout.activity_qr211_show2_row, qr211_vt_listdata);

                                //set dữu liệu từ adapter vào listview
                                lv_search2.setAdapter(GridView_Adapter3);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "沒有查詢資料 Không có dữ liệu để tra cứu (1)", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

    private void check_kho(Context ctx) {
        cursor_1 = createTable.getAll_setup_01();
        cursor_1.moveToFirst();
        int num = cursor_1.getCount();
        station = new String[num + 1];
        station[0] = " ";
        for (int i = 1; i < num + 1; i++) {

            try {
                @SuppressLint("Range") String setup01 = cursor_1.getString(cursor_1.getColumnIndex("setup01"));

                String g_setup01 = setup01;
                station[i] = g_setup01;

            } catch (Exception e) {
                String err = e.toString();
            }
            cursor_1.moveToNext();
        }
        stationlist = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, station);
        cbx_kho2.setAdapter(stationlist);
        cbx_kho2.setAdapter(stationlist);
        cbx_kho2.setSelection(0);
    }

    private void check_region(Context cxt2, String l_kho) {
        cursor_1 = createTable.getAll_stand_01(l_kho);
        cursor_1.moveToFirst();
        int num = cursor_1.getCount();
        station = new String[num + 1];
        station[0] = " ";
        for (int i = 1; i < num+1; i++) {

            try {
                @SuppressLint("Range") String basic02 = cursor_1.getString(cursor_1.getColumnIndex("basic02"));

                String g_basic02 = basic02;
                station[i] = g_basic02;

            } catch (Exception e) {
                String err = e.toString();
            }
            cursor_1.moveToNext();
        }
        stationlist = new ArrayAdapter<>(cxt2, android.R.layout.simple_spinner_item, station);
        cbx_vtri2.setAdapter(stationlist);
        cbx_vtri2.setAdapter(stationlist);
        cbx_vtri2.setSelection(0);

    }

}