package com.lelong.ll_khokythuat.QR211;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lelong.ll_khokythuat.QR210.Adapter.qr210_vt_listDataAdapter;
import com.lelong.ll_khokythuat.QR210.Model.qr210_vt_listData;
import com.lelong.ll_khokythuat.R;

import java.util.ArrayList;
import java.util.List;

public class GridView_Adapter2 extends ArrayAdapter<qr211_vt_listdata> {
    Context context;
    int resource;
    List<qr211_vt_listdata> objects;

    public GridView_Adapter2(@NonNull Context context, int resource, ArrayList<qr211_vt_listdata> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class ViewHolder {
        TextView stt;
        TextView img02;
        TextView img03;
        TextView img01;
        TextView ten;
        TextView ima27;
        TextView img10;
    }

    @SuppressLint({"WrongViewCast", "ResourceType"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);

            holder.stt = (TextView) convertView.findViewById(R.id.tv_stt);
            holder.img02 = (TextView) convertView.findViewById(R.id.tv_kho);
            holder.img03 = (TextView) convertView.findViewById(R.id.tv_vtri);
            holder.img01 = (TextView) convertView.findViewById(R.id.tv_mvl);
            holder.ten = (TextView) convertView.findViewById(R.id.tv_ten);
            holder.ima27 = (TextView) convertView.findViewById(R.id.tv_slat);
            holder.img10 = (TextView) convertView.findViewById(R.id.tv_sl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        qr211_vt_listdata vt = getItem(position);
        if (vt != null) {
            holder = (ViewHolder) convertView.getTag();
            // Gán giá trị cho các TextView
            holder.stt.setText(String.valueOf(position + 1));
            holder.img02.setText(objects.get(position).getImg02());
            holder.img03.setText(objects.get(position).getImg03());
            holder.img01.setText(objects.get(position).getImg01());
            holder.ten.setText(objects.get(position).getTen());
            holder.ima27.setText(String.valueOf(objects.get(position).getIma27()));
            holder.img10.setText(String.valueOf(objects.get(position).getImg10()));

        }

        return convertView;
    }

}

