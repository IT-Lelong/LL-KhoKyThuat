package com.lelong.ll_khokythuat.QR210.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lelong.ll_khokythuat.QR210.Model.qr210_check_ListData;
import com.lelong.ll_khokythuat.R;

import java.util.List;

public class qr210_checkAdapter extends ArrayAdapter<qr210_check_ListData> {
    Context context;
    int resource;
    List<qr210_check_ListData> objects;

    public qr210_checkAdapter(@NonNull Context context, int resource, @NonNull List<qr210_check_ListData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public class ViewHolder {
        TextView qrb02, qrb03, qrb041, qrb042, qrb051, qrb052, qrb053, qrb054, qrb061,qrb062;
        CheckedTextView simpleCheckedTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource,null);

            holder.qrb02 = (TextView) convertView.findViewById(R.id.qrb02);
            holder.qrb03 = (TextView) convertView.findViewById(R.id.qrb03);
            holder.qrb041 = (TextView) convertView.findViewById(R.id.qrb041);
            holder.qrb042 = (TextView) convertView.findViewById(R.id.qrb042);
            holder.qrb051 = (TextView) convertView.findViewById(R.id.qrb051);
            holder.qrb052 = (TextView) convertView.findViewById(R.id.qrb052);
            holder.qrb053 = (TextView) convertView.findViewById(R.id.qrb053);
            holder.qrb054 = (TextView) convertView.findViewById(R.id.qrb054);
            holder.qrb061 = (TextView) convertView.findViewById(R.id.qrb061);
            holder.qrb062 = (TextView) convertView.findViewById(R.id.qrb062);
            holder.simpleCheckedTextView = convertView.findViewById(R.id.simpleCheckedTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        qr210_check_ListData  dataList = this.objects.get(position);
        holder.qrb02.setText(String.valueOf(dataList.getInb03()));
        holder.qrb03.setText(dataList.getInb04());
        holder.qrb041.setText(dataList.getTa_ima02_1());
        holder.qrb042.setText(dataList.getTa_ima021_1());
        holder.qrb051.setText(dataList.getInb05());
        holder.qrb052.setText(dataList.getInb06());
        holder.qrb053.setText(dataList.getInb07());
        holder.qrb054.setText(String.valueOf(dataList.getImg10()));
        holder.qrb061.setText(String.valueOf(dataList.getInb09()));
        holder.qrb062.setText(dataList.getInb08());

        holder.simpleCheckedTextView.setText(null);
        if (dataList.isScan()) {
            holder.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checked);
            holder.simpleCheckedTextView.setChecked(true);
            convertView.setBackgroundColor(context.getColor(R.color.ListSelector));
        } else {
            holder.simpleCheckedTextView.setCheckMarkDrawable(null);
            holder.simpleCheckedTextView.setChecked(false);
            convertView.setBackgroundColor(context.getColor(R.color.background));
        }

        return convertView;
    }
}
