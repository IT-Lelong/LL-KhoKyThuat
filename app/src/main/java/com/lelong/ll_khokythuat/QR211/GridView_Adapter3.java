package com.lelong.ll_khokythuat.QR211;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lelong.ll_khokythuat.R;

import java.util.ArrayList;
import java.util.List;

public class GridView_Adapter3 extends ArrayAdapter<qr211_vt_listdata> {
    Context context;
    int resource;
    List<qr211_vt_listdata> objects;

    public GridView_Adapter3(@NonNull Context context, int resource, ArrayList<qr211_vt_listdata> objects) {
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
        GridView_Adapter3.ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, null);

            holder.stt = (TextView) convertView.findViewById(R.id.tv_stt2);
            holder.img02 = (TextView) convertView.findViewById(R.id.tv_kho2);
            holder.img03 = (TextView) convertView.findViewById(R.id.tv_vtri2);
            holder.img01 = (TextView) convertView.findViewById(R.id.tv_mvl2);
            holder.ten = (TextView) convertView.findViewById(R.id.tv_ten2);
            holder.ima27 = (TextView) convertView.findViewById(R.id.tv_slat2);
            holder.img10 = (TextView) convertView.findViewById(R.id.tv_sl2);

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
