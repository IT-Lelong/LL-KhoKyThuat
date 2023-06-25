package com.lelong.ll_khokythuat.QR210.Main;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class qr210CallAPI {
    String g_server = "";
    qr210_interface qr210Interface;
    private qr210DB db210 = null;
    private Context mCtxAPI = null;


    public qr210CallAPI(Context ctx, qr210DB db210, String mServer) {
        this.mCtxAPI = ctx;
        qr210Interface = (qr210_interface) ctx;
        this.db210 = db210;
        this.g_server = mServer;
    }

    public Integer qr210_KT_item(String mkind, final String mtemKT) {
        //mkind 1 = 廣泰標籤代號 ； mkind 2 = 廣泰標籤工單
        Integer g_chk = -1;
        String res = null;
        //若標籤是廣泰的標籤卡
        /*temKT : 標籤全碼
        mDate : 標籤日期
        mItem ：標籤項目
        mCode ：標籤碼*/
        try {
            res = new qr210_KT_item().execute("http://172.16.40.20/" + g_server + "/QR210/tem_KT_qr210.php?code=" + mtemKT + "&kind=" + mkind).get();
            if (!res.equals("FAIL")) {
                qr210Interface.qr210_TaskComplete("KT", res);
                g_chk = 1;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return g_chk;
    }

    private class qr210_KT_item extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }
    }

    public Integer qr210_LL_thuMua(Integer g_hangmuc,String g_mdc) {
        Integer g_chk = -1;
        String res = null;
        try {
            res = new qr210_LL_thuMua_item().execute("http://172.16.40.20/" + g_server + "/QR210/tem_LL_thuMua_qr210.php?code=" + g_mdc + "&hangmuc=" + g_hangmuc).get();
            if (!res.equals("FAIL")) {
                if(g_hangmuc > 0){
                    if(g_hangmuc > 0){qr210Interface.qr210_TaskComplete("TM", res);}
                }
                g_chk = 3;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return g_chk;
    }

    private class qr210_LL_thuMua_item extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }
    }

    public void qr210_LC_item(String temLC_dc, String temLC_ma_res) {
        //掃到利隆極板標籤卡
        /*temLC_dc : 極板工單
        temLC_ma_res : 極板標籤碼*/
        try {
            String res = new qr210_LC_item().execute("http://172.16.40.20/" + g_server + "/QR210/tem_LC_qr210.php?doc=" + temLC_dc + "&code=" + temLC_ma_res).get();
            qr210Interface.qr210_TaskComplete("LC", res.substring(0, res.length() - 1));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class qr210_LC_item extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }
    }

    public void qr210_getItemData(String res_qr_code) {
        try {
            String res = new qr210_getItemData().execute("http://172.16.40.20/" + g_server + "/QR210/get_ItemData.php?item=" + res_qr_code).get();
            qr210Interface.qr210_TaskComplete("GID", res.substring(0, res.length()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Hàm cập nhật thông tin ima_file của vật liệu vừa được quét
    private class qr210_getItemData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }
    }

    public void qr210_MaCT(String qr_val, String prog) {
        new qr210_MaCT(prog).execute("http://172.16.40.20/" + g_server + "/QR210/getdata_qr210.php?qr01=" + qr_val + "&prog=" + prog);
    }

    private class qr210_MaCT extends AsyncTask<String, Integer, String> {
        String prog;

        public qr210_MaCT(String prog_t) {
            this.prog = prog_t;
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        protected void onPostExecute(String result) {
            try {
                if (result.length() > 0 && !result.substring(result.length() - 2, result.length()).equals("[]")) {
                    JSONArray jsonarray = new JSONArray(result);
                    //mangLV = new ArrayList<qr210_listData>();
                    db210.del_db(prog);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String qra01 = jsonObject.getString("INA01"); //單據
                        String qra02 = jsonObject.getString("INA03"); //單據日期
                        String qra03 = jsonObject.getString("INA04"); //部門編號
                        String qra04 = jsonObject.getString("GEM02"); //部門名稱
                        String qra05 = jsonObject.getString("INAPOST"); //單據過帳狀況

                        String qrb01 = jsonObject.getString("INA01"); //單據
                        int qrb02 = jsonObject.getInt("INB03"); //項目
                        String qrb03 = jsonObject.getString("INB04"); //料號
                        String qrb04 = jsonObject.getString("TA_IMA02_1"); //品名
                        String qrb05 = jsonObject.getString("TA_IMA021_1"); //規格

                        String qrb06 = jsonObject.getString("INB05"); //倉庫
                        String qrb07 = jsonObject.getString("INB06"); //儲位
                        String qrb08 = jsonObject.getString("INB07"); //批號
                        double qrb09 = jsonObject.getDouble("INB09"); //數量

                        String qrb10 = jsonObject.getString("INB08"); //單位
                        String qrb11 = jsonObject.getString("SCAN");
                        ; //掃描否
                        String qrb12 = jsonObject.getString("IMZUD02"); //策略
                        String qrb13_t = jsonObject.getString("IMG10"); //庫存量
                        double qrb13;
                        if (qrb13_t.isEmpty() || qrb13_t.equals("null") || qrb13_t == null) {
                            qrb13 = 0;
                        } else {
                            qrb13 = Double.parseDouble(qrb13_t);
                        }

                        db210.append(prog, qra01, qra02, qra03, qra04, qra05,
                                qrb01, qrb02, qrb03, qrb04, qrb05, qrb06,
                                qrb07, qrb08, qrb09, qrb10, qrb11, qrb12, qrb13);
                    }
                    qr210Interface.qr210Refect(true);
                } else {
                    //Toast.makeText(mCtxAPI, getString(R.string.E17), Toast.LENGTH_SHORT).show();
                    qr210Interface.qr210_Toast("E17", "");
                }
            } catch (Exception e) {
            }
        }
    }

    public void qr210_loadInfo(String g_kind, String g_sql) {
        new loadInfo(g_kind).execute("http://172.16.40.20/" + g_server + "/QR210/getSql.php?SQL=" + g_sql);
    }

    private class loadInfo extends AsyncTask<String, String, String> {
        String kind;

        public loadInfo(String kind_t) {
            this.kind = kind_t;
        }

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.length() > 0) {
                switch (kind) {
                    case "0": //lấy thông tin nhân viên nhận hàng 依單據擷取收料人員
                        try {
                            JSONArray jsonArray = new JSONArray(s);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ina11 = jsonObject.getString("INA11");
                                if (ina11.length() > 0) {
                                    qr210Interface.qr210_getIna11(ina11);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    /*case :
                        break;
                    case :
                        break;*/
                }

            }
        }
    }

    private String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                //content.append(line + "\n");
                content.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
