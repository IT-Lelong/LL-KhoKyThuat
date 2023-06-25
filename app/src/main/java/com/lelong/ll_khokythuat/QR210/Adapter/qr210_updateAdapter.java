package com.lelong.ll_khokythuat.QR210.Adapter;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lelong.ll_khokythuat.QR210.Main.qr210DB;
import com.lelong.ll_khokythuat.QR210.Main.qr210_interface;
import com.lelong.ll_khokythuat.R;


public class qr210_updateAdapter extends SimpleCursorAdapter {
    Activity context;
    int layout;
    Cursor c;
    boolean cursorPresent;
    private final LayoutInflater inflater;
    View view;

    private qr210DB db210 = null;
    qr210_interface qr210Interface;


    public qr210_updateAdapter(Activity context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
        this.c = c;
        this.inflater = LayoutInflater.from(context);
        c.moveToFirst();

        cursorPresent = c != null;
        qr210Interface = (qr210_interface) context;

        db210 = new qr210DB(context);
        db210.open();

    }

    public class ViewHolder {
        TextView qrb02, qrb03, qrb041, qrb042, qrb051, qrb052, qrb053, qrb054, qrb061, qrb062;
        CheckedTextView simpleCheckedTextView;
    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Integer edt_qrb061 = 0;
        if (!cursorPresent) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!c.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        View v;
        if (convertView == null) {
            v = newView(context, c, parent);
        } else {
            v = convertView;
        }


        bindView(v, context, c);
        return v;
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        //View view = inflater.inflate(layout, parent, false);
        view = inflater.inflate(layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.qrb02 = (TextView) view.findViewById(R.id.qrb02);
        holder.qrb03 = (TextView) view.findViewById(R.id.qrb03);
        holder.qrb041 = (TextView) view.findViewById(R.id.qrb041);
        holder.qrb042 = (TextView) view.findViewById(R.id.qrb042);
        holder.qrb051 = (TextView) view.findViewById(R.id.qrb051);
        holder.qrb052 = (TextView) view.findViewById(R.id.qrb052);
        holder.qrb053 = (TextView) view.findViewById(R.id.qrb053);
        holder.qrb054 = (TextView) view.findViewById(R.id.qrb054);
        holder.qrb061 = (TextView) view.findViewById(R.id.qrb061);
        holder.qrb062 = (TextView) view.findViewById(R.id.qrb062);
        holder.simpleCheckedTextView = view.findViewById(R.id.simpleCheckedTextView);
        view.setTag(holder);

        return view;
    }

    public void bindView(View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.qrb02.setText(cursor.getString(cursor.getColumnIndexOrThrow("qrb02")));
        holder.qrb03.setText(cursor.getString(cursor.getColumnIndexOrThrow("qrb03")));
        holder.qrb041.setText(cursor.getString(cursor.getColumnIndexOrThrow("qrb04")));
        holder.qrb042.setText(cursor.getString(cursor.getColumnIndexOrThrow("qrb05")));

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb06")) == null) {
            holder.qrb051.setText("");
        } else {
            holder.qrb051.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb06"))));
        }

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb07")) == null) {
            holder.qrb052.setText("");
        } else {
            holder.qrb052.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb07"))));
        }

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb08")) == null) {
            holder.qrb053.setText("");
        } else {
            holder.qrb053.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb08"))));
        }

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb09")).equals("0")) {
            holder.qrb061.setText("");
        } else {
            holder.qrb061.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb09"))));
        }

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb10")) == null) {
            holder.qrb062.setText("");
        } else {
            holder.qrb062.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb10"))));
        }

        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb13")).equals("0")) {
            holder.qrb054.setText("");
        } else {
            holder.qrb054.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("qrb13"))));
        }

        holder.simpleCheckedTextView.setText(null);
        if (cursor.getString(cursor.getColumnIndexOrThrow("qrb11")).equals("true")) {
            holder.simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checked);
            holder.simpleCheckedTextView.setChecked(true);
            view.setBackgroundColor(context.getColor(R.color.ListSelector));
        } else {
            holder.simpleCheckedTextView.setCheckMarkDrawable(null);
            holder.simpleCheckedTextView.setChecked(false);
            view.setBackgroundColor(context.getColor(R.color.background));
        }

        //holder.qrb061.setSelection(holder.qrb061.getText().length()); //đặt vị trí con trỏ nhập về cuối chuỗi (áp dụng cho edit text)

    }

}
