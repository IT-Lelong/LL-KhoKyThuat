package com.lelong.ll_khokythuat.QR211;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lelong.ll_khokythuat.Create_Table;
import com.lelong.ll_khokythuat.R;

import java.util.List;

public class GridView_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mData;
    String ID, g_INOUT, conf_kho, conf_vtri, l_vtri, g_server;
    Cursor cursor, cursor_1, cursor_2;
    private Create_Table createTable = null;
    String[] station = new String[0];
    ListView lv_dsdata1;


    public GridView_Adapter(Context context, List<String> data, String ID, String conf_kho, ListView lv_dsdata1, String g_server) {
        super(context, R.layout.activity_qr211_search_item, data);
        this.mContext = context;
        this.mData = data;
        this.ID = ID;
        this.conf_kho = conf_kho;
        this.lv_dsdata1 = lv_dsdata1;
        this.g_server = g_server;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_qr211_search_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(mData.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String l_vtri = mData.get(position);
                Intent SCAN = new Intent();
                SCAN.setClass(mContext, qr211_show_data1.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", ID);
                bundle.putString("KHO", conf_kho);
                bundle.putString("VITRI", l_vtri);
                bundle.putString("SERVER", g_server);
                SCAN.putExtras(bundle);
                mContext.startActivity(SCAN);
            }
        });
        return convertView;
    }
}
