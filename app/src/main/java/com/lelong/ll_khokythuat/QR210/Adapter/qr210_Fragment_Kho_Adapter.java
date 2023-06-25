package com.lelong.ll_khokythuat.QR210.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lelong.ll_khokythuat.QR210.Model.qr210_Fragment_Kho_ListData;
import com.lelong.ll_khokythuat.R;

import java.util.ArrayList;
import java.util.List;

public class qr210_Fragment_Kho_Adapter extends ArrayAdapter<qr210_Fragment_Kho_ListData> {
    Activity context;
    int resource;
    List<qr210_Fragment_Kho_ListData> objects;

    public qr210_Fragment_Kho_Adapter(@NonNull Activity context, int resource, @NonNull List<qr210_Fragment_Kho_ListData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public class MyViewHolder {
        TextView tv_fm_kho_hangmuc, tv_fm_kho_kho, tv_fm_kho_vitri, tv_fm_kho_solo, tv_fm_kho_soluong, tv_fm_kho_donvi;
        CheckBox cb_fm_kho_checkbox;
    }

    public void UpdateAdapter(ArrayList<qr210_Fragment_Kho_ListData> up_objects){
        this.objects = up_objects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);

            holder.tv_fm_kho_hangmuc = convertView.findViewById(R.id.tv_fm_kho_hangmuc);
            holder.tv_fm_kho_kho = convertView.findViewById(R.id.tv_fm_kho_kho);
            holder.tv_fm_kho_vitri = convertView.findViewById(R.id.tv_fm_kho_vitri);
            holder.tv_fm_kho_solo = convertView.findViewById(R.id.tv_fm_kho_solo);
            holder.tv_fm_kho_soluong = convertView.findViewById(R.id.tv_fm_kho_soluong);
            holder.tv_fm_kho_donvi = convertView.findViewById(R.id.tv_fm_kho_donvi);
            holder.cb_fm_kho_checkbox = convertView.findViewById(R.id.cb_fm_kho_checkbox);

            final View finalConvertView = convertView;
            holder.cb_fm_kho_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        finalConvertView.setBackgroundColor(context.getColor(R.color.ListSelector));
                    } else {
                        finalConvertView.setBackgroundColor(context.getColor(R.color.fragmentKhoBG));
                    }
                }
            });

            holder.cb_fm_kho_checkbox.setTag(objects.get(position));
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
            holder.cb_fm_kho_checkbox.setTag(objects.get(position));
        }

        qr210_Fragment_Kho_ListData listData = objects.get(position);
        if (listData != null) {
            holder.tv_fm_kho_hangmuc.setText(String.valueOf(position + 1));
            holder.tv_fm_kho_kho.setText(objects.get(position).getTv_fm_kho_kho());
            holder.tv_fm_kho_vitri.setText(objects.get(position).getTv_fm_kho_vitri());
            holder.tv_fm_kho_solo.setText(objects.get(position).getTv_fm_kho_solo());
            holder.tv_fm_kho_soluong.setText(objects.get(position).getTv_fm_kho_soluong());
            holder.tv_fm_kho_donvi.setText(objects.get(position).getTv_fm_kho_donvi());
            holder.cb_fm_kho_checkbox.setChecked(objects.get(position).isCheckBox());
        }

        return convertView;
    }
}
