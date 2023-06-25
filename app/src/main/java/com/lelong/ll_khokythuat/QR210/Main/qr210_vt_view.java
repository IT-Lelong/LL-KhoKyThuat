package com.lelong.ll_khokythuat.QR210.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lelong.ll_khokythuat.QR210.Adapter.qr210_vt_listDataAdapter;
import com.lelong.ll_khokythuat.QR210.Model.qr210_vt_listData;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class qr210_vt_view extends AppCompatActivity {
//public class qr210_vt_view extends Activity {
    String g_server = "";
    TextView tv_MVL, tv_name, tv_SL;
    ListView lv_kho;
    String MVL, TEN, QC, DV, PROG, MCT, KHO, VITRI, SOLO, XUONG, SACHLUOC;
    int SL, sl_memo;
    Boolean g_check = false;
    ArrayList<qr210_vt_listData> qr210VtListData;
    qr210_vt_listDataAdapter qr210VtListDataAdapter;

    Button btnThoat, btnXacNhan;
    private qr210DB db210;
    public static int travetu_qr210_vt_view = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr210_view_stock);
        addcontrols();
        addEvent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


       /* lv_kho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.tv_vt_check);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    sum -= qr210VtListData.get(position).getUses();

                    db210.del_img(MCT,
                            qr210VtListData.get(position).getImg02(),
                            qr210VtListData.get(position).getImg03(),
                            qr210VtListData.get(position).getImg04(),
                            qr210VtListData.get(position).getUses());

                    qr210VtListData.get(position).setUses(0);
                    qr210VtListData.get(position).setChecked(false);
                    qr210VtListDataAdapter.notifyDataSetChanged();
                } else {
                    cb.setChecked(true);
                    int kq = SL - sum;
                    if (sum < SL) {
                        if (qr210VtListData.get(position).getImg10() >= kq) {
                            qr210VtListData.get(position).setUses(kq);
                            sum = sum + kq;
                            qr210VtListData.get(position).setChecked(true);

                            db210.ins_img(PROG,
                                    MCT, MVL, TEN, QC,
                                    qr210VtListData.get(position).getImg02(),
                                    qr210VtListData.get(position).getImg03(),
                                    qr210VtListData.get(position).getImg04(),
                                    qr210VtListData.get(position).getImg09(),
                                    kq, SACHLUOC);
                        } else {
                            qr210VtListData.get(position).setUses(qr210VtListData.get(position).getImg10());
                            sum = sum + qr210VtListData.get(position).getImg10();
                            qr210VtListData.get(position).setChecked(true);

                            db210.ins_img(PROG,
                                    MCT, MVL, TEN, QC,
                                    qr210VtListData.get(position).getImg02(),
                                    qr210VtListData.get(position).getImg03(),
                                    qr210VtListData.get(position).getImg04(),
                                    qr210VtListData.get(position).getImg09(),
                                    qr210VtListData.get(position).getImg10()
                                    , SACHLUOC);
                        }
                    } else if (sum > SL) {
                        qr210VtListData.get(position).setChecked(false);
                        Toast.makeText(getApplicationContext(), "vượt sl phải phát liệu 超過", Toast.LENGTH_SHORT).show();
                    } else if (sum == SL) {
                        qr210VtListData.get(position).setChecked(false);
                        Toast.makeText(getApplicationContext(), "vượt sl phải phát liệu 超過", Toast.LENGTH_SHORT).show();
                    }
                    qr210VtListDataAdapter.notifyDataSetChanged();
                }

            }
        });*/

    }

    private class load_wh_data extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                // \uFEFF[] nội dung được trả về khi truy vấn tồn kho trống, dựa theo nội dung đặt điều kiện để hiện thông báo lỗi
                if ((s.length() > 0) && (s.equals("\uFEFF[]")) != true) {
                    JSONArray jsonarray = new JSONArray(s);
                    qr210VtListData = new ArrayList<qr210_vt_listData>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String img01 = jsonObject.getString("IMG01"); //料號
                        String ima02 = jsonObject.getString("IMA02"); //中文名稱
                        String ta_ima02_1 = jsonObject.getString("TA_IMA02_1"); //越文名稱
                        String ta_ima021_1 = jsonObject.getString("TA_IMA021_1"); //物料規格

                        String img02 = jsonObject.getString("IMG02"); //倉庫
                        String img03 = jsonObject.getString("IMG03"); //儲位
                        String img04 = jsonObject.getString("IMG04"); //批號
                        String img09 = jsonObject.getString("IMG09"); //單位
                        int img10 = Integer.parseInt(jsonObject.getString("IMG10")); //數量

                        /*Phân bổ số lượng có thể phát hàng - 依倉庫分佈發料量(S)*/
                        /*if (sl_memo > img10) {
                            sl_res = img10;
                            sl_memo = sl_memo - img10;
                            g_check = true;
                        } else {
                            sl_res = sl_memo;
                            sl_memo = sl_memo - img10;
                            g_check = true;
                        }
                        if ((sl_res == 0) || ((sl_memo < 0) && (sl_res < 0))) {
                            sl_res = 0;
                            g_check = false;
                        }

                        if (g_check == true) {
                            sum += sl_res;
                            //lưu giá trị phát liệu đã chọn
                            db210.ins_img(PROG, MCT, img01, ta_ima02_1, ta_ima021_1,
                                    img02, img03, img04, img09, sl_res, SACHLUOC);
                        }*/
                        /*Phân bổ  số lượng có thể phát hàng -依倉庫分佈發料量(E)*/

                        //đẩy dữ liệu vào mảng
                        //qr210VtListData.add(new qr210_vt_listData(img01, ima02, ta_ima02_1, ta_ima021_1, img02, img03, img04, img09, img10, sl_res, g_check));
                        qr210VtListData.add(new qr210_vt_listData(img01, ima02, ta_ima02_1, ta_ima021_1, img02, img03, img04, img09, img10, 0));

                        String names = ima02 + " " + ta_ima02_1 + "\n" + ta_ima021_1;
                        tv_name.setText(names);
                    }

                    //Đẩy dữ liệu từ mảng vào adapter
                    //lv_kho.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    qr210VtListDataAdapter = new qr210_vt_listDataAdapter(getApplicationContext(), R.layout.qr210_view_stock_list, qr210VtListData);

                    //set dữu liệu từ adapter vào listview
                    lv_kho.setAdapter(qr210VtListDataAdapter);
                    //lv_kho.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "Không có tồn kho", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Không có tồn kho", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private final View.OnClickListener btnListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnXacNhan: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(qr210_vt_view.this);
                    builder.setCancelable(false);
                    //builder.setTitle("確認信息");
                    //builder.setMessage(qr300_code);
                    //builder.setMessage(getString(R.string.M14));
                    builder.setMessage("確認是否變更發料倉，儲位，批號？？");
                    builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(qr210_vt_view.this, getString(R.string.no), Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(qr210_vt_view.this, getString(R.string.yes), Toast.LENGTH_SHORT).show();
                            //lưu dữ liệu kho và số lượng
                            xulyimg_file();
                            Intent intent = new Intent();
                            intent.putExtra("qr210_st", "kho_change");
                            setResult(travetu_qr210_vt_view, intent);
                            finish();
                        }

                        private void xulyimg_file() {
                            int i = qr210VtListData.size();
                            for (int f = 1; f <= i; f++) {
                                int chek = Integer.parseInt(String.valueOf(qr210VtListData.get(f - 1).getUses()));
                                if ( chek > 0) {
                                    db210.ins_img(PROG, MCT,
                                            qr210VtListData.get(f - 1).getImg01(),
                                            qr210VtListData.get(f - 1).getTa_ima02_1(),
                                            qr210VtListData.get(f - 1).getTa_ima021_1(),
                                            qr210VtListData.get(f - 1).getImg02(),
                                            qr210VtListData.get(f - 1).getImg03(),
                                            qr210VtListData.get(f - 1).getImg04(),
                                            qr210VtListData.get(f - 1).getImg09(),
                                            qr210VtListData.get(f - 1).getImg10(),
                                            SACHLUOC);
                                }
                                ;
                            }
                        }
                    });

                    builder.show();
                    break;
                }

                case R.id.btnThoat: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(qr210_vt_view.this);
                    builder.setCancelable(false);
                    //builder.setTitle("確認放棄否");
                    //builder.setMessage(qr300_code);
                    //builder.setMessage(getString(R.string.M14));
                    builder.setMessage(getString(R.string.M19));
                    builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(qr210_vt_view.this, getString(R.string.no), Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(qr210_vt_view.this, getString(R.string.yes), Toast.LENGTH_SHORT).show();
                            db210.del_db_img(PROG); //刪除 資料庫
                            Intent intent = new Intent();
                            intent.putExtra("qr210_st", "kho_cancel");
                            setResult(travetu_qr210_vt_view, intent);
                            finish();
                        }
                    });

                    builder.show();
                    break;
                }
                default:
                    break;
            }
        }
    };

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

    private void addcontrols() {
        tv_MVL = findViewById(R.id.tv_MVL);
        tv_SL = findViewById(R.id.tv_SL);
        tv_name = findViewById(R.id.tv_name);
        lv_kho = findViewById(R.id.lv_kho);
        btnThoat = findViewById(R.id.btnThoat);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        btnXacNhan.setOnClickListener(btnListenner);
        btnThoat.setOnClickListener(btnListenner);

        Bundle getBundle = getIntent().getExtras();
        MVL = getBundle.getString("MVL");
        TEN = getBundle.getString("TEN");
        QC = getBundle.getString("QC");
        SL = Integer.parseInt(getBundle.getString("SL"));
        sl_memo = Integer.parseInt(getBundle.getString("SL"));
        DV = getBundle.getString("DV");
        PROG = getBundle.getString("PROG");
        MCT = getBundle.getString("MCT");
        KHO = getBundle.getString("KHO");
        VITRI = getBundle.getString("VITRI");
        SOLO = getBundle.getString("SOLO");
        SACHLUOC = getBundle.getString("SACHLUOC");
        g_server = getBundle.getString("SERVER");

        if (MCT.substring(0, 2).equals("BC")) {
            XUONG = "D";   //德和
        } else {
            XUONG = "B";   //濱瀝
        }

        tv_MVL.setText(MVL);
        tv_SL.setText(String.valueOf(SL) + "  " + DV);

        db210 = new qr210DB(this);
        db210.open();
        db210.del_db_img(PROG);
    }

    private void addEvent() {
        if (MVL != null) {
            String.valueOf(new load_wh_data().execute("http://172.16.40.20/" + g_server + "/QR210/getdata_wh.php?mvl=" + MVL + "&xuong=" + XUONG));
        }

    }

}
