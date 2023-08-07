package com.lelong.ll_khokythuat.QR211;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.lelong.ll_khokythuat.Create_Table;
import com.lelong.ll_khokythuat.QR210.Adapter.qr210_vt_listDataAdapter;
import com.lelong.ll_khokythuat.QR210.Model.qr210_vt_listData;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class qr211_show_data1 extends AppCompatActivity {
    String ID, conf_kho, l_vtri, g_server, SaveCode;
    Cursor cursor_1, cursor_2;
    private Create_Table createTable = null;
    ArrayList<qr211_vt_listdata> qr211_vt_listdata;
    GridView_Adapter2 GridView_Adapter2;
    ListView lv_show1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr211_show_data1);
        lv_show1 = findViewById(R.id.lv_show1);

        Bundle getBundle = getIntent().getExtras();
        ID = getBundle.getString("ID");
        conf_kho = getBundle.getString("KHO");
        l_vtri = getBundle.getString("VITRI");
        g_server = getBundle.getString("SERVER");

        createTable = new Create_Table(this);
        createTable.open();

        getcode(conf_kho, l_vtri);

    }


    /*private void getcode(String g_kho, String vtri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/get_Datashow.php?item=" + g_kho + "&item2=" + vtri);
                //final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/get_Datashow.php?item=" + g_kho);
                if (res.length() > 0 && !res.equals("FALSE")) {

                    try {
                        JSONArray jsonArray = new JSONArray(res);

                        qr211_vt_listdata = new ArrayList<qr211_vt_listdata>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String img02 = jsonObject.getString("IMG02");
                            String img03 = jsonObject.getString("IMG03");
                            String img01 = jsonObject.getString("IMG01");
                            String ten = jsonObject.getString("TEN");
                            int ima27 = Integer.parseInt(jsonObject.getString("IMA27"));
                            int img10 = Integer.parseInt(jsonObject.getString("IMG10"));
                            //SaveCode = g_kho;
                            qr211_vt_listdata.add(new qr211_vt_listdata(img02, img03, img01, ten, ima27, img10));
                        }
                        //lv_kho.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        GridView_Adapter2 = new GridView_Adapter2(qr211_show_data1.this, R.layout.activity_qr211_show_data1, qr211_vt_listdata);

                        //set dữu liệu từ adapter vào listview
                        lv_show1.setAdapter(GridView_Adapter2);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();

    }*/

    private void getcode(String g_kho, String vtri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/get_Datashow.php?item=" + g_kho + "&item2=" + vtri);
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
                                    // ima27 = Integer.parseInt(jsonObject.getString("IMA27"));
                                    double ima27 = Double.parseDouble(jsonObject.getString("IMA27"));
                                    double img10 = Integer.parseInt(jsonObject.getString("IMG10"));
                                    //SaveCode = g_kho;
                                    qr211_vt_listdata.add(new qr211_vt_listdata(img02, img03, img01, ten, ima27, img10));
                                }
                                //lv_kho.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                GridView_Adapter2 = new GridView_Adapter2(qr211_show_data1.this, R.layout.activity_qr211_show1_row, qr211_vt_listdata);

                                //set dữu liệu từ adapter vào listview
                                lv_show1.setAdapter(GridView_Adapter2);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
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

}