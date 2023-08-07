package com.lelong.ll_khokythuat.QR211;

import android.content.Context;
import android.os.Looper;

import com.lelong.ll_khokythuat.Create_Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class qr211_upload_data {

    private Create_Table db = null;
    private Create_Table createTable = null;
    private Context mCtxAPI = null;
    String g_package = "";
    String g_server = "";
    String g_kho;

    public qr211_upload_data(Context ctx, String g_server) {
        this.g_server = g_server;
        this.mCtxAPI = ctx;
        g_package = mCtxAPI.getPackageName().toString();
    }

    public void load_data_ime() {
        createTable = new Create_Table(this.mCtxAPI);
        createTable.open();

        createTable.del_basic();
        getcode_up();
    }

    private void getcode_up() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/getDataSetup.php?item=search2");
                final String res = getcodedata("http://172.16.40.20/" + g_server + "/QR211/getData_VT.php");
                if (!res.equals("FALSE")) {
                    try {
                        JSONArray jsonarray = new JSONArray(res);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonObject = jsonarray.getJSONObject(i);
                            String g_basic01 = jsonObject.getString("IME01"); //Kho
                            String g_basic02 = jsonObject.getString("IME02"); //v.tri
                            createTable.insBasic_data(g_basic01, g_basic02);
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
}
