package com.lelong.ll_khokythuat.QR210.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.lelong.ll_khokythuat.Constant_Class;
import com.lelong.ll_khokythuat.QR210.Adapter.qr210_updateAdapter;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class qr210 extends AppCompatActivity implements qr210_interface {
    String g_server = "";
    private qr210DB db210 = null;
    private qr210CallAPI qr210CallAPI = null;
    ListView lv_multiple_selection;
    String ID, prog, g_maCT, comfirmID;
    String g_string_1, g_string_2, g_string_3;
    EditText edt_maCT, edtUser;
    TextView tv_mabpCT, tv_ngayCT, tv_tenbpCT, tvUser, tvStatus;
    Cursor cursor_1, cursor_2;
    Button btn_ok;
    int chk_dialog = -1;
    int chk_insert_new = 0;
    int check_user = -1;

    /*check code variable (S)*/
    String temLC_dc = null, temLC_ma = null, temLC_ma_res = null;
    String temTM_donThuMua = null, temTM_hangMuc = null, temTM_maVatLieu = null, temTM_soLo = null, temTM_soLuong = null;
    String temKT = null, mdate = null, temKT_mItem = null, temKT_mCode = null;
    Date temKT_mDate;
    Integer vLoai;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    /*check code variable (E)*/

    SoundPool OKPool, ERRORPool;
    int oksound, errorsound;
    Locale locale;

    SurfaceView surfaceView, surfaceView_upload;
    CameraSource cameraSource, cameraSource_upload;
    BarcodeDetector barcodeDetector, barcodeDetector_upload;
    boolean firstDetected = true;
    boolean firstDetected_upload = true;
    JSONArray jsonupload, jsonDelUpload;
    JSONObject ujobject;

    String res_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr210_aimt301_asfi511);
        addcontrols();
        addEvent();

        //addSpinner();

        OKPool = new SoundPool.Builder().build();
        ERRORPool = new SoundPool.Builder().build();
        oksound = OKPool.load(qr210.this, R.raw.ok, 1);
        errorsound = ERRORPool.load(qr210.this, R.raw.error, 1);


        db210 = new qr210DB(this);
        db210.open();
        qr210CallAPI = new qr210CallAPI(this, db210, g_server);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CameraSetting(1);
    }

    private void getcode(final String qr210_code) {
        //loai : 0 -> 正常 Tem thường (NO);
        //       1 -> 廣泰標籤 tem KungTay  (KT);
        //       2 -> 利隆極板 tem Lá Chì  (LC);
        //       3 -> 採購收貨標籤 tem nhận hàng Thu mua (TM);
        final String qr_val = qr210_code.trim();
        final String subqr = qr_val.substring(5, 6);
        res_qr_code = qr_val;

        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (qr_val.length() > 0) {
                    try {
                        vLoai = check_codeQR(qr_val);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                qr210.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (subqr.equals("-") && qr_val.length() == 16) {
                            OKPool.play(oksound, 1, 1, 0, 0, 1);
                            AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
                            builder.setCancelable(false);
                            builder.setTitle(R.string.T02);
                            //builder.setMessage(qr300_code);
                            builder.setMessage(getString(R.string.M14) + " " + qr_val);
                            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    firstDetected = true;
                                }
                            });
                            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //執行清空(S)
                                    db210.del_db(prog); //刪除db資料
                                    db210.del_db_deltable(prog); //刪除del_table資料
                                    qr210Refect(true);
                                    //執行清空(E)

                                    qr210CallAPI.qr210_MaCT(qr_val, prog);
                                    firstDetected = true;
                                }
                            });
                            builder.show();
                        } else {
                            if (edt_maCT.length() == 16) {
                                OKPool.play(oksound, 1, 1, 0, 0, 1);
                                g_string_1 = null;
                                g_string_2 = null;
                                g_string_3 = null;
                                qr210CallAPI.qr210_getItemData(res_qr_code);
                                new qr210_Item_tem().execute("http://172.16.40.20/" + g_server + "/QR210/check_ima01.php?ima01=" + res_qr_code);
                            } else {
                                ERRORPool.play(errorsound, 1, 1, 0, 0, 1);
                                Toast.makeText(getApplicationContext(), getString(R.string.E16), Toast.LENGTH_LONG).show();
                                firstDetected = true;
                            }
                        }
                    }
                });
            }
        });

        new Thread() {
            @Override
            public void run() {
                qr210.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        thread1.start();
                        try {
                            thread1.join();
                        } catch (InterruptedException e) {
                        }

                        thread4.start();
                        try {
                            thread4.join();
                        } catch (InterruptedException e) {
                        }

                    }
                });
            }
        }.start();
    }

    private int check_codeQR(String qr_val) throws ParseException {
        //loai : 0 -> 正常 Tem thường (NO);
        //       1 -> 廣泰標籤 tem KungTay  (KT) [oldstamp,normal];
        //       2 -> 利隆極板 tem Lá Chì  (LC);
        //       3 -> 採購收貨標籤 tem nhận hàng Thu mua (TM);
        int loai = 0;
        //檢查標籤是否廣泰舊標籤
        if (qr_val.contains("OLDSTAMP") ) {
            loai = qr210CallAPI.qr210_KT_item("1", qr_val.substring(qr_val.length() - 14, qr_val.length()));
        } else {

            //利隆極板標籤 loai = 2
            if (qr_val.substring(0, 5).equals("BC525") || qr_val.substring(0, 5).equals("BC527") || qr_val.substring(0, 5).equals("BB525") || qr_val.substring(0, 5).equals("BB527")) {
                temLC_dc = null;
                temLC_ma = null;
                temLC_ma_res = null;

                temLC_dc = qr_val.substring(0, 16);
                temLC_ma = qr_val.substring(16, 21);
                temLC_ma_res = cutString(temLC_ma);

                loai = 2;

                qr210CallAPI.qr210_LC_item(temLC_dc, temLC_ma_res);
            } else {
                //檢查是否從工單開頭
                if (qr_val.substring(5, 6).equals("-")) {
                    //先抓取工單編號 來檢查是哪個標籤類
                    String g_mdc = qr_val.substring(0, 16);
                    loai = qr210CallAPI.qr210_KT_item("2", g_mdc);
                    if (loai == -1) {
                        loai = qr210CallAPI.qr210_LL_thuMua(0, g_mdc);
                    }

                    //廣泰標籤 loai = 1
                    if (loai == 1) {
                        temKT = null;
                        mdate = null;
                        temKT_mItem = null;
                        temKT_mCode = null;

                        temKT = qr_val.substring(qr_val.length() - 14, qr_val.length());
                        mdate = temKT.substring(0, 4) + "/" + temKT.substring(4, 6) + "/" + temKT.substring(6, 8);
                        temKT_mDate = formatter.parse(mdate);
                        temKT_mItem = temKT.substring(8, 11);
                        temKT_mCode = temKT.substring(11, temKT.length());
                    }

                    //收貨標籤 loai = 3
                    if (loai == 3) {
                        temTM_donThuMua = null;
                        temTM_hangMuc = null;
                        temTM_maVatLieu = null;
                        temTM_soLo = null;
                        temTM_soLuong = null;
                        Integer j = 1;
                        Integer k = -1;

                        for (int i = 0; i <= qr_val.length(); i++) {
                            if (i == qr_val.length()) {
                                temTM_soLuong = qr_val.substring(k, qr_val.length());
                            } else {
                                if (qr_val.substring(i, i + 1).equals("_")) {
                                    switch (j) {
                                        case 1:
                                            temTM_donThuMua = qr_val.substring(0, 16);
                                            k = i + 1;
                                            break;
                                        case 2:
                                            temTM_hangMuc = qr_val.substring(k, i);
                                            k = i + 1;
                                            break;
                                        case 3:
                                            temTM_maVatLieu = qr_val.substring(k, i);
                                            k = i + 1;
                                            break;
                                        case 4:
                                            temTM_soLo = qr_val.substring(k, i);
                                            k = i + 1;
                                            break;
                                    }
                                    j += 1;
                                }

                            }
                        }

                        loai = qr210CallAPI.qr210_LL_thuMua(Integer.valueOf(temTM_hangMuc), g_mdc);
                    }

                } else {
                    loai = 0;
                }
            }

        }

        return loai;
    }

    /*private class qr210_MaCT extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        protected void onPostExecute(String result) {
            try {
                if (result.length() > 0 && !result.equals("\uFEFF[]")) {
                    JSONArray jsonarray = new JSONArray(result);
                    //mangLV = new ArrayList<qr210_listData>();
                    db210.del_db(prog);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String qra01 = jsonObject.getString("INA01"); //單據
                        String qra02 = jsonObject.getString("INA03"); //單據日期
                        String qra03 = jsonObject.getString("INA04"); //部門編號
                        String qra04 = jsonObject.getString("GEM02"); //部門名稱

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

                        db210.append(prog, qra01, qra02, qra03, qra04,
                                qrb01, qrb02, qrb03, qrb04, qrb05, qrb06,
                                qrb07, qrb08, qrb09, qrb10, qrb11, qrb12, qrb13);
                    }

                    cursor_1 = db210.getAll_1(prog);
                    cursor_2 = db210.getAll_2(prog);
                    UpdateAdapter(cursor_1, cursor_2);
                } else {
                    Toast.makeText(qr210_aimt301_asfi511.this, getString(R.string.E17), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            } finally {
                firstDetected = true;
            }
        }
    }*/

    private class qr210_Item_tem extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.contains("true")) {
                String maSachLuoc = g_string_3; //(aimi110 : imzud02)
                String maChungTu = edt_maCT.getText().toString();
                String maVatLieu = res_qr_code;
                Boolean chk_qrb = db210.check_qrb(maChungTu, maVatLieu);
                if (chk_qrb == true) { //có tồn tại MVL
                    //Kiểm tra qrb_file có tồn tại cùng MVL nhưng CHƯA dc check qrb011
                    int hangmuc = -1;
                    hangmuc = db210.check_qrb011(maChungTu, maVatLieu, false);

                    //Có phần tử chưa check
                    if (hangmuc > 0) {
                        //Cập nhật check (qrb011 false => true)
                        int res = (int) db210.upd_qrb011(maChungTu, maVatLieu, hangmuc);
                    } else { /*Có phần tử đã check */
                        //Kiểm tra trong tất cả các dòng cùng vật liệu có dòng nào chưa được chọn kho. (trả về hạng mục)
                        int c_qrb02 = db210.check_qrb06(maChungTu, maVatLieu);
                        //Nếu có dòng có cùng vật liệu mà chưa chọn kho.
                        if (maSachLuoc.equals("3")) {
                            if (c_qrb02 > 0) {
                                //Số lượng update + 1
                                db210.upd_qrb09(maChungTu, c_qrb02);
                            } else {
                                if (chk_insert_new == 1) {
                                    insert_new_row_to_qrb_file(res_qr_code, 1);
                                }
                            }
                        } else {
                            if (chk_insert_new == 1) {
                                insert_new_row_to_qrb_file(res_qr_code, 0);
                            }
                        }
                    }

                } else { //Không tồn tại
                    if (maSachLuoc.equals("3")) {
                        if (chk_insert_new == 1) {
                            insert_new_row_to_qrb_file(res_qr_code, 1);
                        }
                    } else {
                        if (chk_insert_new == 1) {
                            insert_new_row_to_qrb_file(res_qr_code, 0);
                        }
                    }
                }

                qr210Refect(true);

                firstDetected = true;
            } else {
                res_qr_code = null;
                g_string_1 = null;
                g_string_2 = null;
                g_string_3 = null;
                firstDetected = true;
            }
        }

        private void insert_new_row_to_qrb_file(String g_mvl, int r_qrb09) {
            //Tìm số dòng lón nhất  + 1 để tính số thứ tự tiếp theo (S)
            Cursor cur_count = db210.max_qrb02(prog,
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")));
            cur_count.moveToFirst();
            int max_qrb02 = Integer.parseInt(cur_count.getString(cur_count.getColumnIndexOrThrow("MAX")));
            //Tìm số dòng lón nhất  + 1 để tính số thứ tự tiếp theo (E)

            //Lưu dữ liệu
            db210.append(prog,
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra02")),
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra03")),
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra04")),
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra05")),
                    cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                    max_qrb02 + 1,
                    g_mvl,
                    null, null, null,
                    null, null, r_qrb09,
                    null, "true", null, 0);

            //lấy thông tin vật liệu (S)
            if (g_string_3 != null) {
                db210.upd_qrb_item_data(cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                        max_qrb02 + 1, g_string_1, g_string_2, g_string_3);
            }
            //lấy thông tin vật liệu (E)
        }
    }

    private final ListView.OnItemClickListener lvListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            //check fragment có mở hay không và đóng lại trước khi mở cái mới
            qr210_close_fragment("qr210fmKho", true);
            qr210_close_fragment("qr210fmSL", true);

            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            //Menu menu =  popup.getMenu();
            popup.getMenuInflater().inflate(R.menu.qr210_lv_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return menuItemClicked(item, position);

                }
            });
            // Show the PopupMenu.
            popup.show();
        }

        private boolean menuItemClicked(MenuItem item, int position) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            cursor_2.moveToPosition(position);

            String ready = cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb11"));
            Integer chk_qrb13 = Integer.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb13")));

            Bundle bundle = new Bundle();
            bundle.putString("MVL", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb03"))));
            bundle.putString("TEN", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb04"))));
            bundle.putString("QC", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb05"))));
            bundle.putString("KHO", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb06"))));
            bundle.putString("VITRI", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb07"))));
            bundle.putString("SOLO", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb08"))));
            bundle.putString("SL", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb09"))));
            bundle.putString("DV", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))));
            bundle.putString("PROG", String.valueOf(prog));
            bundle.putString("MCT", String.valueOf(edt_maCT.getText().toString().trim()));
            bundle.putString("SACHLUOC", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb12"))));
            bundle.putString("HANGMUC", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb02"))));
            bundle.putString("SERVER", String.valueOf(g_server));

            switch (item.getItemId()) {
                case R.id.change_quantity:
                    if (ready.equals("true")) {
                        //檢查發料單據是否已過帳 Kiểm tra phiếu đã sang sổ hay chưa ?
                        String g_qra05 = String.valueOf(cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra05")));
                        if (g_qra05.equals("N")) {
                            //檢查庫存數量不能空 Số lượng tồn kho không được = 0
                            if (chk_qrb13 > 0) {
                                change_quantity_open(position);
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.E14), Toast.LENGTH_SHORT).show();
                                firstDetected = true;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.E22), Toast.LENGTH_SHORT).show();
                            firstDetected = true;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.M18), Toast.LENGTH_SHORT).show();
                        firstDetected = true;
                    }

                    break;

                case R.id.open_warehouse_list:
                    if (ready.equals("true")) {
                        /*
                        Intent intent = new Intent();
                        intent.setClass(qr210.this, qr210_vt_view.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("MVL", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb03"))));
                        bundle.putString("TEN", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb04"))));
                        bundle.putString("QC", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb05"))));
                        bundle.putString("KHO", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb06"))));
                        bundle.putString("VITRI", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb07"))));
                        bundle.putString("SOLO", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb08"))));
                        bundle.putString("SL", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb09"))));
                        bundle.putString("DV", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))));
                        bundle.putString("PROG", String.valueOf(prog));
                        bundle.putString("MCT", String.valueOf(edt_maCT.getText().toString().trim()));
                        bundle.putString("SACHLUOC", String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb12"))));
                        intent.putExtras(bundle);

                        save_qrb02 = String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb02")));
                        //startActivity(intent);
                        startActivityForResult(intent, qr210_vt_view.travetu_qr210_vt_view); //Sử dụng phương thức này để có chuyển truyền trả dữ liệu giữ các activity
                        */

                        /*
                        //Fragment (S) (use android.app)
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        qr210_Fragment_Kho qr210FragmentKho = new qr210_Fragment_Kho();
                        fragmentTransaction.add(R.id.frameContent,qr210FragmentKho);
                        fragmentTransaction.commit();
                        //Fragment (E) (use android.app)
                        */

                        /*qr210_Fragment_Kho qr210FragmentKho = (qr210_Fragment_Kho) getFragmentManager().findFragmentById(R.id.fragmentKho);
                        qr210FragmentKho.ganTieuDe(
                                String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb03"))),
                                String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb09"))),
                                String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))),
                                String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb04"))),
                                String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb05")))
                        );*/

                        //檢查發料單據是否已過帳 Kiểm tra phiếu đã sang sổ hay chưa ?
                        String g_qra05 = String.valueOf(cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra05")));
                        if (!g_qra05.equals("Y")) {
                            //Fragment truyền dữ liệu (S)
                            qr210_Fragment_Kho qr210FragmentKho = new qr210_Fragment_Kho();
                            qr210FragmentKho.setArguments(bundle);

                            //Mở fragment
                            fragmentTransaction.add(R.id.frameContent, qr210FragmentKho, "qr210fmKho");
                            fragmentTransaction.addToBackStack("qr210fmKho_BS");
                            fragmentTransaction.commit();
                            //Fragment truyền dữ liệu (E)
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.E22), Toast.LENGTH_SHORT).show();
                            firstDetected = true;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.M18), Toast.LENGTH_SHORT).show();
                        firstDetected = true;
                    }

                    break;

                case R.id.del_lv_item:
                    //檢查發料單據是否已過帳 Kiểm tra phiếu đã sang sổ hay chưa ?
                    String g_qra05 = String.valueOf(cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra05")));
                    if (!g_qra05.equals("Y")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.M05);
                        //builder.setMessage(qr300_code);
                        builder.setMessage(getString(R.string.M15));
                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        qr210.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Delete row (S)
                                                long res = -1;
                                                //lưu lại hạng mục đã xóa
                                                res = db210.ins_deltable(
                                                        prog,
                                                        cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                                                        cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb02"))
                                                );
                                                if (res == 1) {
                                                    //tiến hành xóa trên giao diện
                                                    db210.del_qrb(
                                                            prog,
                                                            cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                                                            cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb02"))
                                                    );
                                                } else {
                                                    Toast.makeText(qr210.this, getString(R.string.M23), Toast.LENGTH_SHORT).show();
                                                }
                                                //Delete row (E)

                                                //Update lại stt (S)
                                                ////db210.upd_qrb02(prog, cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")));
                                                qr210Refect(true);
                                                //Update lại stt (E)
                                            }
                                        });
                                    }
                                }).start();
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.E22), Toast.LENGTH_SHORT).show();
                        firstDetected = true;
                    }

                    break;
                default:
                    break;
            }

            return true;
        }

    };

    //Phương thức nhận dữ liệu trả về từ activity kho (qr210_vt_view) (S)
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == qr210_vt_view.travetu_qr210_vt_view) {
            String s = data.getStringExtra("qr210_st");
            if (s.equals("kho_change")) {
                //Delete dòng dữ liệu cũ trước khi insert mới (S)
                int chek_del = db210.del_qrb(prog,
                        cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                        save_qrb02);
                //Delete dòng dữ liệu cũ trước khi insert mới (E)

                if (chek_del == 1) {
                    cursor_img = db210.getAll_3(prog);      //Truy vấn img_file
                    cursor_img.moveToFirst();               //Di chuyển con trỏ về hàng đầu tiên
                    int img_count = cursor_img.getCount();  //Lấy tổng số dòng cân insert =) để chạy vòng lập for

                    //Tìm số dòng lón nhất  + 1 để tính số thứ tự tiếp theo (S)
                    Cursor cur_count = db210.max_qrb02(prog,
                            cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")));
                    cur_count.moveToFirst();
                    int max_qrb02 = Integer.parseInt(cur_count.getString(cur_count.getColumnIndexOrThrow("MAX")));
                    //Tìm số dòng lón nhất  + 1 để tính số thứ tự tiếp theo (E)

                    for (int cur = 1; cur <= img_count; cur += 1) {
                        max_qrb02 += 1;
                        db210.append(prog,
                                cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                                cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra02")),
                                cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra03")),
                                cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra04")),

                                cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01")),
                                max_qrb02,
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("img01")),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("imgten")),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("imgqc")),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("img02")),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("img03")),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("img04")),
                                Double.parseDouble(cursor_img.getString(cursor_img.getColumnIndexOrThrow("img10"))),
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("img09")),
                                "true",
                                cursor_img.getString(cursor_img.getColumnIndexOrThrow("imgsachluoc")),
                                0
                        );

                        cursor_img.moveToNext();
                    }

                    db210.upd_qrb02(prog, cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra01"))); //Update lại stt
                    cursor_2 = db210.getAll_2(prog);
                    UpdateAdapter(cursor_1, cursor_2);
                }
            }
        }
    }*/
    //Phương thức nhận dữ liệu trả về từ activity kho (qr210_vt_view) (E)

    //Khởi tạo menu trên thanh tiêu đề (S)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qr210_option_popup_menu, menu);

        // Get the action view used in your toggleservice item
        final MenuItem toggleservice = menu.findItem(R.id.toggleservice);
        final Switch actionView = (Switch) toggleservice.getActionView();
        actionView.setText(getString(R.string.switchDes));
        actionView.setTextOff(getString(R.string.switchOff));
        actionView.setTextOn(getString(R.string.switchOn));
        actionView.setShowText(true);

        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chk_insert_new = 1;
                } else {
                    chk_insert_new = 0;
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_data:
                if (edt_maCT.length() == 0) {
                    Toast.makeText(qr210.this, getString(R.string.E16), Toast.LENGTH_SHORT).show();
                    break;
                }
                trans_save_data_qr210("N", null); //拋轉 與保存資料到系統
                break;
            case R.id.update_data:
                if (edt_maCT.length() == 0) {
                    Toast.makeText(qr210.this, getString(R.string.E16), Toast.LENGTH_SHORT).show();
                    break;
                }
                /* Mở tạm cho kỹ thuật check */
                //檢查發料單據是否已過帳 Kiểm tra phiếu đã sang sổ hay chưa ?
                String g_qra05 = String.valueOf(cursor_1.getString(cursor_1.getColumnIndexOrThrow("qra05")));
                if (g_qra05.equals("N")) {
                    try {
                        UpdateDialog(edt_maCT.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.E22), Toast.LENGTH_SHORT).show();
                    firstDetected = true;
                }
                /*TẠM không kiểm tra khi chuyển dữ liệu
                //在名冊 檢查是否有物料未掃描
                int chk_num = db210.check_qrb011_trans(edt_maCT.getText().toString(), false);
                if (chk_num == 0) {
                    //在名冊 檢查是否有物料未選倉庫
                    chk_num = db210.check_qrb06_trans(edt_maCT.getText().toString());
                    if (chk_num == 0) {
                        //在名冊 檢查是否有庫存
                        chk_num = db210.check_qrb13_trans(edt_maCT.getText().toString());
                        if (chk_num == 0) {
                            //在名冊 檢查是否有物料未輸入數量
                            chk_num = db210.check_qrb09_trans(edt_maCT.getText().toString());
                            if (chk_num == 0) {
                                //在名冊 檢查輸入數量是否超過庫存
                                chk_num = db210.check_qrb09_qrb13(edt_maCT.getText().toString());
                                if (chk_num == 0) {
                                    try {
                                        UpdateDialog();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(this, getString(R.string.E19), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.E15), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.E14), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.E13), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.E12), Toast.LENGTH_SHORT).show();
                }

                 */
                break;
            case R.id.clear_data:
                if (edt_maCT.length() == 0) {
                    Toast.makeText(qr210.this, getString(R.string.E16), Toast.LENGTH_SHORT).show();
                    break;
                }
                clear_data_qr210();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void change_quantity_open(int position) {
        TextView tv_MVL, tv_Ten, tv_QuyCach, tv_SoLuongGoc, tv_SoLuongTonKho, tv_DonVi1, tv_DonVi2, tv_DonVi3;
        EditText tv_SoLuongSua;
        Button btnThoat, btnXacNhan;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.qr210_change_quantity);
        firstDetected = false;
        cursor_2.moveToPosition(position);

        //Ánh xạ
        tv_MVL = dialog.findViewById(R.id.tv_MVL);
        tv_Ten = dialog.findViewById(R.id.tv_Ten);
        tv_QuyCach = dialog.findViewById(R.id.tv_QuyCach);
        tv_SoLuongGoc = dialog.findViewById(R.id.tv_SoLuongGoc);
        tv_SoLuongSua = dialog.findViewById(R.id.tv_SoLuongSua);
        tv_SoLuongTonKho = dialog.findViewById(R.id.tv_SoLuongTonKho);
        tv_DonVi1 = dialog.findViewById(R.id.tv_DonVi1);
        tv_DonVi2 = dialog.findViewById(R.id.tv_DonVi2);
        tv_DonVi3 = dialog.findViewById(R.id.tv_DonVi3);
        btnThoat = dialog.findViewById(R.id.btnThoat);
        btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

        tv_MVL.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb03"))));
        tv_Ten.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb04"))));
        tv_QuyCach.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb05"))));
        tv_SoLuongGoc.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb09"))));
        tv_SoLuongTonKho.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb13"))));
        tv_DonVi1.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))));
        tv_DonVi2.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))));
        tv_DonVi3.setText(String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb10"))));
        tv_SoLuongSua.setHint("0");

        tv_SoLuongSua.setSelection(tv_SoLuongSua.getText().length());

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor_2.moveToPosition(position);

                Integer chge, g_qrb013;
                if (tv_SoLuongSua.getText().toString().equals("")) {
                    chge = 0;
                } else {
                    chge = Integer.parseInt(tv_SoLuongSua.getText().toString());
                }

                //庫存數量 Số lượng tồn kho
                if (tv_SoLuongTonKho.getText().toString().equals("")) {
                    g_qrb013 = 0;
                } else {
                    g_qrb013 = Integer.parseInt(tv_SoLuongTonKho.getText().toString());
                }

                //檢查是否超過庫存 Kiểm tra có vượt số lượng tồn kho
                if (chge > 0 && chge <= g_qrb013) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
                    builder.setCancelable(false);
                    builder.setTitle(R.string.T02);
                    //builder.setMessage(qr300_code);
                    builder.setMessage(getString(R.string.M24) + "\n" + getString(R.string.slUses) + " :  " + tv_SoLuongGoc.getText().toString().trim() + " --> " + chge);
                    builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //insert số lượng vào db
                            db210.upd_qrb09(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb01")),
                                    String.valueOf(cursor_2.getString(cursor_2.getColumnIndexOrThrow("qrb02"))),
                                    chge);

                            qr210Refect(true);
                            dialog.dismiss();
                            firstDetected = true;
                        }
                    });
                    builder.show();

                } else {
                    if (chge <= 0) {
                        Toast.makeText(qr210.this, R.string.E15, Toast.LENGTH_SHORT).show();
                    } else if (chge > g_qrb013) {
                        Toast.makeText(qr210.this, R.string.E21, Toast.LENGTH_SHORT).show();
                    }
                    firstDetected = true;
                }
            }
        });


        dialog.show();
    }

    private void UpdateDialog(String s) throws IOException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.qr210_update_dialog);
        cameraSource.stop();
        cameraSource.release();
        firstDetected_upload = true;
        firstDetected = false;

        //Ánh xạ
        edtUser = dialog.findViewById(R.id.edtUser);
        tvUser = dialog.findViewById(R.id.tvUser);
        tvStatus = dialog.findViewById(R.id.tvStatus);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnOK = dialog.findViewById(R.id.btnOK);

        tvStatus.setText(null);
        chk_dialog = 1; //open
        surfaceView_upload = (SurfaceView) dialog.findViewById(R.id.suvqr210_upload);
        CameraSetting(2);

        //Lấy thông tin nhân viên nhận hàng 依單據擷取收料人員
        if (prog.equals("aimt301")) {
            String g_sql = "SELECT ina11 FROM ina_file WHERE ina01 = '" + s + "'";
            qr210CallAPI.qr210_loadInfo("0", g_sql);
        }


        edtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtUser.getText().toString().trim().length() > 0) {
                    getUserCode(edtUser.getText().toString().trim());
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                chk_dialog = -1; //close
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                firstDetected_upload = false;
                firstDetected = true;
                cameraSource_upload.stop();
                cameraSource_upload.release();
                chk_dialog = -1; //close
                check_user = -1;
                setContentView(R.layout.qr210_aimt301_asfi511);
                addcontrols();
                addEvent();
                qr210Refect(true);
                CameraSetting(1);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstDetected_upload = false;
                firstDetected = true;
                cameraSource_upload.stop();
                cameraSource_upload.release();
                check_user = -1;
                dialog.dismiss();
                setContentView(R.layout.qr210_aimt301_asfi511);
                addcontrols();
                addEvent();
                qr210Refect(true);
                CameraSetting(1);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStatus.setText("");
                if (check_user == 1) {
                    trans_save_data_qr210("Y", comfirmID); //拋轉 與保存資料到系統
                } else {
                    tvStatus.setText(getString(R.string.E20));
                    //Toast.makeText(qr210_aimt301.this, getString(R.string.E20), Toast.LENGTH_SHORT).show();
                    check_user = -1;
                }
            }
        });

        dialog.show();
    }

    private void getUserCode(String qr210_userCode) {
        try {
            final String qr_val = qr210_userCode.trim();
            new scan_user().execute("http://172.16.40.20/" + g_server + "/QR210/getID_qr210.php?ID=" + qr_val);
        } catch (Exception e) {
            firstDetected_upload = true;
        }
    }

    private class scan_user extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.length() > 0 && !s.contains("false")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String cpf01 = jsonObject.getString("CPF01");
                    String cpf02 = jsonObject.getString("CPF02");
                    String ta_cpf001 = jsonObject.getString("TA_CPF001");
                    String cpf29 = jsonObject.getString("CPF29");
                    String gem02 = jsonObject.getString("GEM02");

                    tvUser.setText(cpf01 + " : " + ta_cpf001 + "\n" + cpf29 + " : " + gem02);
                    comfirmID = cpf01;
                    check_user = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                    firstDetected_upload = true;
                }
            } else {
                tvUser.setText("");
                check_user = -1;
            }
        }
    }

    private void trans_save_data_qr210(final String g_upload, final String confirmID) { //拋轉與保存資料到系統
        AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.M01);
        //builder.setMessage(qr300_code);
        builder.setMessage(getString(R.string.M02));
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firstDetected = true;
            }
        });

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        //tham số Y , biểu thị cập nhật dữ liệu tới chương trình gốc, và save đến qrf_file
                        Cursor upl = db210.getAll_qra_qrb(ID, String.valueOf(dateFormat.format(date)), g_upload, confirmID);
                        jsonupload = cur2Json(upl);

                        //lấy dữ liệu đã xóa
                        Cursor del_upl = db210.getAll_4(prog);
                        jsonDelUpload = cur2Json(del_upl);

                        try {
                            ujobject = new JSONObject();
                            ujobject.put("docNum", edt_maCT.getText().toString());
                            ujobject.put("djson", jsonDelUpload); //del data
                            ujobject.put("ujson", jsonupload); //update data
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        final String res = upload_all("http://172.16.40.20/" + g_server + "/QR210/upload_qr210.php");
                        runOnUiThread(new Runnable() { //Vì Toast không thể chạy đc nếu không phải UI Thread nên sử dụng runOnUIThread.
                            @Override
                            public void run() {
                                if (res.contains("false")) {
                                    //Toast.makeText(getApplicationContext(), getString(R.string.M09), Toast.LENGTH_SHORT).show();
                                    if (chk_dialog == 1) {
                                        tvStatus.setText(getString(R.string.M09));
                                        tvStatus.setTextColor(getResources().getColor(R.color.red));
                                    }
                                } else {
                                    //Toast.makeText(getApplicationContext(), getString(R.string.M08), Toast.LENGTH_SHORT).show();
                                    if (chk_dialog == 1) {
                                        tvStatus.setText(getString(R.string.M08));
                                        tvStatus.setTextColor(getResources().getColor(R.color.purple_500));
                                        db210.del_db(prog);
                                        db210.del_db_deltable(prog); //刪除del_table資料
                                    }
                                }
                            }
                        });

                    }
                }).start();
            }
        });
        builder.show();
    }

    //更新listview
    public void UpdateAdapter(Cursor cursor_a, Cursor cursor_b) {
        try {
            if (cursor_a != null && cursor_a.getCount() >= 0) {
                if (cursor_a != null && cursor_a.getCount() > 0) {
                    cursor_a.moveToFirst();
                    edt_maCT.setText(cursor_a.getString(cursor_a.getColumnIndexOrThrow("qra01")));
                    tv_ngayCT.setText(cursor_a.getString(cursor_a.getColumnIndexOrThrow("qra02")));
                    tv_mabpCT.setText(cursor_a.getString(cursor_a.getColumnIndexOrThrow("qra03")));
                    tv_tenbpCT.setText(cursor_a.getString(cursor_a.getColumnIndexOrThrow("qra04")));

                    /*SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, R.layout.qr210_view, cursor_b,
                            new String[]{"qrb02", "qrb03", "qrb04", "qrb05", "qrb06", "qrb07", "qrb08", "qrb09", "qrb10", "qrb11"},
                            new int[]{R.id.qrb02, R.id.qrb03, R.id.qrb041, R.id.qrb042, R.id.qrb051, R.id.qrb052, R.id.qrb053, R.id.qrb061, R.id.qrb062, R.id.qrb11}, 0);*/

                    qr210_updateAdapter adapter2 = new qr210_updateAdapter(this, R.layout.qr210_aimt301_asfi511_row, cursor_b,
                            new String[]{"qrb02", "qrb03", "qrb04", "qrb05", "qrb06", "qrb07", "qrb08", "qrb09", "qrb10", "qrb13"},
                            new int[]{R.id.qrb02, R.id.qrb03, R.id.qrb041, R.id.qrb042, R.id.qrb051, R.id.qrb052, R.id.qrb053, R.id.qrb061, R.id.qrb062, R.id.qrb054}, 0);

                    lv_multiple_selection.setAdapter(adapter2);

                } else {
                    edt_maCT.setText(null);
                    tv_ngayCT.setText(null);
                    tv_mabpCT.setText(null);
                    tv_tenbpCT.setText(null);
                    lv_multiple_selection.setAdapter(null);
                    firstDetected = true;
                }
            }
        } catch (Exception e) {
            Toast alert = Toast.makeText(getApplicationContext(), getString(R.string.E04) + e, Toast.LENGTH_LONG);
            alert.show();
        } finally {
            firstDetected = true;
        }
    }

    //上傳資料
    public String upload_all(String apiUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(999999);
            conn.setReadTimeout(999999);
            conn.setDoInput(true); //允許輸入流，即允許下載
            conn.setDoOutput(true); //允許輸出流，即允許上傳

            OutputStream os = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);
            writer.write(ujobject.toString().getBytes("UTF-8"));
            writer.flush();
            writer.close();
            os.close();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String result = reader.readLine();
            reader.close();
            return result;
        } catch (Exception ex) {
            return "false";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void clear_data_qr210() {
        AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //builder.setTitle("@string/M10")
        builder.setMessage(getString(R.string.M10));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //執行清空(S)
                db210.del_db(prog); //刪除db資料
                db210.del_db_deltable(prog); //刪除del_table資料
                qr210Refect(true);
                //執行清空(E)
            }
        })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    //Khởi tạo menu trên thanh tiêu đề (E)

    private void CameraSetting(int i) {
        if (i == 1) {
            surfaceView = (SurfaceView) findViewById(R.id.suvqr210);
            //barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS | Barcode.CODE_128 | Barcode.QR_CODE).build();
            barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
            cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(300, 300).build();
            cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED)
                        return;
                    try {

                        cameraSource.start(surfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                    if (qrCodes.size() != 0 && firstDetected) {
                        firstDetected = false;
                        final String qr210_code = qrCodes.valueAt(0).displayValue.trim();
                        getcode(qr210_code);
                    }
                }
            });

            cursor_1 = db210.getAll_1(prog);
            cursor_2 = db210.getAll_2(prog);
            UpdateAdapter(cursor_1, cursor_2);

        } else {
            barcodeDetector_upload = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
            cameraSource_upload = new CameraSource.Builder(this, barcodeDetector_upload).setRequestedPreviewSize(300, 300).build();
            cameraSource_upload = new CameraSource.Builder(this, barcodeDetector_upload).setAutoFocusEnabled(true).build();

            surfaceView_upload.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED)
                        return;
                    try {

                        cameraSource_upload.start(surfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource_upload.stop();
                }
            });

            barcodeDetector_upload.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                    if (qrCodes.size() != 0 && firstDetected_upload) {
                        OKPool.play(oksound, 1, 1, 0, 0, 1);
                        firstDetected_upload = false;
                        final String qr210_code = qrCodes.valueAt(0).displayValue;
                        getUserCode(qr210_code);
                    }
                }
            });
        }
    }

    private void addcontrols() {
        lv_multiple_selection = (ListView) findViewById(R.id.lv_multiple_selection);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        edt_maCT = (EditText) findViewById(R.id.edt_maCT);
        tv_ngayCT = (TextView) findViewById(R.id.tv_ngayCT);
        tv_mabpCT = (TextView) findViewById(R.id.tv_mabpCT);
        tv_tenbpCT = (TextView) findViewById(R.id.tv_tenbpCT);

        Bundle getBundle = getIntent().getExtras();
        ID = Constant_Class.UserID;
        g_server = Constant_Class.server;
        prog = getBundle.getString("prog");



        //修改畫面標題 Thay đổi tiêu đề của giao diện (S)
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getString(R.string.asfi511_menu)); //Thiết lập tiêu đề
        if (prog.equals("aimt301")) {
            actionBar.setTitle(getString(R.string.str_scanview_1) + " - " + getString(R.string.aimt301_menu)); //Thiết lập tiêu đề
        }
        //String title = actionBar.getTitle().toString(); //Lấy tiêu đề
        //actionBar.hide(); //Ẩn ActionBar
        //修改畫面標題 Thay đổi tiêu đề của giao diện (E)
    }

    private void addEvent() {
        lv_multiple_selection.setOnItemClickListener(lvListener);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_maCT = null;
                g_maCT = edt_maCT.getText().toString().trim();
                if (g_maCT.length() >= 6) {
                    final String subqr = g_maCT.substring(5, 6);
                    if (subqr.equals("-") && g_maCT.length() == 16) {
                        new Thread() {
                            public void run() {
                                qr210.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(qr210.this);
                                        builder.setCancelable(false);
                                        builder.setTitle(R.string.T02);
                                        //builder.setMessage(qr300_code);
                                        builder.setMessage(getString(R.string.M14) + " " + edt_maCT.getText().toString().trim());
                                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                firstDetected = true;
                                            }
                                        });

                                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //執行清空(S)
                                                db210.del_db(prog); //刪除db資料
                                                db210.del_db_deltable(prog); //刪除del_table資料
                                                qr210Refect(true);
                                                //執行清空(E)

                                                qr210CallAPI.qr210_MaCT(g_maCT, prog);
                                                firstDetected = true;
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                        }.start();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.E17), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast alert = Toast.makeText(getApplicationContext(), getString(R.string.E17), Toast.LENGTH_LONG);
                    alert.show();
                }
            }
        });


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
                content.append(line + "\n");
                //content.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            firstDetected = true;
            e.printStackTrace();
        }
        return content.toString();
    }

    //Cursor 轉 Json
    public JSONArray cur2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }

    //cut strings
    private String cutString(String temLC_ma) {
        String[] s1 = temLC_ma.split("_");
        return s1[1];
    }

    @Override
    public void qr210Refect(boolean ref) {
        if (ref) {
            cursor_1 = db210.getAll_1(prog);
            cursor_2 = db210.getAll_2(prog);
            UpdateAdapter(cursor_1, cursor_2);
        }
    }

    @Override
    public void qr210_close_fragment(String kind, boolean close) {
        if (close) {
            //qr210fmKho : Kho ; qr210fmSL : sửa Số lượng
            android.app.Fragment fragment = getFragmentManager().findFragmentByTag(kind);
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }

    @Override
    public void qr210_TaskComplete(String kind, String val) {
        if (kind.equals("KT") || kind.equals("LC") || kind.equals("TM")) {
            if (val.length() > 0) {
                res_qr_code = val;
            }
        } else if (kind.equals("GID")) {
            if (val.length() > 0) {
                try {
                    JSONArray jsonarray = new JSONArray(val);
                    JSONObject jsonObject = jsonarray.getJSONObject(0);
                    g_string_1 = jsonObject.getString("TA_IMA02_1"); //品名
                    g_string_2 = jsonObject.getString("TA_IMA021_1"); //規格
                    g_string_3 = jsonObject.getString("IMZUD02"); //策略
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void qr210_Toast(String ErrCode, String Mess) {
        if (ErrCode.length() > 0) {
            int getID = getResources().getIdentifier(ErrCode, "string", qr210.this.getPackageName());
            String errCode = getString(getID);
            Toast.makeText(this, Mess + "  " + errCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void qr210_getIna11(String g_ina11) {
        if (g_ina11.length() > 0) {
            getUserCode(g_ina11);
        }
    }

    //設定語言
    private void setLanguage() {
        SharedPreferences preferences = getSharedPreferences("Language", Context.MODE_PRIVATE);
        int language = preferences.getInt("Language", 0);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        switch (language) {
            case 0:
                configuration.setLocale(Locale.TRADITIONAL_CHINESE);
                break;
            case 1:
                locale = new Locale("vi");
                Locale.setDefault(locale);
                configuration.setLocale(locale);
                break;
            case 2:
                locale = new Locale("en");
                Locale.setDefault(locale);
                configuration.setLocale(locale);
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}