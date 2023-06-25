package com.lelong.ll_khokythuat.QR210.Adapter;

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

import com.lelong.ll_khokythuat.QR210.Model.qr210_vt_listData;
import com.lelong.ll_khokythuat.R;

import java.util.List;

public class qr210_vt_listDataAdapter extends ArrayAdapter<qr210_vt_listData> {
    Context context;
    int resource;
    List<qr210_vt_listData> objects;

    public qr210_vt_listDataAdapter(@NonNull Context context, int resource, List<qr210_vt_listData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;

    }

    private class ViewHolder {
        TextView stt;
        TextView img02;
        TextView img03;
        TextView img04;
        TextView img09;
        TextView img10;
        EditText uses;
        //CheckBox checkBox;
        ImageButton ibtnTang;
        ImageButton ibtnGiam;
    }

    @SuppressLint({"WrongViewCast", "ResourceType"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        //((ViewHolder) convertView.getTag()).checkBox.setTag(objects.get(position));

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext()); //Built layout bình thường thành code java để sử dụng
            convertView = inflater.inflate(resource, null);

            holder.stt = (TextView) convertView.findViewById(R.id.tv_vt_stt);
            holder.img02 = (TextView) convertView.findViewById(R.id.tv_vt_img02);
            holder.img03 = (TextView) convertView.findViewById(R.id.tv_vt_img03);
            holder.img04 = (TextView) convertView.findViewById(R.id.tv_vt_img04);
            holder.img09 = (TextView) convertView.findViewById(R.id.tv_vt_img09);
            holder.img10 = (TextView) convertView.findViewById(R.id.tv_vt_img10);
            holder.uses = (EditText) convertView.findViewById(R.id.tv_vt_uses);
            holder.ibtnTang = (ImageButton) convertView.findViewById(R.id.ibtnTang);
            holder.ibtnGiam = (ImageButton) convertView.findViewById(R.id.ibtnGiam);
            //holder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_vt_check);

            convertView.setTag(holder);

           /* final View finalView = convertView;
            // final ViewHolder finalHolder = holder;
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        finalView.setBackgroundColor(context.getColor(R.color.ListSelector));
                    } else {
                        finalView.setBackgroundColor(context.getColor(R.color.background));
                    }
                }
            });

            holder.checkBox.setTag(objects.get(position));*/


            // CheckedTextView(S)
           /* final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.tv_vt_check);
            //simpleCheckedTextView.setText("");
            // perform on Click Event Listener on CheckedTextView
            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (simpleCheckedTextView.isChecked()) {
                        // set cheek mark drawable and set checked property to false
                        simpleCheckedTextView.setCheckMarkDrawable(0);
                        simpleCheckedTextView.setChecked(false);
                        Toast.makeText(context," " + position,Toast.LENGTH_SHORT).show();

                    } else {
                        // set cheek mark drawable and set checked property to true
                        simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checked);
                        simpleCheckedTextView.setChecked(true);
                        Toast.makeText(context," " + position,Toast.LENGTH_SHORT).show();
                        qr210_vt_listData vt2 = getItem(position);
                    }

                }
            });*/
            // CheckedTextView(E)
        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.checkBox.setTag(objects.get(position));
            //((ViewHolder) convertView.getTag()).checkBox.setTag(objects.get(position));
        }
        qr210_vt_listData vt = getItem(position);
        if (vt != null) {
            holder = (ViewHolder) convertView.getTag();

            // Capture position and set to the TextViews
            holder.stt.setText(String.valueOf(position + 1));
            holder.img02.setText(objects.get(position).getImg02());
            holder.img03.setText(objects.get(position).getImg03());
            holder.img04.setText(objects.get(position).getImg04());
            holder.img09.setText(objects.get(position).getImg09());
            holder.img10.setText(String.valueOf(objects.get(position).getImg10()));
            holder.uses.setText(String.valueOf(objects.get(position).getUses()));
            //holder.uses.setText("1");
            // holder.checkBox.setChecked(objects.get(position).isSelected());

            final ViewHolder finalHolder = holder;
            holder.ibtnTang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int values = Integer.parseInt(finalHolder.uses.getText().toString());

                    if (values < Integer.parseInt(finalHolder.img10.getText().toString())) {
                        finalHolder.uses.setText(String.valueOf(values + 1));
                        objects.get(position).setUses(values + 1);
                    } else {
                        Toast.makeText(context, "已超過庫存量", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.ibtnGiam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int values = Integer.parseInt(finalHolder.uses.getText().toString());
                    if (values > 0) {
                        finalHolder.uses.setText(String.valueOf(values - 1));
                        objects.get(position).setUses(values - 1);
                    } else {
                        finalHolder.uses.setText(String.valueOf(0));
                    }
                }
            });

            holder.uses.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Gọi trước khi text thay đổi

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Gọi khi thay đổi
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Gọi sau khi thay đổi

                }
            });

        }
        return convertView;
    }
}
