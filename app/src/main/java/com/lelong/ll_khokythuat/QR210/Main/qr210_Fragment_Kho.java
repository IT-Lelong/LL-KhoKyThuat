package com.lelong.ll_khokythuat.QR210.Main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lelong.ll_khokythuat.QR210.Adapter.qr210_Fragment_Kho_Adapter;
import com.lelong.ll_khokythuat.QR210.Model.qr210_Fragment_Kho_ListData;
import com.lelong.ll_khokythuat.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class qr210_Fragment_Kho extends Fragment {
    String g_server = "";
    private qr210DB db210 = null;
    TextView tv_MVL, tv_SL, tv_Name;
    Button btnXacNhan, btnThoat;

    String XUONG, LOAI;
    int preCheck = -1;
    String re_kho, re_vitri, re_solo, re_sltonkho, re_donvi;

    Bundle bundle;
    ListView lv_fragment_kho;
    ArrayList<qr210_Fragment_Kho_ListData> qr210FragmentKhoListData;
    qr210_Fragment_Kho_Adapter qr210FragmentKhoAdapter;
    qr210_interface qr210Interface;

    private View.OnClickListener btnListenner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr210_fragment_kho, container, false);
        /*tv_MVL = view.findViewById(R.id.tv_MVL);
        tv_Sl = view.findViewById(R.id.tv_SL);
        tv_Name = view.findViewById(R.id.tv_Name);*/

        addcontrols(view);
        addEvent();
        qr210Interface = (qr210_interface) getActivity();

        btnXacNhan.setOnClickListener(btnListenner);
        btnThoat.setOnClickListener(btnListenner);


        lv_fragment_kho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qr210_Fragment_Kho_ListData listData = qr210FragmentKhoListData.get(position);
                if (listData.isCheckBox() == false) {
                    listData.setCheckBox(true);
                    re_kho = listData.getTv_fm_kho_kho();
                    re_vitri = listData.getTv_fm_kho_vitri();
                    re_solo = listData.getTv_fm_kho_solo();
                    re_sltonkho = listData.getTv_fm_kho_soluong();
                    re_donvi = listData.getTv_fm_kho_donvi();
                    ;
                    qr210FragmentKhoListData.set(position, listData);
                    if (preCheck > -1) {
                        qr210_Fragment_Kho_ListData listDataPreCheck = qr210FragmentKhoListData.get(preCheck);
                        listDataPreCheck.setCheckBox(false);
                        qr210FragmentKhoListData.set(preCheck, listDataPreCheck);
                    }
                    preCheck = position;
                    qr210FragmentKhoAdapter.UpdateAdapter(qr210FragmentKhoListData);
                } else {
                    listData.setCheckBox(false);
                    qr210FragmentKhoListData.set(position, listData);
                    re_kho = null;
                    re_vitri = null;
                    re_solo = null;
                    re_sltonkho = null;
                    re_donvi = null;
                    preCheck = -1;
                    qr210FragmentKhoAdapter.UpdateAdapter(qr210FragmentKhoListData);
                }
            }
        });

        return view;
    }

    private void addcontrols(View view) {
        tv_MVL = view.findViewById(R.id.tv_MVL);
        tv_SL = view.findViewById(R.id.tv_SL);
        tv_Name = view.findViewById(R.id.tv_Name);
        btnThoat = view.findViewById(R.id.btnThoat);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        lv_fragment_kho = view.findViewById(R.id.lv_fragment_kho);

        db210 = new qr210DB(getActivity());
        db210.open();

        bundle = getArguments();
        if (bundle != null) {
            String donvi = bundle.getString("DV");
            if (donvi.equals("null")) {
                donvi = "";
            }
            tv_MVL.setText(bundle.getString("MVL"));
            tv_SL.setText(bundle.getString("SL") + " " + donvi);
            tv_Name.setText(bundle.getString("TEN") + "\n" + bundle.getString("QC"));

            LOAI = bundle.getString("SACHLUOC");

            if (bundle.getString("MCT").substring(0, 2).equals("BC")) {
                XUONG = "D"; //德和
            } else {
                XUONG = "B"; //濱瀝
            }

            g_server = bundle.getString("SERVER");
        }

    }

    private void addEvent() {
        if (bundle != null) {
            new load_wh_data().execute("http://172.16.40.20/" + g_server + "/QR210/getdata_wh.php?mvl=" + bundle.getString("MVL") + "&xuong=" + XUONG + "&loai=" + LOAI);
        }

        btnListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnXacNhan: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        //builder.setTitle("確認信息");
                        //builder.setMessage(qr300_code);
                        //builder.setMessage(getString(R.string.M14));
                        builder.setMessage(getString(R.string.M20));
                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getActivity(), getString(R.string.no), Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (re_kho != null) {
                                    //Check dữ liệu Main có tồn tại cùng vật liệu cùng kho hay k?
                                    int check_239 = db210.check_239(bundle.getString("MCT"),
                                            Integer.parseInt(bundle.getString("HANGMUC")),
                                            bundle.getString("MVL"),
                                            re_kho, re_vitri, re_solo);
                                    if (check_239 == 1) {
                                        String g_qrb12 = bundle.getString("SACHLUOC");
                                        //update dữ liệu kho đã chọn vào qrb_file
                                        db210.upd_qrb_kho(bundle.getString("MCT"),
                                                Integer.parseInt(bundle.getString("HANGMUC")),
                                                re_kho, re_vitri, re_solo, re_sltonkho, re_donvi, g_qrb12);

                                        if (getFragmentManager().getBackStackEntryCount() != 0) {
                                            getFragmentManager().popBackStack();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.E18), Toast.LENGTH_SHORT).show();
                                    }


                                    //thông qua interface để refect lại listview
                                    qr210Interface.qr210Refect(true);
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.M21), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.show();
                        break;
                    }

                    case R.id.btnThoat: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setTitle("確認放棄否");
                        //builder.setMessage(qr300_code);
                        //builder.setMessage(getString(R.string.M14));
                        builder.setMessage(getString(R.string.M19));
                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getActivity(), getString(R.string.no), Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (getFragmentManager().getBackStackEntryCount() != 0) {
                                    getFragmentManager().popBackStack();
                                }
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
    }

    private class load_wh_data extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                /*"\uFEFF[]" nội dung được trả về khi truy vấn tồn kho trống, dựa theo nội dung đặt điều kiện để hiện thông báo lỗi*/
                if ((s.length() > 0) && (s.equals("\uFEFF[]")) != true) {
                    JSONArray jsonarray = new JSONArray(s);
                    qr210FragmentKhoListData = new ArrayList<>();
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
                        String img10 = jsonObject.getString("IMG10"); //數量

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
                        qr210FragmentKhoListData.add(new qr210_Fragment_Kho_ListData(String.valueOf(i + 1), img02, img03, img04, img10, img09, false));
                    }

                    //Đẩy dữ liệu từ mảng vào adapter
                    //lv_kho.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    qr210FragmentKhoAdapter = new qr210_Fragment_Kho_Adapter(getActivity(), R.layout.qr210_fragment_kho_row, qr210FragmentKhoListData);

                    //set dữu liệu từ adapter vào listview
                    lv_fragment_kho.setAdapter(qr210FragmentKhoAdapter);
                    //lv_kho.deferNotifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.E14), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.E14), Toast.LENGTH_SHORT).show();
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

    /*public void ganTieuDe(String MVL,String SL,String DV,String Ten,String QuiCach){
        tv_MVL.setText(MVL);
        tv_SL.setText(SL + " " + DV);
        tv_Name.setText(Ten + "\n" + QuiCach);

    }*/
}