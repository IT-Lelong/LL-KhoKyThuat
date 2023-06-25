package com.lelong.ll_khokythuat.QR210.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.lelong.ll_khokythuat.QR210.Adapter.qr210_checkAdapter;
import com.lelong.ll_khokythuat.QR210.Model.qr210_check_ListData;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class qr210_check extends AppCompatActivity {
    String g_server = "";
    TextView tvNgay, tvBoPhan;
    EditText edtMaCT;
    Button btnOkCheck;
    ListView lvQr210Check;

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    boolean firstDetected = true;

    qr210_checkAdapter qr210CheckAdapter;
    ArrayList<qr210_check_ListData> dsDonCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr210_check);
        addcontrols();
        addevents();

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
                    final String qr210_code = qrCodes.valueAt(0).displayValue;
                    getcode(qr210_code);
                }
            }
        });
    }

    private void getcode(final String qr210_code) {
        try {
            final String qr_val = qr210_code.trim();
            final String subqr = qr_val.substring(5, 6);
            new Thread() {
                public void run() {
                    qr210_check.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (subqr.equals("-") && qr_val.length() == 16) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(qr210_check.this);
                                builder.setCancelable(false);
                                builder.setTitle("CODE VALUE");
                                //builder.setMessage(qr300_code);
                                builder.setMessage(getString(R.string.M14) + " " + qr_val);
                                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firstDetected = true;
                                    }
                                });

                                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    String prog = "qrf_file";

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new qr210_MaCT().execute("http://172.16.40.20/" + g_server + "/QR210/getdata_qr210.php?qr01=" + qr_val + "&prog=" + prog);
                                        //firstDetected = true;
                                    }
                                });
                                builder.show();
                            } else {
                                Toast.makeText(getApplicationContext(), "chua co don chung tu", Toast.LENGTH_LONG).show();
                                firstDetected = true;
                            }
                        }

                    });
                }
            }.start();
        } catch (
                Exception e) {
            Toast alert = Toast.makeText(getApplicationContext(), "ERROR" + e, Toast.LENGTH_LONG);
            alert.show();
            firstDetected = true;
        } finally {
        }
    }

    private class qr210_MaCT extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        protected void onPostExecute(String result) {
            String qra01 = null,qra02 = null,qra03 = null,qra04 = null;
            try {
                if (result.length() > 0) {
                    JSONArray jsonarray = new JSONArray(result);
                    //mangLV = new ArrayList<qr210_listData>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        qra01 = jsonObject.getString("INA01"); //單據
                        qra02 = jsonObject.getString("INA03"); //單據日期
                        qra03 = jsonObject.getString("INA04"); //部門編號
                        qra04 = jsonObject.getString("GEM02"); //部門名稱

                        String qrb01 = jsonObject.getString("INA01"); //單據
                        int qrb02 = jsonObject.getInt("INB03"); //項目
                        String qrb03 = jsonObject.getString("INB04"); //料號
                        String qrb04 = jsonObject.getString("TA_IMA02_1"); //品名
                        String qrb05 = jsonObject.getString("TA_IMA021_1"); //規格

                        String qrb06 = jsonObject.getString("INB05"); //倉庫
                        String qrb07 = jsonObject.getString("INB06"); //儲位
                        String qrb08 = jsonObject.getString("INB07"); //批號
                        int qrb09 = jsonObject.getInt("INB09"); //數量

                        String qrb10 = jsonObject.getString("INB08"); //單位
                        String qrb11 = jsonObject.getString("SCAN"); //掃描否
                        String qrb12 = jsonObject.getString("IMZUD02"); //策略
                        String qrb13_t = jsonObject.getString("IMG10"); //庫存量
                        int qrb13;
                        if (qrb13_t.equals("null") || qrb13_t == null) {
                            qrb13 = 0;
                        } else {
                            qrb13 = Integer.parseInt(qrb13_t);
                        }

                        boolean qrb11_t;
                        if (qrb11.equals("true")) {
                            qrb11_t = true;
                        } else {
                            qrb11_t = false;
                        }

                        dsDonCT.add(new qr210_check_ListData(qrb02, qrb03, qrb04, qrb05, qrb06, qrb07, qrb08, qrb13, qrb09, qrb10, qrb11_t));
                    }

                    edtMaCT.setText(qra01);
                    tvNgay.setText(qra02);
                    tvBoPhan.setText(qra03 + "-" + qra04);
                    qr210CheckAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(qr210_check.this, "Không có dữ liệu chứng từ hoặc đã sang sổ", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            } finally {
                firstDetected = true;
            }
        }
    }


    private void addevents() {

    }

    private void addcontrols() {
        Bundle getBundle = getIntent().getExtras();
        g_server = getBundle.getString("SERVER");

        surfaceView = (SurfaceView) findViewById(R.id.suvqr210);
        tvNgay = findViewById(R.id.tvNgay);
        tvBoPhan = findViewById(R.id.tvBoPhan);
        edtMaCT = findViewById(R.id.edtMaCT);
        btnOkCheck = findViewById(R.id.btnOkCheck);
        lvQr210Check = findViewById(R.id.lvQr210Check);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.CODE_128 | Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(300, 300).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).build();

        dsDonCT = new ArrayList<>();

        qr210CheckAdapter = new qr210_checkAdapter(this,
                R.layout.qr210_aimt301_asfi511_row,
                dsDonCT);

        lvQr210Check.setAdapter(qr210CheckAdapter);

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
}